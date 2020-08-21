package org.plaidcat.PPMTool.services;

import org.plaidcat.PPMTool.domain.Project;
import org.plaidcat.PPMTool.exception.ProjectIdException;
import org.plaidcat.PPMTool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	public Project saveOrUpdateProject(Project project) {
		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("Project Id '" + project.getProjectIdentifier().toUpperCase() + "' already exists.");
		}
	}
	
	public Project findProjectByIdentifier(String projectId) {
		Project proj = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if (proj == null) {
			throw new ProjectIdException("Project id '" + projectId + "' does not exist.");
		}
		return proj;
	}
	
	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProjectById(String projectId) {
		Project project = findProjectByIdentifier(projectId);
		
		if (project == null) {
			throw new ProjectIdException("Project id '" + projectId + "' does not exist.");
		}
		
		projectRepository.delete(project);
	}
}
