package org.plaidcat.PPMTool.web;


import javax.validation.Valid;

import org.plaidcat.PPMTool.domain.Project;
import org.plaidcat.PPMTool.services.ProjectService;
import org.plaidcat.PPMTool.services.ValidationErrorUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ValidationErrorUtilService validationUtil;
	
	@PostMapping("")
	public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {
		
		ResponseEntity<?> errors = validationUtil.ValidationError(result);
		if (errors != null) { return errors; }
		
		projectService.saveOrUpdateProject(project);
		return new ResponseEntity<Project>(project, HttpStatus.CREATED);
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable String projectId) {

		Project project = projectService.findProjectByIdentifier(projectId);
		
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public Iterable<Project> getAllProjects() {
		return projectService.findAllProjects();
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProjectById(@PathVariable String projectId) {
		
		projectService.deleteProjectById(projectId.toUpperCase());
		
		return new ResponseEntity<String>("Project '" + projectId + "' has been deleted", HttpStatus.OK);
	}
}
