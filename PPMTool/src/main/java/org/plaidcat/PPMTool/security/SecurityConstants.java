/**
 * 
 */
package org.plaidcat.PPMTool.security;

/**
 * @author nlahn
 *
 */
public class SecurityConstants {
	
	private SecurityConstants() {}

	public static final String SIGNUP_URLS = "/api/users/**";
    public static final String H2_URL = "h2-console/**";
    public static final String SECRET ="SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX= "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 300000;
    protected static final String[] STATIC_CONTENT = {"/",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"};
    
    
}
