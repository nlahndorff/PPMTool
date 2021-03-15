package org.plaidcat.PPMTool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectTaskNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8468400008285244473L;

	public ProjectTaskNotFoundException(String projectId) {
		super("Project task '" + projectId + "' does not exist.");
	}
}
