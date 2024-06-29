package util.counting;

public final class Ordinals {

    public static class Zero extends OrdinalBase implements Prev<One> {

        public static final Zero INSTANCE = new Zero();

        private Zero() {
            super(0);
        }

        public One next() {
            return One.INSTANCE;
        }

        public Zero prev() {
            return Zero.INSTANCE;
        }
    }

    public static class One extends OrdinalBase implements Prev<Two> {

        public static final One INSTANCE = new One();

        private One() {
            super(1);
        }

        public Two next() {
            return Two.INSTANCE;
        }

        public One prev() {
            return One.INSTANCE;
        }
    }

    public static class Two extends OrdinalBase implements Prev<Three> {

        public static final Two INSTANCE = new Two();

        private Two() {
            super(2);
        }

        public Three next() {
            return Three.INSTANCE;
        }

        public One prev() {
            return One.INSTANCE;
        }
    }

    public static class Three extends OrdinalBase implements Prev<Four> {

        public static final Three INSTANCE = new Three();

        private Three() {
            super(3);
        }

        public Four next() {
            return Four.INSTANCE;
        }

        public Two prev() {
            return Two.INSTANCE;
        }
    }

    public static class Four extends OrdinalBase implements Prev<Five> {

        public static final Four INSTANCE = new Four();

        private Four() {
            super(4);
        }

        public Five next() {
            return Five.INSTANCE;
        }

        public Three prev() {
            return Three.INSTANCE;
        }
    }

    public static class Five extends OrdinalBase {
        
        public static final Five INSTANCE = new Five();

        private Five() {
            super(5);
        }

        public Five next() {
            return this;
        }

        public Four prev() {
            return Four.INSTANCE;
        }
    }

    private static abstract class OrdinalBase implements Ordinal {

        private final int VALUE;

        private OrdinalBase(final int value) {
            this.VALUE = value;
        }

        @Override
        public int getOrdinal() {
            return this.VALUE;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof OrdinalBase) {
                return this.VALUE == ((OrdinalBase) obj).VALUE;
            }
            return false;
        }
    }
}
