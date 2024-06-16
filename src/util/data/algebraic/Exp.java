package util.data.algebraic;

import java.util.function.Function;

import util.Preconditions;

/**
 * Class to represent the algebraic exponential type. The exponential does not have a recursor, but
 * instead has the {@link Exp#curry(Function)} and {@link Exp#uncurry(Function)} methods to transform
 * functions of products ({@link Prod}) into functions that produce exponentials ({@link Exp}).
 * Additionally, the static method {@link Exp#asExponential(Function)} is allowed to directly transform
 * normal Java functions into exponential objects
 */
public final class Exp<A,B> implements Function<A,B> {

    private final Function<A, B> function;
    
    private Exp(final Function<A, B> function) {
        this.function = function;
    }

    /**
     * Composes this exponential with the provided one.
     * 
     * @param <X> the initial type
     * @param before the exponential that will be "executed" before this exponential
     * @return the composition
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public final <X> Exp<X, B> after(final Exp<X, A> before) {
        Preconditions.throwIfNull(before, "before");
        return Exp.asExponential(x -> this.apply(before.apply(x)));
    }

    /**
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    @Override
    public B apply(final A value) {
        Preconditions.throwIfNull(value, "value");
        return this.function.apply(value);
    }

    /**
     * @param other the other exponential to test
     * @return whether this exponential is equal to the other, using {@link Object#equals(Object)} on the underlying data
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public boolean equalsExp(final Exp<A, B> other) {
        Preconditions.throwIfNull(other, "other");
        return this.function.equals(other.function);
    }
    
    /**
     * Converts a function to an exponential.
     * 
     * @param <A> the function's input type
     * @param <B> the function's output type
     * @param function the function to convert
     * @return a new {@link Exp} object based on the provided function
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A, B> Exp<A, B> asExponential(final Function<A, B> function) {
        Preconditions.throwIfNull(function, "function");
        return new Exp<>(function);
    }

    /**
     * @param <A> the type to create an identity function for
     * @return the identity function for the given type
     */
    public static <A> Exp<A, A> identity() {
        return Exp.asExponential(a -> a);
    }

    /**
     * Builds a constant function using the given value.
     * 
     * @param <A> the exponential's input type
     * @param <B> the exponential's output type
     * @param value the constant value
     * @return a constant function that will produce the given value on evaluation
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A, B> Exp<A, B> constant(final B value) {
        Preconditions.throwIfNull(value, "value");
        return Exp.asExponential(a -> value);
    }

    /**
     * Static method to evaulate an exponential at a given value.
     * 
     * @param <A> the exponential's input type
     * @param <B> the exponential's output type
     * @param exp the exponential to evaluate
     * @param value the value to supply to the exponential
     * @return the result of the application
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public static <A, B> B eval(final Exp<A, B> exp, final A value) {
        Preconditions.throwIfNull(exp, "exp");
        Preconditions.throwIfNull(value, "value");
        return exp.apply(value);
    }

    /**
     * Transforms a curried function (a function resulting in an exponential) to a function 
     * from a product to the result.
     * 
     * @param <U> the result type
     * @param transpose the curried function
     * @return a function from a product to the result type
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static final <A, B, U> Function<Prod<A, B>, U> uncurry(final Function<A, Exp<B, U>> transpose) {
        Preconditions.throwIfNull(transpose, "transpose");
        return pair -> transpose.apply(pair.first()).apply(pair.second());
    }

    /**
     * Transforms a function from a product to the result type to a function resulting in an
     * exponential to the result type.
     * 
     * @param <U> the result type
     * @param transpose the product function to the result type
     * @return a function to the exponential
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static final <A, B, U> Function<A, Exp<B, U>> curry(final Function<Prod<A, B>, U> transpose) {
        Preconditions.throwIfNull(transpose, "transpose");
        return a -> Exp.asExponential(b -> transpose.apply(Prod.pair(a,b)));
    }


    /**
     * Contravariantly maps an exponential in the first type argument.
     * 
     * @param <A> the exponential's input type
     * @param <B> the exponential's output type
     * @param <X> the exponential's new input type
     * @param exp the exponential to transform
     * @param coFunction a function mapping the new input to the old input
     * @return a new function from the new input type to the old output type
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public static <A, B, X> Exp<X, B> comap(final Exp<A, B> exp, final Function<X, A> coFunction) {
        Preconditions.throwIfNull(exp, "exp");
        Preconditions.throwIfNull(coFunction, "coFunction");
        return exp.after(Exp.asExponential(coFunction));
    }

    /**
     * Covariantly maps an exponential in the second type argument.
     * 
     * @param <A> the exponential's input type
     * @param <B> the exponential's output type
     * @param <Y> the exponential's new output type
     * @param exp the exponential to transform
     * @param coFunction a function mapping the old output to the new output
     * @return a new function from the old input type to the new output type
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public static <A, B, Y> Exp<A, Y> map(final Exp<A, B> exp, final Function<B, Y> function) {
        Preconditions.throwIfNull(exp, "exp");
        Preconditions.throwIfNull(function, "function");
        return Exp.asExponential(function).after(exp);
    }
}
