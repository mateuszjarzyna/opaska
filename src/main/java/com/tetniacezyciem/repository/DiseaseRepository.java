package com.tetniacezyciem.repository;

import com.tetniacezyciem.entity.Disease;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

/**
 * Created by mateusz on 10.04.2017.
 */
@RepositoryRestResource(collectionResourceRel = "disease", path = "disease")
public interface DiseaseRepository extends PagingAndSortingRepository<Disease, String> {

    Set<Disease> findByNameIn(List<String> names);

}
