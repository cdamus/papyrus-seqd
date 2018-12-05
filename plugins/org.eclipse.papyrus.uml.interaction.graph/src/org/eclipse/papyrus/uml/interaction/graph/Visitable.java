/*****************************************************************************
 * Copyright (c) 2018 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Christian W. Damus - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.uml.interaction.graph;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Protocol for an object that can be visited, and which is related to others that can be visited in some kind
 * of order. A visitable object can have successors and/or predecessors in this ordering, which can be visited
 * in a forwards or backwards walk (respectively) by a {@link Visitor}
 *
 * @param <T>
 *            the type of visitable graph element
 * @see Visitor
 * @author Christian W. Damus
 */
public interface Visitable<T extends Visitable<T>> {
	/**
	 * Accept a visitor over my type.
	 *
	 * @param visitor
	 * @see Visitor#visit(Visitable)
	 */
	void accept(Visitor<? super T> visitor);

	/**
	 * Queries the graph that contains me. Everything visitiable is in a graph.
	 * 
	 * @return my graph
	 */
	Graph graph();

	/**
	 * Obtains my immediate successors in my canonical visitation order.
	 *
	 * @return my immediate successors
	 * @see #successors()
	 */
	Stream<T> immediateSuccessors();

	/**
	 * Obtains my successors and their successors and so on downstream over the rest of the subgraph starting
	 * from me.
	 *
	 * @return my transitive successor subgraph
	 * @see #immediateSuccessors()
	 */
	default Stream<T> successors() {
		return immediateSuccessors().flatMap(succ -> Stream.concat(Stream.of(succ), succ.successors()))
				.distinct();
	}

	/**
	 * Obtains my immediate predecessors in my canonical visitation order.
	 *
	 * @return my immediate predecessors
	 * @see #predecessors()
	 */
	Stream<T> immediatePredecessors();

	/**
	 * Obtains my predecessors and their predecessors and so on upstream over the rest of the subgraph
	 * starting from me.
	 *
	 * @return my transitive predecessor subgraph
	 * @see #immediatePredecessors()
	 */
	default Stream<T> predecessors() {
		return immediatePredecessors().flatMap(pred -> Stream.concat(Stream.of(pred), pred.predecessors()))
				.distinct();
	}

	/**
	 * Obtains a stream over the groups of which this graph element is a member.
	 * 
	 * @return the groups of which I am a member
	 */
	Stream<Group<T>> groups();

	/**
	 * Queries the group of the given {@code kind} of which I am a member. I can be a member of any number of
	 * groups but at most one group of any particular kind.
	 * 
	 * @param kind
	 *            the kind of group
	 * @return the group of that kind of which I am a member
	 */
	default Optional<Group<T>> group(GroupKind kind) {
		return groups().filter(kind.predicate()).findAny();
	}

	/**
	 * Query whether this element still properly exists in the {@linkplain #graph() graph}. It may not exist
	 * if, for example, the model element that it represents has since been deleted.
	 * 
	 * @return whether this element exists
	 */
	boolean exists();

	/**
	 * Query whether I am a {@link Group}.
	 * 
	 * @return whether I am a {@linkplain Group group} of visitable things.
	 * @see #toGroup()
	 */
	default boolean isGroup() {
		return this instanceof Group;
	}

	/**
	 * Cast me as a {@link Group}.
	 * 
	 * @return myself, as a {@code Group}
	 * @throws ClassCastException
	 *             if I am {@linkplain #isGroup() not a group}
	 * @see #isGroup()
	 */
	default Group<T> toGroup() {
		return (Group<T>)this;
	}
}
