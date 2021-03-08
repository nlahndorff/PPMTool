package org.plaidcat.PPMTool.services;

import org.plaidcat.PPMTool.domain.Backlog;
import org.plaidcat.PPMTool.domain.Project;
import org.plaidcat.PPMTool.domain.ProjectTask;
import org.plaidcat.PPMTool.exception.ProjectNotFoundException;
import org.plaidcat.PPMTool.repositories.BacklogRepository;
import org.plaidcat.PPMTool.repositories.ProjectRepository;
import org.plaidcat.PPMTool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectRepository projectRepository;
    
	@Autowired
	BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask task) {
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		
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

	
	public Iterable<ProjectTask> findBacklogById(String id) {
		Project p = projectRepository.findByProjectIdentifier(id); 
		if (p == null) {
			throw new ProjectNotFoundException(id);
		}
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPtByProjectSequence(String backlogId, String sequence) {	
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
		if (backlog == null) { 
			throw new ProjectNotFoundException(backlogId);
		}
		
		ProjectTask task =  projectTaskRepository.findByProjectSequence(sequence);
		if (task == null) {
			//TODO:  Add new exception for task not found
			throw new ProjectNotFoundException(sequence);
		}
		
		//Could also compare project id from both objects
		if (!task.getProjectSequence().contains(backlog.getProjectIdentifier())) {
			throw new ProjectNotFoundException(sequence);
		}
		
		return task;		
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId) {
		//Call this service method to validate input parms by finding the existing task.
		ProjectTask task =  findPtByProjectSequence(backlogId, ptId);		
		updatedTask.setBacklog(task.getBacklog());
		return projectTaskRepository.save(updatedTask);
	}
	
	public void deletePtByProjectSequence(String backlogId, String ptId) {
		ProjectTask task =  findPtByProjectSequence(backlogId, ptId);
		projectTaskRepository.delete(task);
	}
		

}
