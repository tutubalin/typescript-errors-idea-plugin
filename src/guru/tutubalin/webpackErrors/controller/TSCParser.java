package guru.tutubalin.webpackErrors.controller;

import guru.tutubalin.webpackErrors.model.ErrorGroup;
import guru.tutubalin.webpackErrors.model.ErrorInformation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniil Tutubalin on 07.04.2017.
 */
public class TSCParser implements ILogParser {

    private Pattern patternError = Pattern.compile("^([^(]*\\.ts)\\((\\d+)\\,(\\d+)\\):\\serror\\s(TS\\d+):\\s((?:(.|\\n\\s))*)", Pattern.MULTILINE);

    private static TSCParser ourInstance = new TSCParser();

    public static TSCParser getInstance() {
        return ourInstance;
    }

    private TSCParser() {
    }

    @Override
    public ErrorGroup parseFile(CharSequence fileContent) {

        Matcher errorMatcher = patternError.matcher(fileContent);

        ErrorGroup root = new ErrorGroup("All");

        while (errorMatcher.find()) {

            ErrorInformation error = new ErrorInformation() {{
                file = errorMatcher.group(1);
                line = safeParseInt(errorMatcher.group(2), -1);
                startIndex = safeParseInt(errorMatcher.group(3), -1);
                errorCode = errorMatcher.group(4);
                fullDescription = errorMatcher.group(5);
                shortDescription = fullDescription.split("\\n\\s*")[0];
            }};

            root.add(error);
        }

        return root;
    }

    private int safeParseInt(String string, int defaultValue) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
