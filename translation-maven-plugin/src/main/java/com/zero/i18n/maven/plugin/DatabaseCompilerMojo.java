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

/**
 * Goal which calls the sml-compiler
 * usage: mvn -N com.uc4.maven:sml-compiler-plugin:compile -Dsml.db.url=jdbc:oracle:thin:...
 * -Dsml.db.user=... -Dsml.db.pass=... -Dsml.outputFile=target/ecc.mls
 */
@Mojo(
    name = "compileDatabase", defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
    requiresProject = false)
public class DatabaseCompilerMojo extends AbstractCompilerMojo<Long> {

    /**
     * Database URL (oracle thin syntax)
     */
    @Parameter(property = "sml.db.url", required = true)
    String dbUrl;

    /**
     * Username to use to connect to the database
     */
    @Parameter(property = "sml.db.user", required = true)
    String dbUser;

    /**
     * Password in clear text to use to connect to the database
     */
    @Parameter(property = "sml.db.pass", required = true)
    String dbPass;

    /**
     * Password in clear text to use to connect to the database
     */
    @Parameter(property = "sml.categories", required = false)
    List<String> categories;

    /**
     * persisted properties database of natural key mappings
     */
    @Parameter(
        defaultValue = "${project.basedir}/mapping/mls-mapping.properties",
        property = "sml.mapping.file", required = true)
    File mappingFile;

    public void execute() throws MojoExecutionException {
        try {
            getLog().info("Reading mapping to " + mappingFile);
            if (!mappingFile.exists()) {
                getLog().warn("Creating empty " + mappingFile);
                mappingFile.getParentFile().mkdirs();
                mappingFile.createNewFile();
            }

            // ensure java outdir is here
            javaOutdir.mkdirs();

            Properties properties = new Properties();
            properties.load(new FileInputStream(mappingFile));

            ITranslationRepository<Long> repository = new OracleMLSRepository(
                dbUrl, dbUser, dbPass, categories, properties);
            Collection<SMLEntry<Long>> records = repository.loadRecords();

            generate(records);

            getLog().info("Saving mapping to " + mappingFile);
            FileOutputStream output = new FileOutputStream(mappingFile);
            properties.store(output, "MLS Mapping. needs to be persisted");
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to write to mls file " + outputFile, e);
        }

    }

    @Override
    protected boolean supportsMLS() {
        return true;
    }
}
