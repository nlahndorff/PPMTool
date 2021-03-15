package org.plaidcat.PPMTool.exception.response;

public class ProjectIdExceptionResponse  {
	private String projectIdentifier;
	
	public ProjectIdExceptionResponse(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}
}
