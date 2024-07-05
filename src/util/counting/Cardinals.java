package util.counting;

import util.data.algebraic.Sum;
import util.data.algebraic.Unit;

public final class Cardinals {

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

    public static class Zero extends CardinalBase implements Prev<One> {

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
    }

    public static class One extends CardinalBase implements Prev<Two>, Succ<Zero> {

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
    }

    public static class Two extends CardinalBase implements Prev<Three>, Succ<One> {

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
    }

    public static class Three extends CardinalBase implements Prev<Four>, Succ<Two> {

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
    }

    public static class Four extends CardinalBase implements Prev<Five>, Succ<Three> {

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
    }

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
