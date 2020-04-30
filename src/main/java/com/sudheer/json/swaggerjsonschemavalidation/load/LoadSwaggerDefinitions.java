package com.sudheer.json.swaggerjsonschemavalidation.load;

import com.sudheer.json.swaggerjsonschemavalidation.constants.SwaggerToJsonComparisionConstants;

import java.io.IOException;
import java.util.Map;

public interface LoadSwaggerDefinitions extends SwaggerToJsonComparisionConstants {


    /**
     * loadSwaggerDefinitionsForPath() wil be used to load definitions file from external file system.
     *
     * @param filePath Accepts java.lang.String contains file path of definitions.
     * @return java.util.Map holds the definitions json into Map
     * @throws IOException
     */
    public Map<String, Object> loadJsonForPath(final String filePath) throws IOException;


    /**
     * loadJson() wil be used to load json file from resources folder.
     *
     * @param fileName Accepts java.lang.String contains file name of json.
     * @return java.util.Map holds the definitions json into Map
     * @throws IOException
     */
    public Map<String, Object> loadJson(final String fileName) throws IOException;

    /**
     * loadDefinitions in map for given swagger json and input definition json.
     *
     * @param swaggerFile swagger definitions
     * @param inputFile   input definition
     * @return definitions map.
     * @throws Exception
     */
    public Map<String, Object> loadDefinitions(final String swaggerFile, final String inputFile) throws Exception;

    /**
     * loadDefinitions in map for given swagger file json and input definition file json.
     *
     * @param swaggerFilePath swagger definitions
     * @param inputFilePath   input definition
     * @return definitions map.
     * @throws Exception
     */
    public Map<String, Object> loadDefinitionsForPath(final String swaggerFilePath, final String inputFilePath) throws Exception;

    /**
     * load definitions in map for given swagger json and paths.
     *
     * @param swaggerName swagger definitions
     * @return definitions map.
     * @throws Exception
     */
    public Map<String, Object> loadDefinitions(final String swaggerName) throws Exception;
}
