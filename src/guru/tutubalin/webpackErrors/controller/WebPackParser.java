package guru.tutubalin.webpackErrors.controller;

import guru.tutubalin.webpackErrors.model.ErrorGroup;
import guru.tutubalin.webpackErrors.model.ErrorInformation;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniil Tutubalin on 07.04.2017.
 */
public class WebPackParser implements ILogParser {

    private static final Pattern patternSplitter = Pattern.compile("ERROR\\sin\\s(?:\\[at-loader]\\s)?", Pattern.MULTILINE);
    private static final Pattern patternError = Pattern.compile("([^:]+)(?::(\\d+):(\\d+))?\\s*\\n^\\s*([^:]+):\\s*(.*)",Pattern.MULTILINE);
    private static final Pattern patternStackTrace = Pattern.compile("\\s@\\s(?:\\S*\\s(?:(\\d+):(\\d+)-(\\d+))?)",Pattern.MULTILINE);

    private static WebPackParser ourInstance = new WebPackParser();

    public static WebPackParser getInstance() {
        return ourInstance;
    }

    private WebPackParser() {
    }

    public ErrorGroup parseFile(CharSequence fileContent) {
        ErrorGroup root = new ErrorGroup("All");

        patternSplitter.splitAsStream(fileContent)
                .skip(1)
                .map(this::parseError)
                .filter(Objects::nonNull)
                .forEach(root::add);

        return root;
    }

    private ErrorInformation parseError(String errorMessage) {

        Matcher errorMatcher = patternError.matcher(errorMessage);

        if (errorMatcher.lookingAt()) {
            ErrorInformation result = new ErrorInformation() {{
                file = fixPath(errorMatcher.group(1));
                line = safeParseInt(errorMatcher.group(2), -1);
                startIndex = safeParseInt(errorMatcher.group(3), -1);
                errorCode = errorMatcher.group(4);
                shortDescription = errorMatcher.group(5);
            }};

            Matcher stackTraceMatcher = patternStackTrace.matcher(errorMessage.substring(errorMatcher.end()));

            com.intellij.openapi.diagnostic.Logger.getInstance("My").debug(errorMessage.substring(errorMatcher.end()));

            if (stackTraceMatcher.find()) {
                result.line = safeParseInt(stackTraceMatcher.group(1), result.line);
                result.startIndex = safeParseInt(stackTraceMatcher.group(2), result.startIndex);
                result.endIndex = safeParseInt(stackTraceMatcher.group(3), result.endIndex);
            }

            return result;
        }

        return null;
    }

    private String fixPath(String path) {
        return path;
    }

    private int safeParseInt(String string, int defaultValue) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


}
