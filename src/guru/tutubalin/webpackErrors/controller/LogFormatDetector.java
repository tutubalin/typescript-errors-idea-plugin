package guru.tutubalin.webpackErrors.controller;

/**
 * Created by Daniil Tutubalin on 07.04.2017.
 */
public class LogFormatDetector {

    public static ILogParser getParser(CharSequence fileContent) {
        return WebPackParser.getInstance();
    }
}
