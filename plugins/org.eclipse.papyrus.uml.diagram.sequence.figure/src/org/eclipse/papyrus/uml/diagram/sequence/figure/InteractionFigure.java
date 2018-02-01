/*****************************************************************************
 * Copyright (c) 2018 EclipseSource and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - Initial API and implementation
 *   
 *****************************************************************************/
package org.eclipse.papyrus.uml.diagram.sequence.figure;

import org.eclipse.draw2d.RectangleFigure;

/**
 *  Figure displayed for the interaction, as the main frame. 
 */
public class InteractionFigure extends RectangleFigure {

	/**
	 * Constructor.
	 */
	public InteractionFigure() {
	}
	
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}
}
