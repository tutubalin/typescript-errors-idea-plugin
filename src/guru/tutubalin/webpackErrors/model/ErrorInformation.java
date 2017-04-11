package guru.tutubalin.webpackErrors.model;

public class ErrorInformation {

    public String errorCode;
    public String shortDescription;
    public String fullDescription;
    public String file;
    public int line;
    public int startIndex;
    public int endIndex = -1;

    public String getLocation() {
        return line + ":" + startIndex + (endIndex>=0 ? "-"+endIndex : "");
    }
}
