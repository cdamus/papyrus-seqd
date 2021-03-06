/**
 * Copyright (c) 2018 Christian W. Damus and others.
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Christian W. Damus - Initial API and implementation
 * 
 */
package org.eclipse.papyrus.uml.interaction.model.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.papyrus.uml.interaction.model.util.SequenceDiagramAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers. The adapters
 * generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged
 * fireNotifyChanged}. The adapters also support Eclipse property sheets. Note that most of the adapters are
 * shared among multiple instances. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class SequenceDiagramItemProviderAdapterFactory extends SequenceDiagramAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<>();

	/**
	 * This constructs an instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SequenceDiagramItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.papyrus.uml.interaction.model.MInteraction} instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected MInteractionItemProvider mInteractionItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.uml.interaction.model.MInteraction}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Adapter createMInteractionAdapter() {
		if (mInteractionItemProvider == null) {
			mInteractionItemProvider = new MInteractionItemProvider(this);
		}

		return mInteractionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.papyrus.uml.interaction.model.MLifeline} instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected MLifelineItemProvider mLifelineItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.uml.interaction.model.MLifeline}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Adapter createMLifelineAdapter() {
		if (mLifelineItemProvider == null) {
			mLifelineItemProvider = new MLifelineItemProvider(this);
		}

		return mLifelineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.papyrus.uml.interaction.model.MExecution} instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected MExecutionItemProvider mExecutionItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.uml.interaction.model.MExecution}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Adapter createMExecutionAdapter() {
		if (mExecutionItemProvider == null) {
			mExecutionItemProvider = new MExecutionItemProvider(this);
		}

		return mExecutionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.papyrus.uml.interaction.model.MExecutionOccurrence} instances. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MExecutionOccurrenceItemProvider mExecutionOccurrenceItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.uml.interaction.model.MExecutionOccurrence}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Adapter createMExecutionOccurrenceAdapter() {
		if (mExecutionOccurrenceItemProvider == null) {
			mExecutionOccurrenceItemProvider = new MExecutionOccurrenceItemProvider(this);
		}

		return mExecutionOccurrenceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.papyrus.uml.interaction.model.MMessageEnd} instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected MMessageEndItemProvider mMessageEndItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.uml.interaction.model.MMessageEnd}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Adapter createMMessageEndAdapter() {
		if (mMessageEndItemProvider == null) {
			mMessageEndItemProvider = new MMessageEndItemProvider(this);
		}

		return mMessageEndItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.papyrus.uml.interaction.model.MMessage} instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected MMessageItemProvider mMessageItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.uml.interaction.model.MMessage}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Adapter createMMessageAdapter() {
		if (mMessageItemProvider == null) {
			mMessageItemProvider = new MMessageItemProvider(this);
		}

		return mMessageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.papyrus.uml.interaction.model.MDestruction} instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected MDestructionItemProvider mDestructionItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.uml.interaction.model.MDestruction}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Adapter createMDestructionAdapter() {
		if (mDestructionItemProvider == null) {
			mDestructionItemProvider = new MDestructionItemProvider(this);
		}

		return mDestructionItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void dispose() {
		if (mInteractionItemProvider != null) {
			mInteractionItemProvider.dispose();
		}
		if (mLifelineItemProvider != null) {
			mLifelineItemProvider.dispose();
		}
		if (mExecutionItemProvider != null) {
			mExecutionItemProvider.dispose();
		}
		if (mExecutionOccurrenceItemProvider != null) {
			mExecutionOccurrenceItemProvider.dispose();
		}
		if (mMessageEndItemProvider != null) {
			mMessageEndItemProvider.dispose();
		}
		if (mMessageItemProvider != null) {
			mMessageItemProvider.dispose();
		}
		if (mDestructionItemProvider != null) {
			mDestructionItemProvider.dispose();
		}
	}

}
