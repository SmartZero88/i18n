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
    name = "compileProperties", defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
    requiresProject = false)
public class PropertyCompilerMojo extends AbstractCompilerMojo<String> {

    @Parameter(property = "sml.directory", required = false)
    private File directory;

    @Parameter(property = "sml.properties", required = true)
    private final List<File> propertyFiles = new ArrayList<>();

    @Parameter(property = "sml.failOnFileNotFound", required = false)
    private boolean failOnFileNotFound;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            File[] files = this.directory.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".properties");
                }
            });
            Set<File> props = new HashSet<>();
            props.addAll(this.propertyFiles);
            props.addAll(Arrays.asList(files));
            if (!this.failOnFileNotFound && !this.propertyFiles.get(0).exists()) {
                this.getLog().info(
                    "File " + this.propertyFiles.get(0) + " not found, skipping this plugin");
                return;
            }
            ITranslationRepository<String> repository = new PropsRepository(props);
            Collection<SMLEntry<String>> records = repository.loadRecords();

            super.generate(records);
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to write to mls file " + this.javaOutdir, e);
        }

    }

    @Override
    protected boolean supportsMLS() {
        return false;
    }
}
