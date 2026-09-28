package com.example.thymeleaftutorialtwo.controller;

import com.example.thymeleaftutorialtwo.dto.ContributeRequestDTO;
import com.example.thymeleaftutorialtwo.service.ContributorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContributionController {

    private final ContributorService contributorService;


    // ==========================================================
    // CONSTRUCTOR INJECTION
    // ==========================================================
    public ContributionController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }


    // ==========================================================
    // READ - DISPLAY ALL CONTRIBUTORS
    // ==========================================================
    @GetMapping("/contributorsList")
    public String getContributionList(Model model) {

        // Ask the service for all contributors.
        List<ContributeRequestDTO> contributors =
                contributorService.getAllContributors();

        // Put the list inside the Model.
        //
        // The name "contributors" is VERY IMPORTANT.
        //
        // Thymeleaf will later use:
        //
        // ${contributors}
        //
        model.addAttribute("contributors", contributors);

        // Open:
        // templates/contributorsList.html
        return "contributorsList";
    }


    // ==========================================================
    // CREATE - SHOW ADD FORM
    // ==========================================================
    @GetMapping("/")
    public String addContributorForm(Model model) {

        // Create an EMPTY DTO.
        //
        // This object will be connected to the Thymeleaf form.
        model.addAttribute(
                "contributor",
                new ContributeRequestDTO()
        );

        return "form";
    }


    // ==========================================================
    // CREATE - PROCESS ADD FORM
    // ==========================================================
    @PostMapping("/contributors")
    public String addContributor(
            @ModelAttribute("contributor")
            ContributeRequestDTO dto) {

        /*
         * @ModelAttribute does something important.
         *
         * Suppose the HTML form submits:
         *
         * name=John
         * amount=500
         *
         * Spring creates/populates:
         *
         * ContributeRequestDTO
         *
         * so that:
         *
         * dto.getName()   -> "John"
         * dto.getAmount() -> 500
         */

        contributorService.addContributor(dto);

        // redirect prevents the browser from resubmitting
        // the POST request when the page is refreshed.
        return "redirect:/contributorsList";
    }


    // ==========================================================
    // UPDATE - SHOW EDIT FORM
    // ==========================================================
    @GetMapping("/contributors/{id}/edit")
    public String showUpdateForm(
            @PathVariable Long id,
            Model model) {

        /*
         * Example:
         *
         * User clicks:
         *
         * /contributors/5/edit
         *
         * Spring extracts:
         *
         * id = 5
         *
         * because:
         *
         * @PathVariable Long id
         *
         * connects the URL's {id} to the Java variable.
         */

        // At this point, we need to obtain the existing contributor.
        // For this simple tutorial, we can use the repository indirectly
        // through a service method.
        //
        // We haven't created that service method yet, so for now
        // this controller example assumes one exists.
        //
        // See the improved service version below.
        ContributeRequestDTO contributor =
                contributorService.getContributorById(id);

        // Put the EXISTING contributor into the model.
        model.addAttribute("contributor", contributor);

        return "form";
    }


    // ==========================================================
    // UPDATE - PROCESS EDIT FORM
    // ==========================================================
    @PostMapping("/contributors/{id}/update")
    public String updateContributor(
            @PathVariable Long id,
            @ModelAttribute("contributor")
            ContributeRequestDTO dto) {

        /*
         * Example:
         *
         * POST /contributors/5/update
         *
         * means:
         *
         * "Update contributor whose ID is 5."
         */

        contributorService.updateContributor(id, dto);

        return "redirect:/contributorsList";
    }


    // ==========================================================
    // DELETE
    // ==========================================================
    @PostMapping("/contributors/{id}/delete")
    public String deleteContributor(
            @PathVariable Long id) {

        /*
         * Example:
         *
         * POST /contributors/5/delete
         *
         * means:
         *
         * delete contributor with ID 5.
         */

        contributorService.deleteContributor(id);

        return "redirect:/contributorsList";
    }
}