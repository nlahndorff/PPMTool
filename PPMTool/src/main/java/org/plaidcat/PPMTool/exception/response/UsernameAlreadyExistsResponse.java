package org.plaidcat.PPMTool.exception.response;

//JSON response for user name already exists
public class UsernameAlreadyExistsResponse {
	
	public UsernameAlreadyExistsResponse(String username) {
		this.username = username;
	}
	
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
