package com.sudheer.json.swaggerjsonschemavalidation.validate;

import com.sudheer.json.swaggerjsonschemavalidation.constants.SwaggerToJsonComparisionConstants;

import java.util.Set;

public interface Validate extends SwaggerToJsonComparisionConstants {

    /**
     * validate() will use to validate input json against swagger definitions and provides
     * @param input
     * @return
     * @throws Exception
     */
    public Set<String> validate(final String input) throws Exception;
}
