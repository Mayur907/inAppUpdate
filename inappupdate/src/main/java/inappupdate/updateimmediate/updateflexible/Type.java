package inappupdate.updateimmediate.updateflexible;

public enum Type {
    IMMEDIATE(0),
    FLEXIBLE(1);
    private final int code;

    Type(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
