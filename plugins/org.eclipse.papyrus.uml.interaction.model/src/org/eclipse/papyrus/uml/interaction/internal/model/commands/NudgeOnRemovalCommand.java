/*****************************************************************************
 * Copyright (c) 2018 Johannes Faltermeier and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Johannes Faltermeier - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.interaction.internal.model.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.interaction.internal.model.impl.MElementImpl;
import org.eclipse.papyrus.uml.interaction.internal.model.impl.MInteractionImpl;
import org.eclipse.papyrus.uml.interaction.model.MElement;
import org.eclipse.papyrus.uml.interaction.model.MExecution;
import org.eclipse.papyrus.uml.interaction.model.MInteraction;
import org.eclipse.papyrus.uml.interaction.model.MLifeline;
import org.eclipse.papyrus.uml.interaction.model.MMessage;
import org.eclipse.papyrus.uml.interaction.model.MOccurrence;
import org.eclipse.papyrus.uml.interaction.model.spi.NoopCommand;
import org.eclipse.uml2.uml.Element;

/**
 * This command analyses the current graph and fills the white space created by the deletion of elements.
 */
public class NudgeOnRemovalCommand extends ModelCommand<MInteractionImpl> {

	private static final int DEFAULT_VERTICAL_OFFSET = 10;

	private Set<MElement<? extends Element>> mElementsToRemove;

	private MInteraction interaction;

	/**
	 * Creates a new {@link NudgeCommand}.
	 * 
	 * @param editingDomain
	 *            the {@link EditingDomain}
	 * @param interaction
	 *            the {@link MInteractionImpl}
	 * @param elementsToRemove
	 *            the elements which will be removed
	 */
	public NudgeOnRemovalCommand(EditingDomain editingDomain, MInteractionImpl interaction,
			Collection<Element> elementsToRemove) {
		super(interaction);

		this.interaction = interaction;
		this.mElementsToRemove = getRemovedMElements(elementsToRemove);

		List<Command> nudgeCommands = new ArrayList<>();
		nudgeCommands.addAll(createVerticalNudgeCommands());
		nudgeCommands.addAll(createHorizontalNudgeCommands());

		/* create the command */
		if (nudgeCommands.isEmpty()) {
			command = NoopCommand.INSTANCE;
		} else {
			command = CompoundModelCommand.compose(editingDomain, nudgeCommands);
		}

	}

	/**
	 * Maps the removed {@link Element}s to their respective {@link MElement}. Filters out {@link MOccurrence}
	 * as they are currently just point values connected to other {@link MElement}s.
	 * 
	 * @param elementsToRemove
	 *            the removed {@link Element elements}
	 */
	private Set<MElement<? extends Element>> getRemovedMElements(Collection<Element> elementsToRemove) {
		return elementsToRemove.stream()//
				.map(interaction::getElement)//
				.filter(Optional::isPresent)//
				.map(Optional::get)//
				.filter(e -> !MOccurrence.class.isInstance(e))// TODO MGate special case once available
				.collect(Collectors.toSet());
	}

	@SuppressWarnings("boxing")
	private List<Command> createVerticalNudgeCommands() {
		List<Command> nudgeCommands = new ArrayList<>();
		/* find out the deleted range */
		int deletedTop = Integer.MAX_VALUE;
		int deletedBottom = Integer.MIN_VALUE;
		for (MElement<? extends Element> element : mElementsToRemove) {
			OptionalInt top = getTop(element);
			if (top.isPresent() && top.getAsInt() < deletedTop) {
				deletedTop = top.getAsInt();
			}
			OptionalInt bottom = element.getBottom();
			if (bottom.isPresent() && bottom.getAsInt() > deletedBottom) {
				deletedBottom = bottom.getAsInt();
			}
		}
		final int topFinal = deletedTop;
		final int bottomFinal = deletedBottom;

		/* put remaining elements in maps based on top/bottom values */
		Map<Integer, Set<MElement<? extends Element>>> topToElements = new LinkedHashMap<>();
		Map<Integer, Set<MElement<? extends Element>>> bottomToElements = new LinkedHashMap<>();
		for (MLifeline lifeline : interaction.getLifelines()) {
			for (MExecution execution : lifeline.getExecutions()) {
				putValuesInMaps(execution, topToElements, bottomToElements);
			}
		}
		for (MMessage message : interaction.getMessages()) {
			putValuesInMaps(message, topToElements, bottomToElements);
		}

		/* analyse the starting points of remaining elements in comparison with deleted range */

		/* find out if we have to move elements which start in the deleted range */
		if (shouldMoveUpperElements(topFinal, bottomFinal, topToElements, bottomToElements)) {

			Set<Integer> topsStartingAfter = topToElements.keySet().stream()//
					.filter(top -> top >= topFinal)//
					.collect(Collectors.toSet());
			Optional<Integer> min = topsStartingAfter.stream().min(Integer::compare);
			int delta = topFinal - min.orElse(topFinal) + additionalVerticalOffSet();
			topsStartingAfter.stream()//
					.flatMap(key -> topToElements.get(key).stream())//
					.forEach(element -> nudgeCommands
							.add(new NudgeCommand((MElementImpl<? extends Element>)element, delta, false)));
		}

		/* find out if we have to move elements which start after the deleted range */
		if (shouldMoveBottomElements(bottomFinal, topToElements)) {
			Set<Integer> topsBelowDeletion = topToElements.keySet().stream()//
					.filter(top -> top >= bottomFinal)//
					.collect(Collectors.toSet());
			Optional<Integer> min = topsBelowDeletion.stream().min(Integer::compare);

			/* if there is an undeleted element ending in deletion range use this as benchmark */
			Optional<Integer> benchmark = bottomToElements.keySet().stream()//
					.filter(bottom -> bottom >= topFinal && bottom <= bottomFinal)//
					.max(Integer::max);

			int delta;
			if (benchmark.isPresent()) {
				/* keep previous distance to deleted range and move below benchmark */
				delta = (benchmark.get()) - min.orElse(benchmark.get())
						+ (min.orElse(bottomFinal) - bottomFinal);
			} else {
				/* move to top of deleted range */
				delta = topFinal - min.orElse(topFinal) + additionalVerticalOffSet();
			}

			topsBelowDeletion.stream()//
					.flatMap(key -> topToElements.get(key).stream())//
					.forEach(element -> nudgeCommands
							.add(new NudgeCommand((MElementImpl<? extends Element>)element, delta, false)));
		}
		return nudgeCommands;
	}

