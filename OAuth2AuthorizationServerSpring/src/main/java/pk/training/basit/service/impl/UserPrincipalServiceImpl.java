package pk.training.basit.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pk.training.basit.jpa.entity.UserPrincipal;
import pk.training.basit.jpa.repository.UserPrincipalRepository;
import pk.training.basit.service.UserPrincipalService;

@Service
public class UserPrincipalServiceImpl implements UserPrincipalService {

    private final UserPrincipalRepository userPrincipalRepository;

	public UserPrincipalServiceImpl(UserPrincipalRepository userPrincipalRepository) {
		this.userPrincipalRepository = userPrincipalRepository;
	}
    
    @Override
    @Transactional
    public UserPrincipal loadUserByUsername(String username) {
        
    	UserPrincipal principal = userPrincipalRepository.getByUsername(username);
       
    	// make sure the authorities and password are loaded
        principal.getAuthorities().size();
        principal.getPassword();
        return principal;
    }
      
}
