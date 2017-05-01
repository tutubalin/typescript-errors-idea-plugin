package pro.tutubalin.ideaPlugins.typescriptErrors.model;

public enum ErrorGroupLevel {
    ROOT,
    ERROR_CODE,
    DESCRIPTION,
    FILE,
    LOCATION;

    private static final ErrorGroupLevel[] vals = values();

    public ErrorGroupLevel next() {
        return vals[ordinal() + 1];
    }
}
