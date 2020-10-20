package org.plaidcat.PPMTool.services;

import org.plaidcat.PPMTool.domain.Backlog;
import org.plaidcat.PPMTool.domain.Project;
import org.plaidcat.PPMTool.exception.ProjectIdException;
import org.plaidcat.PPMTool.exception.ProjectNotFoundException;
import org.plaidcat.PPMTool.repositories.BacklogRepository;
import org.plaidcat.PPMTool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	public Project saveOrUpdateProject(Project project) {
		
		project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
		try {
			project.setProjectIdentifier(project.getProjectIdentifier());
			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier());
			} else {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
			}
			
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("Project Id '" + project.getProjectIdentifier() + "' already exists.");
		}
	}
	
	public Project findProjectByIdentifier(String projectId) {
		Project proj = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if (proj == null) {
			throw new ProjectNotFoundException(projectId);
		}
		return proj;
	}
	
	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProjectById(String projectId) {
		Project project = findProjectByIdentifier(projectId);
		
		if (project == null) {
			throw new ProjectNotFoundException(projectId);
		}
		
		projectRepository.delete(project);
	}
}
