package org.plaidcat.PPMTool.web;

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
	@PostMapping("/{backlog_id}")
	public ResponseEntity<?> addProjectTaskToBackLog(@RequestBody ProjectTask task, BindingResult result, @PathVariable String backlog_id ) {
		
		if (!ValidationUtil.isValid(result, task, Default.class, SecondaryValidation.class)) {
			ResponseEntity<?> errorMap = errorService.validationError(result);
			if (errorMap != null) {
				return errorMap;
			}
		}
		
		ProjectTask newTask = ptService.addProjectTask(backlog_id, task);
		
		return new ResponseEntity<ProjectTask>(newTask, HttpStatus.CREATED);
	}
	
	/**
	 * Retrieves the list of tasks belonging to the backlog
	 * @param backlog_id - The projectIdentifier of the project we're retrieving tasks for
	 * @return - A list of ProjectTask objects
	 */
	@GetMapping("/{backlog_id}")
	public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id) {
		
		return ptService.findBacklogById(backlog_id);
	}

	/**
	 * Retrieves a single task from the backlog using the project identifier and task sequence
	 * @param backlog_id - The projectIdentifier for this backlog
	 * @param sequence - The sequence within the project of the task we are looking for
	 * @return
	 */
	@GetMapping("/{backlog_id}/{sequence}")
	public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String sequence) {
		
		ProjectTask task = ptService.findPtByProjectSequence(backlog_id, sequence);
		return new ResponseEntity<ProjectTask>(task, HttpStatus.OK);
	}
	
	@PatchMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result, 
			@PathVariable String backlog_id, @PathVariable String pt_id) {
		
		if (!ValidationUtil.isValid(result, projectTask, Default.class, SecondaryValidation.class)) {
			ResponseEntity<?> errorMap = errorService.validationError(result);
			if (errorMap != null) {
				return errorMap;
			}
		}
		
		ProjectTask updatedTask = ptService.updateByProjectSequence(projectTask, backlog_id);
		
		
		return new ResponseEntity<ProjectTask>(updatedTask, HttpStatus.OK);
	}
}
