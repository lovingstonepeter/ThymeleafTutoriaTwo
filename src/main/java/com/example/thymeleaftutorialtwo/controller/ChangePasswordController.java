package com.example.thymeleaftutorialtwo.controller;

// DTO containing the password-change form data.
import com.example.thymeleaftutorialtwo.dto.ChangePasswordRequestDTO;

// InMemoryUserDetailsManager allows us to find and update
// the Treasurer's in-memory account.
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// Authentication represents the currently authenticated user.
import org.springframework.security.core.Authentication;

// UserDetails contains information about the authenticated user.
import org.springframework.security.core.userdetails.UserDetails;

// Used to encode passwords and verify whether a password matches
// an existing encoded password.
import org.springframework.security.crypto.password.PasswordEncoder;

// Marks this class as an MVC controller.
import org.springframework.stereotype.Controller;

// Used to pass data from the controller to Thymeleaf.
import org.springframework.ui.Model;

// Handles GET and POST requests.
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// Handles HTTP form parameters.
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class ChangePasswordController {

    /*
     * ================================================================
     * DEPENDENCIES
     * ================================================================
     *
     * We need three things:
     *
     * 1. InMemoryUserDetailsManager
     *    → Find the current user and update their password.
     *
     * 2. PasswordEncoder
     *    → Verify the current password and encode the new password.
     *
     * 3. Authentication
     *    → Tell us which user is currently logged in.
     *
     * The first two are injected through the constructor.
     */
    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;


    /*
     * Constructor injection.
     *
     * Spring automatically supplies the objects that we need because
     * they were registered as @Bean objects.
     */
    public ChangePasswordController(
            InMemoryUserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder) {

        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }


    /*
     * ================================================================
     * DISPLAY CHANGE PASSWORD PAGE
     * ================================================================
     *
     * When the authenticated Treasurer visits:
     *
     *     /change-password
     *
     * this method displays the form.
     *
     * We don't have to explicitly check whether the user is logged in
     * here because SecurityConfig already contains:
     *
     *     .anyRequest().authenticated()
     *
     * Therefore Spring Security protects this endpoint before this
     * method is reached.
     */
    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {

        /*
         * Create an empty DTO.
         *
         * Thymeleaf will use this object to bind the form fields.
         */
        model.addAttribute(
                "changePasswordRequest",
                new ChangePasswordRequestDTO()
        );

        /*
         * Return the Thymeleaf template:
         *
         *     templates/change-password.html
         */
        return "change-password";
    }


    /*
     * ================================================================
     * PROCESS PASSWORD CHANGE
     * ================================================================
     *
     * The form will send:
     *
     *     POST /change-password
     *
     * Spring Security has already authenticated the user.
     *
     * Authentication tells us WHO is logged in.
     */
    @PostMapping("/change-password")
    public String changePassword(
            @ModelAttribute("changePasswordRequest")
            ChangePasswordRequestDTO request,

            Authentication authentication,

            Model model) {


        /*
         * ============================================================
         * 1. FIND THE CURRENTLY LOGGED-IN USER
         * ============================================================
         *
         * authentication.getName()
         *
         * returns the username of the currently authenticated user.
         *
         * In our application this will be:
         *
         *     "treasurer"
         *
         * Notice that we are NOT asking the browser:
         *
         *     "Which user's password do you want to change?"
         *
         * This is important for security.
         *
         * We use the identity that Spring Security has already
         * authenticated.
         */
        String username = authentication.getName();


        /*
         * ============================================================
         * 2. LOAD THAT USER
         * ============================================================
         *
         * Ask the InMemoryUserDetailsManager to find the user.
         */
        UserDetails user =
                userDetailsManager.loadUserByUsername(username);


        /*
         * ============================================================
         * 3. VERIFY THE CURRENT PASSWORD
         * ============================================================
         *
         * The user entered their existing password in the form.
         *
         * We compare:
         *
         *     request.getCurrentPassword()
         *
         * against:
         *
         *     user.getPassword()
         *
         * The stored password is BCrypt encoded.
         *
         * Therefore we MUST use:
         *
         *     passwordEncoder.matches(...)
         *
         * instead of:
         *
         *     currentPassword.equals(storedPassword)
         *
         * BCrypt handles the comparison correctly.
         */
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            /*
             * The old password is incorrect.
             *
             * Return to the form and display an error message.
             */
            model.addAttribute(
                    "error",
                    "Current password is incorrect."
            );

            return "change-password";
        }


        /*
         * ============================================================
         * 4. CHECK NEW PASSWORD CONFIRMATION
         * ============================================================
         *
         * The user enters the new password twice:
         *
         *     New password
         *     Confirm new password
         *
         * Both values must be identical.
         */
        if (!request.getNewPassword().equals(
                request.getConfirmPassword())) {

            model.addAttribute(
                    "error",
                    "New passwords do not match."
            );

            return "change-password";
        }


        /*
         * ============================================================
         * 5. ENCODE THE NEW PASSWORD
         * ============================================================
         *
         * NEVER store the new password directly.
         *
         * For example, don't do:
         *
         *     updatePassword("newpassword")
         *
         * Instead:
         *
         *     newPassword
         *          ↓
         *     BCrypt
         *          ↓
         *     encoded password
         */
        String encodedNewPassword =
                passwordEncoder.encode(request.getNewPassword());


        /*
         * ============================================================
         * 6. UPDATE THE PASSWORD
         * ============================================================
         *
         * We give the manager:
         *
         *     - the current user
         *     - the newly encoded password
         *
         * The manager updates the user's password in memory.
         */
        userDetailsManager.updatePassword(
                user,
                encodedNewPassword
        );


        /*
         * ============================================================
         * 7. DISPLAY SUCCESS MESSAGE
         * ============================================================
         */
        model.addAttribute(
                "success",
                "Password changed successfully."
        );


        /*
         * Stay on the change-password page so the Treasurer can
         * see the success message.
         */
        return "change-password";
    }
}

