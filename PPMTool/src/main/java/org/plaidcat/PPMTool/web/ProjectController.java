package org.plaidcat.PPMTool.web;

import java.security.Principal;

import javax.validation.groups.Default;

import org.plaidcat.PPMTool.SecondaryValidation;
import org.plaidcat.PPMTool.TertiaryValidation;
import org.plaidcat.PPMTool.ValidationUtil;
import org.plaidcat.PPMTool.domain.Project;
import org.plaidcat.PPMTool.services.ProjectService;
import org.plaidcat.PPMTool.services.ValidationErrorUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ValidationErrorUtilService validationUtil;
	
	/**
	 * Creates or updates a project.  If the project has an id, it will be updated, otherwise a new project will be created
	 * @param project - The project object to create or update
	 * @param result - The resulting project
	 * @return
	 */
	@PostMapping("")
	public ResponseEntity<?> createOrUpdateNewProject(@RequestBody Project project, BindingResult result, Principal principal) {
		
		if (!ValidationUtil.isValid(result, project, Default.class, SecondaryValidation.class, TertiaryValidation.class)) {
			ResponseEntity<?> errors = validationUtil.validationError(result);
			if (errors != null) { 
				return errors; 
			}
		}
		
		projectService.saveOrUpdateProject(project, principal.getName());
		return new ResponseEntity<>(project, HttpStatus.CREATED);
	}
	
	/**
	 * Retrieve an individual project from the database
	 * @param projectId - The projectIdentifier of the project being searched for
	 * @return - Either an error response, or the Project being searched for
	 */
	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {

		var project = projectService.findProjectByIdentifier(projectId, principal.getName());
		
		return new ResponseEntity<>(project, HttpStatus.OK);
	}
	
	/**
	 * Retrieves a list of all projects
	 * @return - List of Project objects
	 */
	@GetMapping("/all")
	public Iterable<Project> getAllProjects(Principal principal) {
		return projectService.findAllProjects(principal.getName());
	}

	/**
	 * Deletes a project
	 * @param projectId - The projectIdentifier to be deleted
	 * @return - Either an error (unknown project) or a success message
	 */
	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProjectById(@PathVariable String projectId, Principal principal) {
		
		projectService.deleteProjectById(projectId.toUpperCase(), principal.getName());
		
		return new ResponseEntity<>("Project '" + projectId + "' has been deleted", HttpStatus.OK);
	}
}
