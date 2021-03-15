package org.plaidcat.PPMTool.services;

import org.plaidcat.PPMTool.domain.Backlog;
import org.plaidcat.PPMTool.domain.Project;
import org.plaidcat.PPMTool.domain.ProjectTask;
import org.plaidcat.PPMTool.exception.ProjectNotFoundException;
import org.plaidcat.PPMTool.exception.ProjectTaskNotFoundException;
import org.plaidcat.PPMTool.repositories.BacklogRepository;
import org.plaidcat.PPMTool.repositories.ProjectRepository;
import org.plaidcat.PPMTool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class ProjectTaskService {
   
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask task, String username) {
		
		//projectService.findProjectByIdentifier handles validity and owner validation.
		Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
		
		if (backlog == null) {
			throw new ProjectNotFoundException(projectIdentifier);
		}
		task.setBacklog(backlog);
		
		//ProjectSequence -> projectIdentifier + backlog.PTSequence
		Integer projSeq = backlog.getPTSequence();
		projSeq++;
		
		backlog.setPTSequence(projSeq);
		
		task.setProjectSequence(projectIdentifier + '-' + Integer.toString(projSeq));
		task.setProjectIdentifier(backlog.getProjectIdentifier());
		
		//3 = low priority, 1 = high, need constants or enums
		if (task.getPriority() == null || task.getPriority() == 0) {
			task.setPriority(3);
		}
		
		if (task.getStatus() == null) {
			task.setStatus("TODO");
		}		
		
		return projectTaskRepository.save(task);

	}

	
	public Iterable<ProjectTask> findBacklogById(String id, String username) {

		//Perform validations
		projectService.findProjectByIdentifier(id, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPtByProjectSequence(String backlogId, String sequence, String username) {
		
		//Perform validations
		projectService.findProjectByIdentifier(backlogId, username);
		
		ProjectTask task =  projectTaskRepository.findByProjectSequence(sequence);
		if (task == null) {
			throw new ProjectTaskNotFoundException(sequence);
		}
		
		//Could also compare project id from both objects
		if (!task.getProjectSequence().contains(backlogId)) {
			throw new ProjectNotFoundException(sequence);
		}		
		return task;		
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId, String username) {
		//Call this service method to validate input parms by finding the existing task.
		ProjectTask task =  findPtByProjectSequence(backlogId, ptId, username);		
		updatedTask.setBacklog(task.getBacklog());
		return projectTaskRepository.save(updatedTask);
	}
	
	public void deletePtByProjectSequence(String backlogId, String ptId, String username) {
		ProjectTask task =  findPtByProjectSequence(backlogId, ptId, username);
		projectTaskRepository.delete(task);
	}
		

}
