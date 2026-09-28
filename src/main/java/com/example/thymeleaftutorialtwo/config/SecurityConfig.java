package com.example.thymeleaftutorialtwo.config;

// HttpSecurity is used to configure how HTTP requests are secured.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// @Bean allows us to register objects in Spring's application context.
import org.springframework.context.annotation.Bean;

// @Configuration tells Spring that this class contains configuration
// and bean definitions.
import org.springframework.context.annotation.Configuration;

// User is a builder/helper class used to create a Spring Security user.
import org.springframework.security.core.userdetails.User;

// UserDetails represents the information Spring Security needs
// about a user, such as username, password and authorities/roles.
import org.springframework.security.core.userdetails.UserDetails;

// InMemoryUserDetailsManager stores user accounts in application memory.
// It also provides user-management operations such as updating passwords.
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// BCryptPasswordEncoder hashes passwords using the BCrypt algorithm.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// PasswordEncoder is the interface Spring Security uses for password
// encoding and password verification.
import org.springframework.security.crypto.password.PasswordEncoder;

// SecurityFilterChain defines the security rules that Spring Security
// applies to incoming HTTP requests.
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    /*
     * ================================================================
     * 1. PASSWORD ENCODER
     * ================================================================
     *
     * We should NEVER store a user's password as plain text.
     *
     * For example, we should NOT store:
     *
     *     treasurer123
     *
     * Instead, BCrypt converts it into a one-way encoded value.
     *
     * Example conceptually:
     *
     *     treasurer123
     *          ↓
     *     BCrypt
     *          ↓
     *     $2a$10$................................
     *
     * Spring Security can later check whether a password matches
     * the encoded password without needing to store the original
     * plain-text password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        // BCryptPasswordEncoder is a commonly used password encoder
        // provided by Spring Security.
        return new BCryptPasswordEncoder();
    }


    /*
     * ================================================================
     * 2. CREATE OUR IN-MEMORY USER MANAGER
     * ================================================================
     *
     * You asked why we are using InMemoryUserDetailsManager instead
     * of simply UserDetailsService.
     *
     * The important distinction is:
     *
     * UserDetailsService:
     *     - Mainly provides user information to Spring Security.
     *     - Its main operation is loading a user by username.
     *
     * InMemoryUserDetailsManager:
     *     - Stores users in application memory.
     *     - Implements UserDetailsService.
     *     - Also implements user-management functionality.
     *     - Allows operations such as updating a user's password.
     *
     * Since our Treasurer must be able to CHANGE their password,
     * we want access to the concrete manager that can update the user.
     *
     * IMPORTANT:
     * This is suitable for our tutorial/demo application.
     *
     * If we were building a production system, the user would normally
     * be stored in a database such as PostgreSQL.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(
            PasswordEncoder passwordEncoder) {


        /*
         * Create the Treasurer's account.
         *
         * User.withUsername(...) starts the Spring Security User builder.
         */
        UserDetails treasurer = User
                .withUsername("treasurer")

                /*
                 * We DO NOT store the password directly.
                 *
                 * passwordEncoder.encode(...)
                 * converts the plain password into a BCrypt hash.
                 *
                 * The user types:
                 *
                 *     treasurer123
                 *
                 * But the manager stores the encoded version.
                 */
                .password(passwordEncoder.encode("treasurer123"))

                /*
                 * roles("TREASURER") gives this user the role:
                 *
                 *     ROLE_TREASURER
                 *
                 * Spring Security automatically adds the "ROLE_"
                 * prefix when using the roles(...) method.
                 *
                 * We can later use this role to restrict certain
                 * functionality specifically to the Treasurer.
                 */
                .roles("TREASURER")

                /*
                 * build() finishes construction of the UserDetails
                 * object.
                 */
                .build();


        /*
         * Return the manager containing our Treasurer account.
         *
         * Spring Security will use this manager when it needs to
         * find the user attempting to log in.
         */
        return new InMemoryUserDetailsManager(treasurer);
    }


    /*
     * ================================================================
     * 3. SECURITY FILTER CHAIN
     * ================================================================
     *
     * This is where we define:
     *
     *     - Which pages are public
     *     - Which pages require authentication
     *     - Which login page to use
     *     - Where the user goes after login
     *     - What happens during logout
     *
     * Spring Security places filters in front of our controllers.
     *
     * Therefore, a request such as:
     *
     *     GET /contributorsList
     *
     * reaches Spring Security BEFORE it reaches our controller.
     *
     * Security then decides:
     *
     *     "Is this person authenticated?"
     *
     * If YES:
     *     allow the request to continue.
     *
     * If NO:
     *     redirect the user to the login page.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {


        /*
         * ============================================================
         * 3A. AUTHORIZATION RULES
         * ============================================================
         *
         * Authorization answers:
         *
         *     "What is this user allowed to access?"
         *
         * Authentication answers:
         *
         *     "Who is this user, and are they logged in?"
         *
         * Here we configure authorization.
         */
        http.authorizeHttpRequests(auth -> auth


                /*
                 * The login page must be publicly accessible.
                 *
                 * Why?
                 *
                 * Imagine a user is NOT logged in and visits:
                 *
                 *     /login
                 *
                 * If /login itself required authentication, Spring
                 * would say:
                 *
                 *     "You must log in to see the login page."
                 *
                 * But the user cannot log in because they cannot
                 * reach the login page.
                 *
                 * Therefore /login must be permitted to everyone.
                 */
                .requestMatchers("/login").permitAll()
                //.requestMatchers("/players/**").permitAll() if you do this no authentication(login)


                /*
                 * Our CSS files are static resources.
                 *
                 * They are located under:
                 *
                 *     src/main/resources/static/css/
                 *
                 * and are accessed through URLs such as:
                 *
                 *     /css/style.css
                 *
                 * We allow everyone to access CSS so that the login
                 * page can be styled even before authentication.
                 */
                .requestMatchers("/css/**","/js/**","/images/**").permitAll()


                /*
                 * This is one of the MOST IMPORTANT lines.
                 *
                 * "/**" conceptually means all remaining requests.
                 *
                 * authenticated() means:
                 *
                 *     The user MUST be logged in.
                 *
                 * Therefore:
                 *
                 *     /contributorsList       → protected
                 *     /                     → protected
                 *     /contributors          → protected
                 *     /contributors/1/edit  → protected
                 *     /change-password       → protected
                 *
                 * unless we explicitly make another URL public.
                 *
                 * This gives us a secure default:
                 *
                 *     "Everything is protected unless I explicitly
                 *      say otherwise."
                 */
                .anyRequest().authenticated()
        );


        /*
         * ============================================================
         * 3B. FORM LOGIN
         * ============================================================
         *
         * We are using a normal HTML login form.
         *
         * Spring Security handles the authentication process for us.
         */
        http.formLogin(form -> form


                /*
                 * Tell Spring Security that our custom login page is:
                 *
                 *     /login
                 *
                 * This means we will create:
                 *
                 *     login.html
                 *
                 * and a controller mapping for /login.
                 */
                .loginPage("/login")


                /*
                 * After successful authentication, send the Treasurer
                 * to:
                 *
                 *     /contributorsList
                 *
                 * The 'true' means always use this URL after successful
                 * login instead of trying to restore the page that was
                 * originally requested.
                 *
                 * Example:
                 *
                 * Treasurer visits /contributorsList
                 *        ↓
                 * Not logged in
                 *        ↓
                 * Spring Security sends them to /login
                 *        ↓
                 * Login succeeds
                 *        ↓
                 * Redirect to /contributorsList
                 */
                .defaultSuccessUrl("/dashboard",true)


                /*
                 * Allow everyone to access the login functionality.
                 *
                 * This is important because an unauthenticated user
                 * needs to be able to submit the login form.
                 */
                .permitAll()
        );


        /*
         * ============================================================
         * 3C. LOGOUT
         * ============================================================
         *
         * Spring Security can also handle logout for us.
         */
        http.logout(logout -> logout


                /*
                 * After logout, send the user back to:
                 *
                 *     /login?logout
                 *
                 * The "?logout" is a query parameter.
                 *
                 * We can use it in login.html to display something
                 * such as:
                 *
                 *     "You have been logged out."
                 */
                .logoutSuccessUrl("/login?logout")


                /*
                 * Logout functionality itself should be available
                 * to authenticated users without requiring another
                 * permission rule.
                 */
                .permitAll()
        );


        /*
         * Build the complete security configuration.
         *
         * Spring Security uses the resulting SecurityFilterChain
         * to protect incoming HTTP requests.
         */
        return http.build();
    }
}

