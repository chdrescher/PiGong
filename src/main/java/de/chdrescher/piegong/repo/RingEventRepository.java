package de.chdrescher.piegong.repo;

import de.chdrescher.piegong.model.RingEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RingEventRepository extends CrudRepository<RingEvent, Integer>{
    List<RingEvent> findAllByOrderByRingDateDesc();
}
