package com.sudheer.json.swaggerjsonschemavalidation.validate;

import com.sudheer.json.swaggerjsonschemavalidation.load.LoadSwaggerDefinitionsImpl;

import java.util.*;

public class ValidatorImpl implements Validate {

    public Set<String> validate(final String inputJson) throws Exception {
        final Set<String> errors = new LinkedHashSet<>();
        if (inputJson != null) {
            final Map<String, Object> definitions = new LoadSwaggerDefinitionsImpl().loadDefinitions("swagger-pets");
            final Map<String, Object> input = new LoadSwaggerDefinitionsImpl().loadJson(inputJson);
            validate(input, definitions, errors);
        } else
            errors.add("Please provide input !!!");
        return errors;
    }

    private void validate(final Map<String, Object> input, final Map<String, Object> definitions, final Set<String> error) throws Exception {
        if (input != null && !input.isEmpty()) {
            final Object required = definitions.get(REQUIRED);
            if (required != null)
                validateMandatory(input, (ArrayList) required, error);
            for (Map.Entry<String, Object> entry : input.entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                if (value instanceof Map) {
                    final Object def = definitions.get(key);
                    if (def != null)
                        validate((Map<String, Object>) value, (Map<String, Object>) def, error);
                    else
                        error.add(key.concat(" : Not a valid container."));
                    continue;
                } else if (value instanceof String) {
                    final Object def = definitions.get(key);
                    if (def != null) {
                        if (def instanceof Map && ((LinkedHashMap) def).keySet().contains(ENUM)) {
                            validateEnum((String) value, (ArrayList) ((LinkedHashMap) def).get(ENUM), error);
                            continue;
                        } else if (def instanceof Map) {
                            final Object type = ((LinkedHashMap) def).get(TYPE);
                            if (type != null && INTEGER_STRING.equals(type) && validateIntOrString(type))
                                continue;
                            else
                                error.add(key.concat(" : Invalid value present, accepts only java.lang.String/java.lang.Integer"));
                            continue;
                        } else if (validateString((String) def, value))
                            continue;
                        else
                            error.add(key.concat(" : Invalid value, accepts only java.lang.String"));
                        continue;
                    } else if (validateIntOrString(value))
                        continue;
                    else
                        error.add(key.concat(" : Not a valid field."));
                    continue;
                } else if (value instanceof List) {
                    final Object def = definitions.get(key);
                    if (def != null && def instanceof Map && ((LinkedHashMap) def).keySet().contains(TYPE) &&
                            validateArray((String) ((LinkedHashMap) def).get(TYPE), value))
                        for (Object obj : (ArrayList) value)
                            validate((Map<String, Object>) obj, (Map<String, Object>) def, error);
                    else if (def != null && def instanceof String) {
                        validateArray((String) def, value);
                    } else
                        error.add(key.concat(" : Invalid type, accepts only java.util.List"));
                    continue;
                } else if (value instanceof Integer) {
                    if (!validateInt((String) definitions.get(key), value))
                        error.add(key.concat(" : Invalid value, accepts only java.lang.Integer"));
                    continue;
                } else if (value instanceof Long) {
                    if (!validateLong((String) definitions.get(key), value))
                        error.add(key.concat(" : Invalid value, accepts only java.lang.Long"));
                    continue;
                } else if (value instanceof Double) {
                    if (!validateDouble((String) definitions.get(key), value))
                        error.add(key.concat(" : Invalid value, accepts only java.lang.Double"));
                    continue;
                } else
                    throw new Exception("None of the conditions matched for : " + key);
            }
        } else {
            throw new Exception("Please verify input and try again ..!");
        }
    }

    private boolean validateIntOrString(final Object input) {
        return input instanceof String || input instanceof Integer ? true : false;
    }

    private boolean validateString(final String type, final Object input) {
        return STRING.equalsIgnoreCase(type) && input instanceof String ? true : false;
    }

    private boolean validateInt(final String type, final Object input) {
        return INTEGER.equalsIgnoreCase(type) && input instanceof Integer ? true : false;
    }

    private boolean validateDouble(final String type, final Object input) {
        return (DOUBLE.equalsIgnoreCase(type) || INTEGER.equalsIgnoreCase(type)) && input instanceof Double ? true : false;
    }

    private boolean validateLong(final String type, final Object input) {
        return LONG.equalsIgnoreCase(type) && input instanceof Long ? true : false;
    }

    private boolean validateArray(final String type, final Object input) {
        return ARRAY.equalsIgnoreCase(type) && input instanceof List ? true : false;
    }

    private void validateMandatory(final Map<String, Object> input, final ArrayList required, final Set<String> error) {
        for (Object field : required) {
            if (input.get((String) field) == null)
                error.add("Missing mandatory field : ".concat((String) field));
        }
    }

    private void validateEnum(final String input, final ArrayList enums, final Set<String> error) {
        if (!enums.contains(input))
            error.add(input.concat(" --  Not a valid enum, Accepted : ").concat(String.valueOf(enums)));

    }
}
