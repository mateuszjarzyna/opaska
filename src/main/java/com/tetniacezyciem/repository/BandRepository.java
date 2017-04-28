package com.tetniacezyciem.repository;

import com.tetniacezyciem.entity.Band;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * Created by mateusz on 10.04.2017.
 */
@RepositoryRestResource(collectionResourceRel = "band", path = "band")
public interface BandRepository extends PagingAndSortingRepository<Band, String>  {

    Band findByBandShortId(String shortId);

}
