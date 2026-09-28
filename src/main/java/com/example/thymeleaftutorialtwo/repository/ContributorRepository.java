package com.example.thymeleaftutorialtwo.repository;

import com.example.thymeleaftutorialtwo.model.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorRepository
        extends JpaRepository<Contributor, Long> {

    /*
     * We don't need to write:
     *
     * findAll()
     * save()
     * findById()
     * deleteById()
     *
     * ourselves.
     *
     * JpaRepository already provides them.
     *
     * Contributor = entity type
     * Long        = type of the entity's primary key
     */
}