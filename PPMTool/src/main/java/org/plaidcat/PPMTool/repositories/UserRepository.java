/**
 * 
 */
package org.plaidcat.PPMTool.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import org.plaidcat.PPMTool.domain.User;

/**
 * @author nlahn
 *
 */

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
	
	User findByUsername(String username);
	User getById(Long id);
	
	Optional<User> findById(Long id);

}
