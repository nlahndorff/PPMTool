package org.plaidcat.PPMTool.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.plaidcat.PPMTool.SecondaryValidation;
import org.plaidcat.PPMTool.TertiaryValidation;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Project Name is Required")
	private String projectName;
	
	@NotBlank(message= "A unique project identifier of 4-5 characters is required", groups = Default.class)
	@Size(min=4,max=5, message = "A unique project identifier of 4-5 characters is required", groups = SecondaryValidation.class)
	@Column(updatable = false, unique=true)
	private String projectIdentifier;
	
	@NotBlank(message = "Project description is Required", groups = Default.class)
	@Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Project description should be alphanumeric", groups = SecondaryValidation.class)
	@Size(min=1,max=100, message = "Enter a description between 1 and 100 characters", groups = TertiaryValidation.class)
	private String description;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date start_date;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date end_date;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(updatable = false)
	private Date created_at;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updated_at;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project")
	@JsonIgnore
	private Backlog backlog;

	public Project() {
		//Empty constructor for JPA
	}
	
	@PrePersist
	protected void onCreate() {
		created_at = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updated_at = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}
}