	/**
	 * Checks if an additional vertical offset is required.
	 */
	private int additionalVerticalOffSet() {
		if (mElementsToRemove.stream()//
				.anyMatch(MLifeline.class::isInstance)) {
			/* ensure there is a gap after the start of a lifeline */
			return DEFAULT_VERTICAL_OFFSET;
		}
		return 0;
	}

	private List<Command> createHorizontalNudgeCommands() {
		List<Command> nudgeCommands = new ArrayList<>();
		/* find the deleted lifelines and order all lifelines from left to right */
		Set<MLifeline> deletedLifelines = mElementsToRemove.stream()//
				.filter(MLifeline.class::isInstance)//
				.map(MLifeline.class::cast)//
				.collect(Collectors.toSet());

		List<MLifeline> lifelinesLeftToRight = interaction.getLifelines().stream()//
				.sorted((l1, l2) -> {
					return l1.getLeft().orElse(Integer.MAX_VALUE) - l2.getLeft().orElse(Integer.MAX_VALUE);
				}).collect(Collectors.toList());

		/* loop over lifelines from left to right and create nudge commands based on deleted lifelines */
		int delta = 0;
		MLifeline deleted = null;
		for (MLifeline lifeline : lifelinesLeftToRight) {
			if (deleted != null) {
				delta = deleted.getLeft().orElse(Integer.MAX_VALUE)
						- lifeline.getLeft().orElse(Integer.MAX_VALUE);
				deleted = null;
			}
			if (deletedLifelines.contains(lifeline)) {
				deleted = lifeline;
			}

			if (deleted == null && delta != 0) {
				nudgeCommands.add(lifeline.nudgeHorizontally(delta));
				delta = 0;
			}
		}
		return nudgeCommands;
	}

	/**
	 * Returns the top of a given {@link MElement}. This may increase the top value for some {@link MElement}s
	 * based on whether child elements may be placed directly at this coordinate or not.
	 */
	private OptionalInt getTop(MElement<? extends Element> element) {
		if (element instanceof MLifeline) {
			View shape = ((MLifeline)element).getDiagramView().orElse(null);
			int lifelineTop = layoutHelper()//
					.getTop(diagramHelper().getLifelineBodyShape(shape));
			return OptionalInt.of(lifelineTop);
		}
		return element.getTop();
	}

	/** Whether elements in the deleted range have to be moved up. */
	@SuppressWarnings("boxing")
	private boolean shouldMoveUpperElements(final int topFinal, final int bottomFinal,
			Map<Integer, Set<MElement<? extends Element>>> topToElements,
			Map<Integer, Set<MElement<? extends Element>>> bottomToElements) {

		/*
		 * this is the case if the top-most element in this range starts after the deletion range and it is
		 * not anchored by an element ending in the range but starting before
		 */
		Optional<Integer> topElementStartingInRange = topToElements.keySet().stream()//
				.filter(top -> top > topFinal && top <= bottomFinal)// starting in range
				.sorted(Integer::min).findFirst();
		if (!topElementStartingInRange.isPresent()) {
			return false;
		} else {
			/* now check for anchor. element has to end in range and must be before our top element */
			Optional<Integer> anchor = bottomToElements.keySet().stream()//
					.filter(bottom -> bottom >= topFinal && bottom < bottomFinal)// ending in range
					.filter(bottom -> bottom < topElementStartingInRange.get())//
					.findAny();
			return !anchor.isPresent();
		}
	}

	/** Whether elements starting below the deleted range need to be moved up. */
	@SuppressWarnings("boxing")
	private boolean shouldMoveBottomElements(final int bottomFinal,
			Map<Integer, Set<MElement<? extends Element>>> topToElements) {
		/*
		 * this is NOT the case if we have an element which starts in or before the deleted range and ends
		 * after the deleted range. In all other cases we want to move.
		 */
		return !topToElements.keySet().stream()//
				.filter(top -> top <= bottomFinal)// starts in or before range
				.flatMap(key -> topToElements.get(key).stream())//
				.filter(e -> e.getBottom().orElse(bottomFinal) > bottomFinal)// ends after range
				.findAny()//
				.isPresent();
	}

	@SuppressWarnings("boxing")
	private void putValuesInMaps(MElement<? extends Element> element,
			Map<Integer, Set<MElement<? extends Element>>> topToElements,
			Map<Integer, Set<MElement<? extends Element>>> bottomToElements) {
		if (mElementsToRemove.contains(element)) {
			return;
		}

		Integer top = getTop(element).orElse(Integer.MAX_VALUE);
		Integer bottom = element.getBottom().orElse(Integer.MIN_VALUE);
		if (!topToElements.containsKey(top)) {
			topToElements.put(top, new LinkedHashSet<>());
		}
		if (!bottomToElements.containsKey(bottom)) {
			bottomToElements.put(bottom, new LinkedHashSet<>());
		}
		topToElements.get(top).add(element);
		bottomToElements.get(bottom).add(element);
	}

}
