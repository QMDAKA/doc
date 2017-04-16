package gmo.configuration;

import gmo.model.Authcode;
import gmo.model.Doc;
import gmo.model.New;
import gmo.model.User;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 * Created by QUANG MINH on 3/8/2016.
 */


public class JpaConfig extends RepositoryRestMvcConfiguration {
    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(New.class);
        config.exposeIdsFor(Doc.class);
        config.exposeIdsFor(User.class);
        config.exposeIdsFor(Authcode.class);
    }
}
