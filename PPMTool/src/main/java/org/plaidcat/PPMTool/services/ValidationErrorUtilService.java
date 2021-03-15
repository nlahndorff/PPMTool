package org.plaidcat.PPMTool.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidationErrorUtilService {

	public ResponseEntity<?> validationError(BindingResult result) {
        if(result.hasErrors()) {
        	Map<String,String> errors = new HashMap<String,String>();
        	
        	for (FieldError e: result.getFieldErrors()) {
        		errors.put(e.getField(), e.getDefaultMessage());
        	}
        	
            return new ResponseEntity<Map<String,String>>(errors, HttpStatus.BAD_REQUEST);
        }
        
        return null;
	}
}
