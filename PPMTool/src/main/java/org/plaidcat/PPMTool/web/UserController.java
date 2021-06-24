/**
 * 
 */
package org.plaidcat.PPMTool.web;

import javax.validation.Valid;

import org.plaidcat.PPMTool.domain.User;
import org.plaidcat.PPMTool.payload.JWTLoginSuccessResponse;
import org.plaidcat.PPMTool.payload.LoginRequest;
import org.plaidcat.PPMTool.security.JwtTokenProvider;
import org.plaidcat.PPMTool.services.UserService;
import org.plaidcat.PPMTool.services.ValidationErrorUtilService;
import org.plaidcat.PPMTool.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.plaidcat.PPMTool.security.SecurityConstants.*;

/**
 * @author nlahn
 *
 */

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private ValidationErrorUtilService errorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authManager;
	
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		
		userValidator.validate(user, result);
		
		//we've validated the passwords match, so clear it out so we don't send it plain text in the response.
		user.setConfirmPassword("");
		ResponseEntity<?> errorMap = errorService.validationError(result);
		if (errorMap != null) {
				return errorMap;
		}
		
		var newUser = userService.saveUser(user);
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {

		ResponseEntity<?> errorMap = errorService.validationError(result);
		if (errorMap != null) {
				return errorMap;
		}
		
		var authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()
		));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);
		
		return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));				
		
	}

}
