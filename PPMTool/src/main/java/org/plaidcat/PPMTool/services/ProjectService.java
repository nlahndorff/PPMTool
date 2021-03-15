package org.plaidcat.PPMTool.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.plaidcat.PPMTool.domain.Backlog;
import org.plaidcat.PPMTool.domain.Project;
import org.plaidcat.PPMTool.domain.User;
import org.plaidcat.PPMTool.exception.ProjectIdException;
import org.plaidcat.PPMTool.exception.ProjectNotFoundException;
import org.plaidcat.PPMTool.repositories.BacklogRepository;
import org.plaidcat.PPMTool.repositories.ProjectRepository;
import org.plaidcat.PPMTool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdateProject(Project project, String username) {
		
		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			//If we're updating a project, we need to make sure its real and belongs to this user.
			if (project.getId() != null) {
				Optional<Project> p = projectRepository.findById(project.getId());
				
				try {
					Project existingProj = p.get();
					//If the leader doesn't match the user id or the project identifier doesn't match the original,
					// someone has monkeyed around with the request values.
					if (!existingProj.getProjectLeader().equalsIgnoreCase(username) || 
							!existingProj.getProjectIdentifier().equalsIgnoreCase(project.getProjectIdentifier())) {
						throw new ProjectNotFoundException(project.getProjectIdentifier());
					}					
				} catch (NoSuchElementException e) {
					throw new ProjectNotFoundException(project.getProjectIdentifier()); 
				}
				
				//ensure the backlog is set for the project
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
			} else {			
				User user = userRepository.findByUsername(username);
			 						
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier());
				
				project.setUser(user);
				project.setProjectLeader(user.getUsername());
				project.setProjectIdentifier(project.getProjectIdentifier());
			} 
			
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("Project Id '" + project.getProjectIdentifier() + "' already exists.");
		}
	}
	
	public Project findProjectByIdentifier(String projectId, String username) {
		Project proj = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if (proj == null || !proj.getProjectLeader().equalsIgnoreCase(username)) {
			throw new ProjectNotFoundException(projectId);
		}
		return proj;
	}
	
	public Iterable<Project> findAllProjects(String username) {
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProjectById(String projectId, String username) {	
		projectRepository.delete(findProjectByIdentifier(projectId, username));
	}
}
