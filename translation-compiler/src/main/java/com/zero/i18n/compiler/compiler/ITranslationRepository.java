package com.zero.i18n.compiler.compiler;

import java.util.*;

import com.zero.i18n.compiler.model.*;

public interface ITranslationRepository<ID> {

    Collection<SMLEntry<ID>> loadRecords();
}
