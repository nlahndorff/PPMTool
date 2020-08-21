package org.plaidcat.PPMTool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectIdException extends RuntimeException {

	private static final long serialVersionUID = 291784809913275387L;

	public ProjectIdException(String message) {
		super(message);
	}
}
