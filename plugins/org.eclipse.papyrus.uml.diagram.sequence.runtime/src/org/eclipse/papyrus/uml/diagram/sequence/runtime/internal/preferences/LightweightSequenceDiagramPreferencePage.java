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

package org.eclipse.papyrus.uml.diagram.sequence.runtime.internal.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.papyrus.infra.gmfdiag.preferences.pages.DiagramPreferencePage;
import org.eclipse.papyrus.uml.diagram.sequence.runtime.internal.Activator;
import org.eclipse.papyrus.uml.diagram.sequence.runtime.internal.Messages;
import org.eclipse.papyrus.uml.interaction.model.spi.ViewTypes;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * This is the {@code LightweightSequenceDiagramPreferencePage} type. Enjoy.
 *
 * @author Christian W. Damus
 */
public class LightweightSequenceDiagramPreferencePage extends DiagramPreferencePage {

	private BooleanFieldEditor autoCreateExecution;

	private BooleanFieldEditor autoCreateReply;

	/**
	 * Initializes me.
	 */
	public LightweightSequenceDiagramPreferencePage() {
		super();

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setPreferenceKey(ViewTypes.LIGHTWEIGHT_SEQUENCE_DIAGRAM);
	}

	@Override
	protected Control createContents(Composite parent) {
		Group optionsGroup = new Group(parent, 2);
		optionsGroup.setLayout(new GridLayout());
		optionsGroup.setText(Messages.SynchronousMessagePreferencesLabel);

		autoCreateExecution = new BooleanFieldEditor(
				LightweightSequenceDiagramPreferences.AUTO_CREATE_EXEC_AFTER_SYNC_MESSAGE,
				Messages.CreateExecutionAfterSyncMessageAutomaticallyLabel, optionsGroup);
		autoCreateExecution.setPage(this);

		autoCreateReply = new BooleanFieldEditor(
				LightweightSequenceDiagramPreferences.AUTO_CREATE_REPLY_MESSAGE,
				Messages.CreateReplyMessageAutomaticallyLabel, optionsGroup);
		autoCreateReply.setPage(this);

		addField(autoCreateExecution);
		addField(autoCreateReply);

		return super.createContents(parent);
	}

}
