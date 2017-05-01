package pro.tutubalin.ideaPlugins.typescriptErrors.controller;

import pro.tutubalin.ideaPlugins.typescriptErrors.model.ErrorGroup;

/**
 * Created by Daniil Tutubalin on 07.04.2017.
 */
public interface ILogParser {
    ErrorGroup parseFile(CharSequence fileContent);
}
