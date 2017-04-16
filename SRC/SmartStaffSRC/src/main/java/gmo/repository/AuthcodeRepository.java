package gmo.repository;

import gmo.model.Authcode;
import gmo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by quangminh on 14/09/2016.
 */
@RepositoryRestResource
public interface AuthcodeRepository extends JpaRepository<Authcode, Long> {

    Authcode findByCode(String code);

    Long deleteByCode(String code);

}
