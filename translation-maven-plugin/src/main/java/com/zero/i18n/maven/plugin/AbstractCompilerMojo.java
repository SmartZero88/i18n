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
     * directory where to put the java files
     */
    @Parameter(
        property = "sml.java.outdir", defaultValue = "${project.build.directory}/generated-sources",
        required = true)
    protected File javaOutdir;

    @Parameter(property = "sml.java.implementationClassname", required = false)
    private String implementationClassname;

    @Parameter(property = "sml.java.interfaceClassname", required = false)
    private String interfaceClassname;

    @Parameter(
        property = "sml.java.packageName", defaultValue = "com.zero.translation.generated",
        required = false)
    private String packageName;

    @Parameter(property = "sml.java.classPrefix", defaultValue = "", required = false)
    private String classPrefix;

    protected abstract boolean supportsMLS();

    protected final void generate(Collection<SMLEntry<T>> records) throws MojoExecutionException {
        try {
            // ensure java outdir is here
            if (this.javaOutdir == null) {
                throw new IllegalArgumentException("specify javaOutdir");
            }
            this.javaOutdir.mkdirs();

            if (this.supportsMLS()) {
                this.getLog().info("Writing " + records.size() + " records to " + this.javaOutdir);
                new MLSFileWriter().write(this.javaOutdir, records);
            }

            this.getLog().info("Writing " + records.size() + " records to " + this.javaOutdir);
            JavaSourceWriter writer = new JavaSourceWriter(this.packageName, this.classPrefix);
            if (this.interfaceClassname != null) {
                writer.write(
                    this.javaOutdir, records, this.interfaceClassname,
                    this.implementationClassname);
            } else {
                writer.write(this.javaOutdir, records);
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Unable to write to mls file " + this.javaOutdir, e);
        }

    }
}
