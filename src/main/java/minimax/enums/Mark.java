package minimax.enums;

public enum Mark {
    CROSS('X'), CIRCLE('O'), BLANK('_');

    private final char value;

    public char getValue() {
        return value;
    }

    private Mark(final char value) {
        this.value = value;
    }

}