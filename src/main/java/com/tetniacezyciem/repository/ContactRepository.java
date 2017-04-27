package com.tetniacezyciem.repository;

import com.tetniacezyciem.entity.Contact;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by mateusz on 12.04.2017.
 */
@RepositoryRestResource(collectionResourceRel = "contact", path = "contact")
public interface ContactRepository extends PagingAndSortingRepository<Contact, String> {
}
