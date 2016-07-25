package com.zero.i18n.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.*;
import java.util.*;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;

import com.zero.i18n.compiler.compiler.*;
import com.zero.i18n.compiler.model.*;
import com.zero.i18n.compiler.source.*;

@Mojo(
    name = "compileXML", defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
    requiresProject = false)
public class XMLCompilerMojo extends AbstractCompilerMojo<String> {
    /**
     * Password in clear text to use to connect to the database
     */
    @Parameter(property = "sml.properties", required = true)
    List<File> propertyFiles;

    @Parameter(property = "sml.failOnFileNotFound", required = false)
    boolean failOnFileNotFound = true;

    public void execute() throws MojoExecutionException {
        try {
            if (!failOnFileNotFound && !propertyFiles.get(0).exists()) {
                System.out.println(
                    "File " + propertyFiles.get(0) + " not found, skipping this plugin");
                return; //
            }
            ITranslationRepository<String> repository = new XMLRepository(propertyFiles);
            Collection<SMLEntry<String>> records = repository.loadRecords();

            super.generate(records);
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to write to mls file " + outputFile, e);
        }

    }

    @Override
    protected boolean supportsMLS() {
        return false;
    }
}
