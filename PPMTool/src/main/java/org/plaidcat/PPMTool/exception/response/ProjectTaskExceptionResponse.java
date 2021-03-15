package org.plaidcat.PPMTool.exception.response;

public class ProjectTaskExceptionResponse  {
	private String taskIdentifier;
	
	public ProjectTaskExceptionResponse(String taskIdentifier) {
		this.taskIdentifier = taskIdentifier;
	}

	public String getProjectIdentifier() {
		return taskIdentifier;
	}

	public void setProjectIdentifier(String taskIdentifier) {
		this.taskIdentifier = taskIdentifier;
	}
}
