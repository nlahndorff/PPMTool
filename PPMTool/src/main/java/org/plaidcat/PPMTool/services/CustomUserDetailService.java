package org.plaidcat.PPMTool.services;

import org.plaidcat.PPMTool.domain.User;
import org.plaidcat.PPMTool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Our User value extends UserDetail which is used by spring security.   This service is used for authentication.
 * @author nlahn
 *
 */

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.findByUsername(username);
		
		if (user==null) { 
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}

	
	@Transactional
	public User loadUserById(Long id) {
		var user = userRepository.getById(id);

		if (user==null) { 
			throw new UsernameNotFoundException("User not found");
		}
		return user;		
	}
	
}
