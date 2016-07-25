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

import com.zero.i18n.compiler.generators.java.*;
import com.zero.i18n.compiler.generators.mls.*;
import com.zero.i18n.compiler.model.*;

public abstract class AbstractCompilerMojo<T> extends AbstractMojo {
    /**
     * Location of the MLS-File that will be written to
     */
    @Parameter(
        defaultValue = "${project.build.directory}/ecc.mls", property = "sml.outputFile",
        required = true)
    File outputFile;

    /**
     * directory where to put the java files
     */
    @Parameter(
        defaultValue = "${project.build.directory}/generated-sources", property = "sml.java.outdir",
        required = true)
    File javaOutdir;

    @Parameter(property = "sml.java.implementationClassname", required = false)
    private String implementationClassname;

    @Parameter(property = "sml.java.interfaceClassname", required = false)
    private String interfaceClassname;

    @Parameter(property = "sml.java.packageName", required = false)
    private String packageName = "com.automic.translation.generated";

    @Parameter(property = "sml.java.classPrefix", required = false)
    private String classPrefix = "Translation";

    protected abstract boolean supportsMLS();

    protected void generate(Collection<SMLEntry<T>> records) throws MojoExecutionException {
        try {
            // ensure java outdir is here
            javaOutdir.mkdirs();

            if (outputFile != null && supportsMLS()) {
                getLog().info("Writing " + records.size() + " records to " + outputFile);
                new MLSFileWriter().write(outputFile, records);
            }

            if (javaOutdir != null) {
                getLog().info("Writing " + records.size() + " records to " + javaOutdir);
                if (interfaceClassname != null) {
                    new JavaSourceWriter(packageName, classPrefix).write(
                        javaOutdir, records, interfaceClassname, implementationClassname);
                }
                else {
                    new JavaSourceWriter(packageName, classPrefix).write(javaOutdir, records);
                }
            }

            if (javaOutdir == null && outputFile == null) {
                throw new IllegalArgumentException("specify outputFile or javaOutdir");
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Unable to write to mls file " + outputFile, e);
        }

    }
}
