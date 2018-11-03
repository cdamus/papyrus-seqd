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

package org.eclipse.papyrus.uml.interaction.model.util;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

/**
 * Static utilities for APIs missing from the {@code Optional} class.
 */
public class Optionals {

	/**
	 * Not instantiable by clients.
	 */
	private Optionals() {
		super();
	}

	/**
	 * Test type and cast in one step.
	 * 
	 * @param optional
	 *            an optional
	 * @param asType
	 *            the type to try to cast
	 * @return the cast result
	 */
	public static <T, U> Optional<U> as(Optional<? super T> optional, Class<? extends U> asType) {
		return optional.filter(asType::isInstance).map(asType::cast);
	}

	/**
	 * Unwrap a stream of optionals as a stream of the present values.
	 * 
	 * @param stream
	 *            a stream of optionals
	 * @return a stream of the present values
	 */
	public static <T> Stream<T> mapPresent(Stream<Optional<T>> stream) {
		return stream.filter(Optional::isPresent).map(Optional::get);
	}

	/**
	 * Map an {@code optional} int under an integer-valued unary {@code operator}.
	 * 
	 * @param optional
	 *            an optional integer value
	 * @param operator
	 *            an operator to apply
	 * @return the optional {@code operator} result
	 */
	public static OptionalInt map(OptionalInt optional, IntUnaryOperator operator) {
		return optional.isPresent() ? OptionalInt.of(operator.applyAsInt(optional.getAsInt())) : optional;
	}

	/**
	 * Map an optional integer value to an object.
	 * 
	 * @param optionalInt
	 *            an optional integer value
	 * @param mapping
	 *            the integer mapping function
	 * @return the optional result of the {@code mapping}
	 */
	public static <R> Optional<R> mapToObj(OptionalInt optionalInt, IntFunction<? extends R> mapping) {
		return optionalInt.isPresent() ? Optional.ofNullable(mapping.apply(optionalInt.getAsInt()))
				: Optional.empty();
	}

	/**
	 * Flat-map an optional integer value to an object.
	 * 
	 * @param optionalInt
	 *            an optional integer value
	 * @param mapping
	 *            the integer mapping function
	 * @return the optional result of the {@code mapping}
	 */
	@SuppressWarnings("unchecked")
	public static <R> Optional<R> flatMapToObj(OptionalInt optionalInt,
			IntFunction<Optional<? extends R>> mapping) {

		// Cast is unnecessary because Optional is immutable
		return optionalInt.isPresent() ? (Optional<R>)mapping.apply(optionalInt.getAsInt())
				: Optional.empty();
	}

	/**
	 * Map an optional to an optional-int under an integer function.
	 * 
	 * @param optional
	 *            an optional to map
	 * @param mapping
	 *            the integer function to apply
	 * @return the optional result of the {@code mapping}
	 */
	public static <T> OptionalInt mapToInt(Optional<T> optional, ToIntFunction<? super T> mapping) {
		return optional.map(v -> OptionalInt.of(mapping.applyAsInt(v))).orElse(OptionalInt.empty());
	}

	/**
	 * Map an optional to an optional-int under an optional-integer function.
	 * 
	 * @param optional
	 *            an optional to map
	 * @param mapping
	 *            the optional-integer function to apply
	 * @return the optional result of the {@code mapping}
	 */
	public static <T> OptionalInt flatMapToInt(Optional<T> optional,
			Function<? super T, OptionalInt> mapping) {
		return optional.map(mapping).orElse(OptionalInt.empty());
	}

	/**
	 * A cascading <em>if-elseif</em> sequence of optionals. For a final <em>else</em> case, just do any
	 * variant of {@link Optional#orElse(Object)} on the result of this operation.
	 * 
	 * @param optional
	 *            an optional for the <em>if</em> case
	 * @param orElse
	 *            optionals for <em>elseif</em> cases
	 * @return the optional result of whichever case {@linkplain Optional#isPresent() is present} or empty if
	 *         none
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> Optional<T> elseMaybe(Optional<? extends T> optional, Optional<? extends T>... orElse) {
		if (optional.isPresent()) {
			// Don't do the work of Optional.of(optional.get()) for type safety because Optional is immutable
			return (Optional<T>)optional;
		}
		for (Optional<? extends T> next : orElse) {
			if (next.isPresent()) {
				return (Optional<T>)next;
			}
		}
		return Optional.empty();
	}

	/**
	 * A cascading <em>if-elseif</em> sequence of optionals. For a final <em>else</em> case, just do any
	 * variant of {@link Optional#orElse(Object)} on the result of this operation.
	 * 
	 * @param optional
	 *            an optional for the <em>if</em> case
	 * @param orElse
	 *            suppliers of optionals for <em>elseif</em> cases, to defer execution (short-cut semantics)
	 * @return the optional result of whichever case {@linkplain Optional#isPresent() is present} or empty if
	 *         none
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> Optional<T> elseMaybe(Optional<? extends T> optional,
			Supplier<Optional<? extends T>>... orElse) {
		if (optional.isPresent()) {
			// Don't do the work of Optional.of(optional.get()) for type safety because Optional is immutable
			return (Optional<T>)optional;
		}
		for (Supplier<Optional<? extends T>> next : orElse) {
			Optional<? extends T> possibleResult = next.get();
			if (possibleResult.isPresent()) {
				return (Optional<T>)possibleResult;
			}
		}
		return Optional.empty();
	}

	/**
	 * Get the minimum of two optional integers. If eith4r is {@link Optional#empty() empty}, then the minimum
	 * is the other.
	 * 
	 * @param a
	 *            an optional integer
	 * @param b
	 *            another optional integer
	 * @return the minimum of the two
	 */
	public static OptionalInt min(OptionalInt a, OptionalInt b) {
		return a.isPresent() ? b.isPresent() ? OptionalInt.of(Math.min(a.getAsInt(), b.getAsInt())) : a : b;
	}

	/**
	 * Get the minimum of two optional integers. If eith4r is {@link Optional#empty() empty}, then the minimum
	 * is the other.
	 * 
	 * @param a
	 *            an optional integer
	 * @param b
	 *            another optional integer
	 * @return the minimum of the two
	 */
	public static OptionalInt max(OptionalInt a, OptionalInt b) {
		return a.isPresent() ? b.isPresent() ? OptionalInt.of(Math.max(a.getAsInt(), b.getAsInt())) : a : b;
	}

	/**
	 * Unwrap some {@code optionals} into a stream of the non-{@link Optional#empty() empty} values.
	 * 
	 * @param optionals
	 *            some optional values
	 * @return a stream of the {@link Optional#isPresent() present} values
	 */
	@SafeVarargs
	public static <T> Stream<T> stream(Optional<? extends T>... optionals) {
		return Stream.of(optionals).filter(Optional::isPresent).map(Optional::get);
	}

	/**
	 * Is an optional integer less than another?
	 * 
	 * @param a
	 *            an optional integer
	 * @param b
	 *            another optional integer
	 * @return {@code true} if both values are present and {@code a} is less than {@code b}; {@code false}
	 *         under any other circumstance
	 */
	public static boolean lessThan(OptionalInt a, OptionalInt b) {
		return a.orElse(Integer.MAX_VALUE) < b.orElse(Integer.MIN_VALUE);
	}

}
