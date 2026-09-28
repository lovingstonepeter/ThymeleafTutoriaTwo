package com.example.thymeleaftutorialtwo.service;

import com.example.thymeleaftutorialtwo.dto.ContributeRequestDTO;
import com.example.thymeleaftutorialtwo.model.Contributor;
import com.example.thymeleaftutorialtwo.repository.ContributorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContributorService {

    private final ContributorRepository contributorRepository;

    // CONSTRUCTOR INJECTION
    // Spring sees ContributorRepository as a dependency and automatically
    // gives an instance of it to this constructor.
    public ContributorService(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }


    // ==========================================================
    // DTO -> ENTITY
    // ==========================================================
    // A DTO is the object we use to transfer data between layers.
    // An Entity is the object that represents our database record.
    //
    // Example:
    // DTO:
    // { name = "John", amount = 500 }
    //
    // becomes:
    // Contributor entity:
    // { name = "John", amount = 500 }
    public Contributor convertToEntity(ContributeRequestDTO dto) {

        return new Contributor(
                dto.getId(),
                dto.getName(),
                dto.getAmount()
        );
    }


    // ==========================================================
    // ENTITY -> DTO
    // ==========================================================
    // We use this when data comes from the database and we want
    // to send it back to the controller/view as a DTO.
    public ContributeRequestDTO convertToDTO(Contributor contributor) {

        return new ContributeRequestDTO(
                contributor.getId(),
                contributor.getName(),
                contributor.getAmount()
        );
    }


    // ==========================================================
    // READ - GET ALL CONTRIBUTORS
    // ==========================================================
    public List<ContributeRequestDTO> getAllContributors() {

        return contributorRepository.findAll()
                .stream()

                // For every Contributor entity returned by the database,
                // convert it into a ContributeRequestDTO.
                //
                // This:
                // .map(this::convertToDTO)
                //
                // is equivalent to:
                //
                // .map(contributor -> convertToDTO(contributor))
                .map(this::convertToDTO)

                .collect(Collectors.toList());
    }
    public ContributeRequestDTO getContributorById(Long id) {

        Contributor contributor = contributorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Contributor with ID " + id + " not found"
                        )
                );

        return convertToDTO(contributor);
    }


    // ==========================================================
    // CREATE - ADD CONTRIBUTOR
    // ==========================================================
    public ContributeRequestDTO addContributor(
            ContributeRequestDTO dto) {

        // Instead of manually doing:
        //
        // Contributor contributor = new Contributor();
        // contributor.setName(dto.getName());
        // contributor.setAmount(dto.getAmount());
        //
        // we can reuse our conversion method.
        Contributor contributor = convertToEntity(dto);

        // save() tells Spring Data JPA to persist the entity.
        //
        // Because this is a NEW contributor, this normally results
        // in an INSERT into the database.
        contributorRepository.save(contributor);

        // Return the saved entity as a DTO.
        return convertToDTO(contributor);
    }


    // ==========================================================
    // UPDATE - UPDATE EXISTING CONTRIBUTOR
    // ==========================================================
    public ContributeRequestDTO updateContributor(
            Long id,
            ContributeRequestDTO dto) {

        // IMPORTANT:
        //
        // For an UPDATE, we first find the EXISTING record.
        //
        // We don't simply create a completely new object and assume
        // that the record exists.
        Contributor contributor = contributorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Contributor with ID " + id + " not found"
                        )
                );

        // Now change the existing entity's values.
        contributor.setName(dto.getName());
        contributor.setAmount(dto.getAmount());

        // Save the modified entity.
        //
        // Because this entity already has an existing ID,
        // JPA treats it as an existing record and updates it.
        contributorRepository.save(contributor);

        return convertToDTO(contributor);
    }


    // ==========================================================
    // DELETE
    // ==========================================================
    public void deleteContributor(Long id) {

        // Delete the database record whose primary key is 'id'.
        contributorRepository.deleteById(id);
    }
}