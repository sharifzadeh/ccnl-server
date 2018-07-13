package edu.dartmouth.ccnl.ridmp.com;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class ConstantUtils {
    public static Collection getConstants(Class source, Class itemType, String prefix) {
        Collection result = new ArrayList();
        Field[] fields = source.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            int modifiers = field.getModifiers();

            if (Modifier.isStatic(modifiers) &&
                Modifier.isFinal(modifiers) &&
                field.getName().startsWith(prefix) &&
                itemType.isAssignableFrom(field.getType())) {
                try {
                    result.add(field.get(null));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return Collections.unmodifiableCollection(result);
    }

    public static Map getConstantsMap(Class domain, Class range, String domainPrefix, String rangeSuffix) {
        Map result = new HashMap();

        try {
            Field[] domainFields = domain.getFields();
            for (int i = 0; i < domainFields.length; i++) {
                Field domainField = domainFields[i];
                String domainFieldName = domainField.getName();
                int domainModifiers = domainField.getModifiers();

                if (Modifier.isStatic(domainModifiers) &&
                    Modifier.isFinal(domainModifiers) &&
                    domainFieldName.startsWith(domainPrefix)) {
                    //noinspection EmptyCatchBlock
                    try {
                        Field rangeField = range.getField(domainFieldName + rangeSuffix);
                        int rangeModifiers = rangeField.getModifiers();

                        if (Modifier.isStatic(rangeModifiers) &&
                            Modifier.isFinal(rangeModifiers)) {
                            result.put(domainField.get(null), rangeField.get(null));
                        }
                    } catch (NoSuchFieldException e) {
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return Collections.unmodifiableMap(result);
    }
}
