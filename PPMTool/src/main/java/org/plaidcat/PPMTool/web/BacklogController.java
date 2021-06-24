package org.plaidcat.PPMTool.web;

import java.security.Principal;

import javax.validation.Valid;
import javax.validation.groups.Default;

import org.plaidcat.PPMTool.SecondaryValidation;
import org.plaidcat.PPMTool.ValidationUtil;
import org.plaidcat.PPMTool.domain.ProjectTask;
import org.plaidcat.PPMTool.services.ProjectTaskService;
import org.plaidcat.PPMTool.services.ValidationErrorUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
@SuppressWarnings("rawtypes")
public class BacklogController {
	
	@Autowired
	ProjectTaskService ptService;
	
	@Autowired
	ValidationErrorUtilService errorService;
	
	/**
	 * Adds a task to a project's backlog
	 * @param task - The task being added
	 * @param result - Any errors returned
	 * @param backlog_id - the ProjectIdentifier of the backlog we're adding the task to
	 * @return Either errors, or value of the created task
	 */
	
	@PostMapping("/{backlogId}")
	public ResponseEntity addProjectTaskToBackLog(@RequestBody ProjectTask task, BindingResult result, @PathVariable String backlogId, Principal principal ) {
		
		if (!ValidationUtil.isValid(result, task, Default.class, SecondaryValidation.class)) {
			ResponseEntity<?> errorMap = errorService.validationError(result);
			if (errorMap != null) {
				return errorMap;
			}
		}
		
		var newTask = ptService.addProjectTask(backlogId, task, principal.getName());
		
		return new ResponseEntity<>(newTask, HttpStatus.CREATED);
	}
	
	/**
	 * Retrieves the list of tasks belonging to the backlog
	 * @param backlogId - The projectIdentifier of the project we're retrieving tasks for
	 * @return - A list of ProjectTask objects
	 */
	@GetMapping("/{backlogId}")
	public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlogId, Principal principal) {
		
		return ptService.findBacklogById(backlogId, principal.getName());
	}

	/**
	 * Retrieves a single task from the backlog using the project identifier and task sequence
	 * @param backlogId - The projectIdentifier for this backlog
	 * @param sequence - The sequence within the project of the task we are looking for
	 * @return
	 */
	@GetMapping("/{backlogId}/{sequence}")
	public ResponseEntity getProjectTask(@PathVariable String backlogId, @PathVariable String sequence, Principal principal) {
		
		ProjectTask task = ptService.findPtByProjectSequence(backlogId, sequence, principal.getName());
		return new ResponseEntity<>(task, HttpStatus.OK);
	}
	
	@PatchMapping("/{backlogId}/{ptId}")
	public ResponseEntity updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result, 
			@PathVariable String backlogId, @PathVariable String ptId, Principal principal) {
		
		if (!ValidationUtil.isValid(result, projectTask, Default.class, SecondaryValidation.class)) {
			ResponseEntity<?> errorMap = errorService.validationError(result);
			if (errorMap != null) {
				return errorMap;
			}
		}
		
		ProjectTask updatedTask = ptService.updateByProjectSequence(projectTask, backlogId, ptId, principal.getName());
				
		return new ResponseEntity<>(updatedTask, HttpStatus.OK);
	}
	
	@DeleteMapping("/{backlogId}/{ptId}")
	public ResponseEntity deleteProjectTask(@PathVariable String backlogId, @PathVariable String ptId, Principal principal) {
		ptService.deletePtByProjectSequence(backlogId, ptId, principal.getName());
		
		return new ResponseEntity<>("Project Task " + ptId + " was successfully deleted", HttpStatus.OK);
	}
}
