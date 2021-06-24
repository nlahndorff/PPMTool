/**
 * 
 */
package org.plaidcat.PPMTool;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.groups.Default;

import org.springframework.validation.Errors;

/**
 * @author nlahn
 *
 */
public class ValidationUtil {

	private ValidationUtil() {
		//Everything is static, no need to instantiate this class.
	}
	
	/**
	 * Test validity of an object against some number of validation groups, or
	 * Default if no groups are specified.
	 * 
	 * @param result Errors object for holding validation errors for use in
	 *            Spring form taglib. Any violations encountered will be added
	 *            to this errors object.
	 * @param o Object to be validated
	 * @param classes Validation groups to be used in validation
	 * @return true if the object is valid, false otherwise.
	 */
	public static boolean isValid( Errors result, Object o, Class<?>... classes )
	{
	    if ( classes == null || classes.length == 0 || classes[0] == null )
	    {
	        classes = new Class<?>[] { Default.class };
	    }
	    var validator = Validation.buildDefaultValidatorFactory().getValidator();
	    Set<ConstraintViolation<Object>> violations = validator.validate( o, classes );
	    for ( ConstraintViolation<Object> v : violations )
	    {
	        var path = v.getPropertyPath();
	        var propertyName = new StringBuilder();
	        if ( path != null )
	        {
	            for ( Node n : path )
	            {
	                propertyName.append(n.getName()).append(".");
	            }
	            propertyName = propertyName.deleteCharAt(propertyName.length()); 	            		
	        }
	        var constraintName = v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
	        if ( propertyName == null || "".equals(  propertyName.toString()  )) {
	            result.reject( constraintName, v.getMessage());
	        } else {
	            result.rejectValue( propertyName.toString(), constraintName, v.getMessage() );
	        }
	    }
	    return violations.isEmpty();
	}
}
