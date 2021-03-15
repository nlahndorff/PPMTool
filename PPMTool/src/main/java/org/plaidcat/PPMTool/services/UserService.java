package org.plaidcat.PPMTool.services;

import org.plaidcat.PPMTool.domain.User;
import org.plaidcat.PPMTool.exception.UsernameExistsException;
import org.plaidcat.PPMTool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		//User name has to be unique. (new exception)
		//password and confirmpassword are equal
		//Don't persist or show confirm password
		newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
		
		try {
			return userRepo.save(newUser);
		} catch (Exception e) {
			throw new UsernameExistsException("Username already exists");
		}
	}
}
