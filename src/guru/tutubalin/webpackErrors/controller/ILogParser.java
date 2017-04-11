package guru.tutubalin.webpackErrors.controller;

import guru.tutubalin.webpackErrors.model.ErrorGroup;

/**
 * Created by Daniil Tutubalin on 07.04.2017.
 */
public interface ILogParser {
    ErrorGroup parseFile(CharSequence fileContent);
}
