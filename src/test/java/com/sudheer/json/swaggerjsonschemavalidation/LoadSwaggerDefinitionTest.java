package com.sudheer.json.swaggerjsonschemavalidation;

import com.sudheer.json.swaggerjsonschemavalidation.load.LoadSwaggerDefinitions;
import com.sudheer.json.swaggerjsonschemavalidation.load.LoadSwaggerDefinitionsImpl;
import com.sudheer.json.swaggerjsonschemavalidation.validate.Validate;
import com.sudheer.json.swaggerjsonschemavalidation.validate.ValidatorImpl;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class LoadSwaggerDefinitionTest {

    @Test
    public void load() {
        try {
            final LoadSwaggerDefinitions load = new LoadSwaggerDefinitionsImpl();
            final Map<String, Object> definitions = load.loadDefinitions("swagger-pets");
            final Validate validate = new ValidatorImpl();
            final Set<String> errors = validate.validate("input");
            System.out.println(errors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
