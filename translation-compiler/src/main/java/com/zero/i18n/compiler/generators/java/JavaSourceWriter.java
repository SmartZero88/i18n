package com.zero.i18n.compiler.generators.java;

import java.io.*;
import java.util.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.*;

import com.google.common.collect.*;
import com.sun.codemodel.*;
import com.zero.i18n.api.*;
import com.zero.i18n.api.impl.*;
import com.zero.i18n.compiler.model.*;

public class JavaSourceWriter {

    private static final String INDENT = "\r\n\t\t\t";
    private static final Class<?> DEFAULT_CLASS = Object.class;
    private String packageName;
    private String classPrefix;

    public JavaSourceWriter(String packageName, String classPrefix) {
        this.packageName = packageName;
        this.classPrefix = classPrefix;

    }

    public <T> void write(File targetDirectory, Collection<SMLEntry<T>> records)
        throws IOException {
        write(targetDirectory, records, IText.class.getName(), ITextImpl.class.getName());
    }

    public <T> void write(
        File targetDirectory, Collection<SMLEntry<T>> records, String interfaceClassname,
        String implementationClassname) throws IOException {

        JCodeModel model = new JCodeModel();

        JClass defaultImplementation = model.ref(implementationClassname);
        JClass defaultInterface = model.ref(interfaceClassname);

        Map<String, JDefinedClass> categories = buildClasses(
            model, defaultInterface, defaultImplementation, records);
        model.build(targetDirectory);

        System.out.println(
            "Generated classes for categories: \r\n"
                + StringUtils.join(categories.keySet(), "\r\n"));

    }

    private <T> Map<String, JDefinedClass> buildClasses(JCodeModel model, JClass defaultInterface,
        JClass defaultImplementation, Collection<SMLEntry<T>> records) {
        Map<String, JDefinedClass> classes = Maps.newHashMap();
        for (SMLEntry<?> entry : records) {
            String category = entry.getCategory();

            JDefinedClass _class = ensureClass(model, classes, category);
            JDefinedClass _dataClass = ensureDataClass(model, classes, category,
                Math.abs(entry.getNaturalKey().hashCode()) % 10);

            // buildLocaleProvider(model, _class);

            buildFieldData(
                model, defaultInterface, defaultImplementation, _class, _dataClass, entry);
            // buildFieldData(model, _dataClass, entry);

        }
        return classes;
    }

    private static void buildFieldData(
        JCodeModel model, JClass defaultInterface, JClass defaultImplementation,
        JDefinedClass _class, JDefinedClass _dataClass, SMLEntry<?> entry) {

        if (entry.getMessageId() == null) {
            return;
        }

        int variables = 0;

        JInvocation create = model.ref(SMLBuilder.class).staticInvoke("create");
        // Only add constructore if non default
        if (!defaultImplementation.fullName().equals(ITextImpl.class.getName())) {
            create = create.arg(defaultInterface.dotclass()).arg(defaultImplementation.dotclass());
        }

        for (Locale language : entry.getTranslations().keySet()) {
            JExpression localeRef = findLocaleRef(model, language);

            String message = entry.getTranslations().get(language);
            variables = Math.max(variables, paramCount(message));
            create = create.invoke(INDENT + "withMessage").arg(localeRef).arg(message);
        }

        String buildFunctionName = "build" + (variables <= 3 ? variables : "N");
        JInvocation expr = create.invoke(INDENT + buildFunctionName);
        for (int i = 0; i < variables && i < 3; i++) {
            expr = expr.arg(model.ref(DEFAULT_CLASS).staticRef("class"));
        }

        JClass interf;

        switch (variables) {
            case 0:
                interf = defaultInterface;
                break;
            case 1:
                interf = model.ref(ITextPattern1.class).narrow(defaultInterface).narrow(
                    DEFAULT_CLASS);
                break;
            case 2:
                interf = model.ref(ITextPattern2.class).narrow(defaultInterface).narrow(
                    DEFAULT_CLASS, DEFAULT_CLASS);
                break;
            case 3:
                interf = model.ref(ITextPattern3.class).narrow(defaultInterface).narrow(
                    DEFAULT_CLASS, DEFAULT_CLASS,
                    DEFAULT_CLASS);
                break;
            default:
                interf = model.ref(ITextPatternN.class).narrow(defaultInterface).narrow(
                    DEFAULT_CLASS, DEFAULT_CLASS,
                    DEFAULT_CLASS);
                break;
        }

        JFieldVar datafield = _dataClass.field(
            JMod.PUBLIC | JMod.STATIC, interf, generateConstantName(entry), expr);
        buildJavadoc(datafield, entry);

        _class._implements(_dataClass);
        // Bulild reference
        // JFieldRef refDefinition =
        // _dataClass.staticRef(generateConstantName(entry));
        // JFieldVar field = _class.field(JMod.PUBLIC | JMod.STATIC, interf,
        // generateConstantName(entry), refDefinition);

    }

    private static void buildJavadoc(JFieldVar field, SMLEntry<?> entry) {
        for (Locale language : entry.getTranslations().keySet()) {
            String message = entry.getTranslations().get(language);
            field.javadoc().addParam(language.toString()).add(
                StringEscapeUtils.escapeHtml4(message));
        }
    }

    private static JExpression findLocaleRef(JCodeModel model, Locale language) {
        try {
            return model._ref(Locale.class).boxify().staticRef(
                MLSLocales.getConstantName(language));
        } catch (IllegalArgumentException e) {
            return model._ref(Locale.class).boxify().staticInvoke("forLanguageTag").arg(
                language.getLanguage());
        }
    }

    private static String generateConstantName(SMLEntry<?> entry) {
        String id = entry.getNaturalKey();
        String withoutSpecials = id
            .replaceAll("[^a-zA-Z0-9]", "_").replaceAll("__", "_").replaceAll("_$", "");
        return withoutSpecials;
    }

    private static int paramCount(String string) {

        for (int i = 99; i >= 0; i--) {
            String replace = "{" + i + "}";
            if (StringUtils.contains(string, replace)) {
                return i + 1;
            }
        }
        return 0;
    }

    private JDefinedClass ensureClass(
        JCodeModel model, Map<String, JDefinedClass> classes, String category) {

        JPackage pkg = model._package(this.packageName);
        JDefinedClass _class = classes.get(category);
        if (_class == null) {
            String className = generateClassName(category);
            try {
                _class = pkg._class(className);

                classes.put(category, _class);
            } catch (JClassAlreadyExistsException e) {
                e.printStackTrace();
            }
        }

        return _class;
    }

    private JDefinedClass ensureDataClass(
        JCodeModel model, Map<String, JDefinedClass> classes, String category,
        int split) {
        JPackage pkg = model._package(this.packageName + ".data");

        String className = "__Data" + generateClassName(category) + (split);
        JDefinedClass _class = classes.get(className);

        if (_class == null) {
            try {
                _class = pkg._interface(className);

                classes.put(className, _class);
            } catch (JClassAlreadyExistsException e) {
                e.printStackTrace();
            }
        }

        return _class;
    }

    private String generateClassName(String category) {
        String withoutSpecials = category.replaceAll("[^a-zA-Z0-9]", " ");
        return this.classPrefix + WordUtils.capitalizeFully(withoutSpecials).replaceAll(" ", "");
    }

}
