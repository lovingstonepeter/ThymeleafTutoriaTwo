/*

* ================================================================
* PASSWORD VISIBILITY TOGGLE
* ================================================================
*
* This function changes a password input between:
*
*
  type="password" → password is hidden

*
* and:
*
*
  type="text" → password is visible

*
* The function receives the ID of the password input that
* should be controlled.
  */

function togglePassword(inputId) {


/*
 * Find the password input using its HTML ID.
 *
 * Example:
 *
 *     togglePassword("password")
 *
 * searches for:
 *
 *     id="password"
 */
const passwordInput = document.getElementById(inputId);


/*
 * Find the button that called this function.
 *
 * event.currentTarget refers to the button that was clicked.
 *
 * This allows us to change the button text from:
 *
 *     Show Password
 *
 * to:
 *
 *     Hide Password
 */
const toggleButton = event.currentTarget;


/*
 * Safety check.
 *
 * If the password input cannot be found, stop the function.
 */
if (!passwordInput) {
    return;
}


/*
 * Check whether the password is currently hidden.
 */
if (passwordInput.type === "password") {

    /*
     * Change the input type from:
     *
     *     password
     *
     * to:
     *
     *     text
     *
     * The password will now be visible.
     */
    passwordInput.type = "text";


    /*
     * Change the button text so the user knows
     * what clicking it again will do.
     */
    toggleButton.textContent = "Hide Password";


} else {

    /*
     * Change the input type back to:
     *
     *     password
     *
     * The password becomes hidden again.
     */
    passwordInput.type = "password";


    /*
     * Change the button text back.
     */
    toggleButton.textContent = "Show Password";
}


}
