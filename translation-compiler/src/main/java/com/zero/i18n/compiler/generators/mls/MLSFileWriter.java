package com.zero.i18n.compiler.generators.mls;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import com.zero.i18n.compiler.model.*;

public class MLSFileWriter {

    public <T> void write(File outputFile, Collection<SMLEntry<T>> records) throws IOException {

        ArrayList<SMLEntry<T>> sortedList = new ArrayList<>(records);
        // Collections.sort(sortedList, new Comparator<SMLEntry>() {
        //
        // @Override
        // public int compare(SMLEntry arg0, SMLEntry arg1) {
        // return arg0.getMessageId() < arg1.getMessageId() ? -1 : 1;
        // }
        // });

        MLSGenerator mlsGenerator = new MLSGenerator();
        try (FileOutputStream out = new FileOutputStream(outputFile)) {

            for (SMLEntry<T> record : sortedList) {
                String mlsEntries = mlsGenerator.format(record);

                String string = mlsEntries;
                out.write(string.getBytes(Charset.forName("ISO-8859-1")));
            }

        }
    }
}
