package com.github.mdcdi1315.mdex.util.weight;

import com.github.mdcdi1315.mdex.codecs.PrimitiveCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentOutOfRangeException;
import com.mojang.serialization.DynamicOps;

/**
 * Provides the randomized weighted logic as it was defined in Minecraft versions prior 1.21.5,
 * with a look on performance and proper serialization routines.
 */
public final class Weight
{
    /**
     * This field provides a proper serialization codec for {@link Weight} instances, with appropriate validation and behavior as specified by the in-code {@link Weight#Of} method.
     */
    public static final Codec<Weight> CODEC;
    public static final Weight ZERO , ONE;
    private final int weight;

    static {
        // Default and recommended codec for a Weight instance
        CODEC = new InternalCodec();
        // Renamed SINGLE to ZERO because it is more consistent.
        // Also added one more default object that is the weight of 1 (Also pretty much common).
        ZERO = new Weight(0);
        ONE = new Weight(1);
    }

    private static class InternalCodec
        extends PrimitiveCodec<Weight>
    {
        private static DataResult<Weight> ValidateAndReturn(Number n)
        {
            int decoded = n.intValue();
            if (decoded < 0) {
                String s = String.format("Weight must be more than or equal to zero.\nActual value: %d" , decoded);
                return DataResult.error(() -> s);
            } else if (decoded == 0) {
                return DataResult.success(Weight.ZERO);
            } else if (decoded == 1) {
                return DataResult.success(Weight.ONE);
            } else {
                return DataResult.success(new Weight(decoded));
            }
        }

        @Override
        @SuppressWarnings("all")
        protected <T> DataResult<Weight> Read(DynamicOps<T> ops, T input) {
            DataResult<Number> n = ops.getNumberValue(input);
            var e = n.error();
            return e.<DataResult<Weight>>
                    map(error -> DataResult.error(error::message))
                    .orElse(ValidateAndReturn(n.result().get())); // Either we will have an error or a result, not both
        }

        @Override
        protected <T> T Write(DynamicOps<T> ops, Weight value) {
            return ops.createNumeric(value.weight);
        }
    }

    /**
     * Creates a new {@link Weight} instance through code, of the specified weight. <br />
     * Weights are integer values that are positive or zero.
     * @param wt The value that the {@link Weight} instance will have.
     * @return The created weight value. May return the value of {@link Weight#ZERO} or {@link Weight#ONE} fields depending on which integer is passed as the argument.
     * @throws ArgumentOutOfRangeException {@code weight} is less than zero.
     */
    public static Weight Of(int wt)
        throws ArgumentOutOfRangeException
    {
        if (wt < 0) {
            throw new ArgumentOutOfRangeException("wt" , "Weight must be more than or equal to zero.");
        } else if (wt == 0) {
            return ZERO;
        } else if (wt == 1) {
            return ONE;
        } else {
            return new Weight(wt);
        }
    }

    // Enforce using the Of method above.
    private Weight(int weight) {
        this.weight = weight;
    }

    /**
     * Gets the actual value that this {@link Weight} instance holds.
     * @return The value held for this {@link Weight} instance.
     */
    public int getValue() {
        return weight;
    }
}
