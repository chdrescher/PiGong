package de.chdrescher.piegong.repo;

import de.chdrescher.piegong.model.AudioModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioModelRepository extends PagingAndSortingRepository<AudioModel, Integer>{
    List<AudioModel> findByActive(boolean b);

    AudioModel findByName(String name);

    List<AudioModel> findAll();
}
