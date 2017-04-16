package gmo.repository;

import gmo.model.Hotkey;
import gmo.model.New;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by quangminh on 08/12/2016.
 */
@RepositoryRestResource
public interface HotkeyRepository extends JpaRepository<Hotkey, Long> {
    Hotkey findByWord(String word);

    Page<Hotkey> findAllByOrderByTotalDesc(Pageable pageable);

}
