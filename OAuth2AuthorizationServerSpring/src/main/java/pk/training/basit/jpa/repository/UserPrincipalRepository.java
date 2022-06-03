package pk.training.basit.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import pk.training.basit.jpa.entity.UserPrincipal;


public interface UserPrincipalRepository extends CrudRepository<UserPrincipal, Long> {
    
	UserPrincipal getByUsername(String username);
}
