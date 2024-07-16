package util.counting;

import util.data.algebraic.Sum;
import util.data.algebraic.Unit;

/**
 * Helper class that contains all of the cardinals (0, 1, 2, 3, 4, 5).
 */
public final class Cardinals {

    /**
     * Either creates the corresponding cardinal from the given integer or fails and returns a
     * {@link Unit#unit()}.
     * 
     * @param size the cardinal as an integer
     * @return either the corresponding {@link Cardinal} or {@link Unit#unit()}
     */
    public static Sum<Unit, Cardinal> getCardinal(final int size) {
        switch (size) {
            case 0:
                return Sum.right(Zero.INSTANCE);
            case 1:
                return Sum.right(One.INSTANCE);
            case 2:
                return Sum.right(Two.INSTANCE);
            case 3:
                return Sum.right(Three.INSTANCE);
            case 4:
                return Sum.right(Four.INSTANCE);
            case 5:
                return Sum.right(Five.INSTANCE);
            default:
                return Sum.left(Unit.unit());
        }
    }

    /**
     * The 0th cardinal for sets of size 0.
     */
    public static class Zero extends CardinalBase implements Pred<One> {

        public static final Zero INSTANCE = new Zero();

        private Zero() {
            super(0);
        }

        @Override
        public One next() {
            return One.INSTANCE;
        }

        @Override
        public Zero prev() {
            return Zero.INSTANCE;
        }

        @Override
        public Cardinal instance() {
            return INSTANCE;
        }
    }

    /**
     * The 1st cardinal for sets of size 1.
     */
    public static class One extends CardinalBase implements Pred<Two>, Succ<Zero> {

        public static final One INSTANCE = new One();

        private One() {
            super(1);
        }

        @Override
        public Two next() {
            return Two.INSTANCE;
        }

        @Override
        public Zero prev() {
            return Zero.INSTANCE;
        }

        @Override
        public Cardinal instance() {
            return INSTANCE;
        }
    }

    /**
     * The 2nd cardinal for sets of size 2.
     */
    public static class Two extends CardinalBase implements Pred<Three>, Succ<One> {

        public static final Two INSTANCE = new Two();

        private Two() {
            super(2);
        }

        @Override
        public Three next() {
            return Three.INSTANCE;
        }

        @Override
        public One prev() {
            return One.INSTANCE;
        }

        @Override
        public Cardinal instance() {
            return INSTANCE;
        }
    }

    /**
     * The 3rd cardinal for sets of size 3.
     */
    public static class Three extends CardinalBase implements Pred<Four>, Succ<Two> {

        public static final Three INSTANCE = new Three();

        private Three() {
            super(3);
        }

        @Override
        public Four next() {
            return Four.INSTANCE;
        }

        @Override
        public Two prev() {
            return Two.INSTANCE;
        }

        @Override
        public Cardinal instance() {
            return INSTANCE;
        }
    }

    /**
     * The 4th cardinal for sets of size 4.
     */
    public static class Four extends CardinalBase implements Pred<Five>, Succ<Three> {

        public static final Four INSTANCE = new Four();

        private Four() {
            super(4);
        }

        @Override
        public Five next() {
            return Five.INSTANCE;
        }

        @Override
        public Three prev() {
            return Three.INSTANCE;
        }

        @Override
        public Cardinal instance() {
            return INSTANCE;
        }
    }

    /**
     * The 5th cardinal for sets of size 5.
     */
    public static class Five extends CardinalBase implements Succ<Four> {
        
        public static final Five INSTANCE = new Five();

        private Five() {
            super(5);
        }

        @Override
        public Five next() {
            return this;
        }

        @Override
        public Four prev() {
            return Four.INSTANCE;
        }

        @Override
        public Cardinal instance() {
            return INSTANCE;
        }
    }

    private static abstract class CardinalBase implements Cardinal {

        private final int VALUE;

        private CardinalBase(final int value) {
            this.VALUE = value;
        }

        @Override
        public int getInteger() {
            return this.VALUE;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof CardinalBase) {
                return this.VALUE == ((CardinalBase) obj).VALUE;
            }
            return false;
        }
    }
}
