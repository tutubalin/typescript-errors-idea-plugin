package guru.tutubalin.webpackErrors.model;

public enum ErrorGroupLevel {
    ROOT,
    ERROR_CODE,
    DESCRIPTION,
    FILE,
    LOCATION;

    private static ErrorGroupLevel[] vals = values();

    public ErrorGroupLevel next() {
        return vals[ordinal() + 1];
    }
}
