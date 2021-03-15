package org.plaidcat.PPMTool.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements UserDetails {
	
	private static final long serialVersionUID = -9177338660474378899L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Email(message = "User name should be an email address")
	@NotBlank(message = "User field is required")
	@Column(unique=true)
	private String username;
	
	@NotBlank(message = "Please enter your full name")
	private String fullName;
	
	@NotBlank(message= "Password field is required")
	private String password;
	
	@Transient
	private String confirmPassword;
	
	
	private Date createdAt;
	private Date updatedAt;
	
	//One to many w/Project
	@OneToMany(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER, mappedBy = "user", orphanRemoval=true)
	List<Project> projects = new ArrayList<>();
	
	public User() {
		//Added just shiggles.
	}
	
	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


	/*
	 * User Detail Interface methods
	 */
	
	//For role based authorizations.  Leaving null b/c no roles defined
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return null;
	}

	//For users that haven't logged in or haven't paid their bills.  
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	//Add any account locking for invalid passwords here.
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	//Password expiration logic?
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//Add logic here to enable after enail auth etc.
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
	
}
