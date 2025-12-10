package com.renderdeployment.renderdemo.validator;

import lombok.Data;

import java.util.List;
@Data
public class ValidationResult {

    private boolean isValid =true;
    private List<String> errors;
    private Object object;
}
