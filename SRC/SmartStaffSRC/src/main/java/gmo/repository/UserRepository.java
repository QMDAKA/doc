package gmo.repository;

import gmo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Quang Minh on 8/11/2016.
 */
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
