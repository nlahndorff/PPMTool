package org.plaidcat.PPMTool.repositories;

import java.util.List;

import org.plaidcat.PPMTool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
	
	List<ProjectTask> findByProjectIdentifierOrderByPriority(String backlog_id);

	ProjectTask findByProjectSequence(String projectSequcene);
}
