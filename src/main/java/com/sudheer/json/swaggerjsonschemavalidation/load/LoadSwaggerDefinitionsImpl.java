package com.sudheer.json.swaggerjsonschemavalidation.load;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LoadSwaggerDefinitionsImpl implements LoadSwaggerDefinitions {

    public Map<String, Object> loadJson(final String fileName) throws IOException {
        return new Gson().fromJson(new FileReader(LoadSwaggerDefinitionsImpl.class.getResource(FSLASH.
                concat(fileName).concat(".json")).getFile()), HashMap.class);
    }

    public Map<String, Object> loadJsonForPath(final String filePath) throws IOException {
        return new Gson().fromJson(new FileReader(filePath), HashMap.class);
    }

    public Map<String, Object> loadDefinitions(final String swaggerJson) throws Exception {
        final Map<String, Object> swagger = loadJson(swaggerJson);
        final Map<String, Object> swaggerDefinitions = (Map<String, Object>) swagger.get(DEFINITIONS);
        final Map<String, Object> paths = (Map<String, Object>) swagger.get("paths");
        final Map<String, Object> definitions = new HashMap<>();
        load(paths, swaggerDefinitions, definitions);
        return definitions;
    }

    public Map<String, Object> loadDefinitions(final String swaggerFile, final String inputFile) throws Exception {
        final Map<String, Object> swaggerDefinitions = (Map<String, Object>) loadJson(swaggerFile).get(DEFINITIONS);
        final Map<String, Object> input = loadJson(inputFile);
        final Map<String, Object> definitions = new HashMap<>();
        load(input, swaggerDefinitions, definitions);
        return definitions;
    }

    public Map<String, Object> loadDefinitionsForPath(final String swaggerFilePath, final String inputFilePath) throws Exception {
        final Map<String, Object> swaggerDefinitions = (Map<String, Object>) loadJsonForPath(swaggerFilePath).get(DEFINITIONS);
        final Map<String, Object> input = loadJson(inputFilePath);
        final Map<String, Object> definitions = new HashMap<>();
        load(input, swaggerDefinitions, definitions);
        return definitions;
    }

    private void load(final Map<String, Object> inputJson, final Map<String, Object> definitionsJson, final Map<String, Object> input) throws Exception {
        if (inputJson != null && !inputJson.isEmpty()) {
            for (Map.Entry<String, Object> entry : inputJson.entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                if (value instanceof Map) {
                    mapCase(definitionsJson, input, key, (Map) value);
                    continue;
                } else if ($_REF.equalsIgnoreCase(key))
                    load((Map) definitionsJson.get(value.toString().split(DEF_PREFIX)[1]), definitionsJson, input);
                else if ((ALL_OF.equalsIgnoreCase(key) || ANY_OF.equalsIgnoreCase(key) || ONE_OF.equalsIgnoreCase(key))) {
                    if (value instanceof List)
                        listCase(definitionsJson, input, (List<Map>) value);
                } else if (value instanceof List && PARAMETERS.equalsIgnoreCase(key)) {
                    listCase(definitionsJson, input, (List<Map>) value);
                } else if (ENUM.equalsIgnoreCase(key) && value instanceof List)
                    input.put(key, value);
                else if (REQUIRED.equalsIgnoreCase(key) && value instanceof List)
                    input.put(key, value);
            }
        } else {
            throw new Exception("Please verify input and try again ..!");
        }
    }

    private void mapCase(Map<String, Object> definitionsJson, Map<String, Object> input, String key, Map<String, Object> value) throws Exception {
        final Map<String, Object> mapVal = value;
        if (key != null && !PROPERTIES.equals(key) && !TYPE.equals(key) && !ITEMS.equals(key)
                && !XML.equals(key) && !RESPONSES.equals(key)) {
            final Map<String, Object> child = new LinkedHashMap<>();
            if (mapVal.keySet().contains(TYPE)) {
                input.put(key, mapVal.get(TYPE));
                return;
            }
            input.put(key, child);
            load(mapVal, definitionsJson, child);
        } else if (!RESPONSES.equals(key))
            load(mapVal, definitionsJson, input);
    }

    private void listCase(Map<String, Object> definitionsJson, Map<String, Object> input, List<Map> value) throws Exception {
        for (Map<String, Object> list : value)
            for (Map.Entry<String, Object> arrayEntry : list.entrySet()) {
                final String key = arrayEntry.getKey();
                final Object val = arrayEntry.getValue();
                if (key.equalsIgnoreCase(PROPERTIES) || key.equalsIgnoreCase(SCHEMA))
                    load((Map) val, definitionsJson, input);
                else if (val.toString().contains(DEF_PREFIX))
                    if (key.equalsIgnoreCase(PROPERTIES))
                        load((Map) val, definitionsJson, input);
                    else
                        load((Map) definitionsJson.get(val.toString().split(DEF_PREFIX)[1]), definitionsJson, input);
                else if (key.equalsIgnoreCase(TYPE) && !ARRAY.equalsIgnoreCase(val.toString()) && !OBJECT.equalsIgnoreCase(val.toString()))
                    input.put(key, input.get(TYPE) != null ? input.get(TYPE).toString().concat(",").concat(val.toString()) : val);
                continue;
            }
    }
}
