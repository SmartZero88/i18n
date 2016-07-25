package com.zero.i18n.compiler.source;

import java.io.*;
import java.util.*;

import com.zero.i18n.compiler.compiler.*;
import com.zero.i18n.compiler.model.*;

public final class XMLRepository implements ITranslationRepository<String> {

    private final List<File> xmlFiles;

    public XMLRepository(List<File> propertyFiles) {
        this.xmlFiles = propertyFiles;
    }

    @Override
    public Collection<SMLEntry<String>> loadRecords() {
        return null;
    }

}
