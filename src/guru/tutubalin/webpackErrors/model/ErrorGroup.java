package guru.tutubalin.webpackErrors.model;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ErrorGroup implements Comparable<ErrorGroup> {

    private String title;
    private int count;

    private List<ErrorGroup> children = new LinkedList<ErrorGroup>() {};

    private Map<String, ErrorGroup> hash = new HashMap<>();

    private ErrorInformation errorInfo;

    private ErrorGroupLevel level;

    private boolean sorted = false;
    private ErrorGroup parent = null;

    public ErrorGroup(String title) {
        this(title, ErrorGroupLevel.ROOT);
    }

    public ErrorGroup(String title, ErrorGroupLevel level) {
        this.title = title;
        this.level = level;
    }

    public synchronized void add(ErrorInformation error) {

        count++;
        if (count == 1) {
            this.errorInfo = error;
        }
        sorted = false;

        if (level != ErrorGroupLevel.LOCATION) {

            ErrorGroupLevel sublevel = level.next();

            String key;

            switch (sublevel) {
                default:
                case ERROR_CODE:
                    key = error.errorCode;
                    break;
                case DESCRIPTION:
                    key = error.shortDescription;
                    break;
                case FILE:
                    key = error.file;
                    break;
                case LOCATION:
                    key = error.getLocation();
                    break;
            }

            ErrorGroup subgroup = hash.get(key);

            if (subgroup == null) {
                subgroup = new ErrorGroup(key, sublevel);
                subgroup.parent = this;
                hash.put(key, subgroup);
                children.add(subgroup);
            }

            subgroup.add(error);
        }
    }

    private String getCommonErrorDescription(String errorCode) {
        return errorCode;
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return count;
    }

    public boolean isLeaf() {
        return count == 1;
    }

    public ErrorInformation getErrorInfo() {
        return errorInfo;
    }

    public List<ErrorGroup> getChildren() {
        if (!sorted) {
            Collections.sort(children);
        }
        return children;
    }

    @Override
    public String toString() {
        return title + " ("+count+")";
    }

    @Override
    public int compareTo(@NotNull ErrorGroup anotherGroup) {
        return anotherGroup.count - this.count;
    }

    public ErrorGroup getParent() {
        return parent;
    }
}

