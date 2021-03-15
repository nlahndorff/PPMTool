package org.plaidcat.PPMTool.repositories;

import org.plaidcat.PPMTool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	@Override
	Iterable<Project> findAllById(Iterable<Long> iterable);

	Project findByProjectIdentifier(String projectIdentifier);
	
	Iterable<Project> findAll();
		
	Iterable<Project> findAllByProjectLeader(String username);
}
