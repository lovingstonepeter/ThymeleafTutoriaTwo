package com.example.thymeleaftutorialtwo.dto;


/*
 * ================================================================
 * CHANGE PASSWORD REQUEST DTO
 * ================================================================
 *
 * DTO stands for:
 *
 *     Data Transfer Object
 *
 * This class is used to carry data from our HTML form
 * to our ChangePasswordController.
 *
 * It is NOT a database entity.
 *
 * It does not represent a table in PostgreSQL.
 *
 * Its purpose is simply to carry the information needed
 * for a password-change request.
 */
public class ChangePasswordRequestDTO {


    /*
     * The user's CURRENT password.
     *
     * Example:
     *
     *     treasurer123
     *
     * The controller will use this value to verify that
     * the person knows their existing password.
     */
    private String currentPassword;


    /*
     * The NEW password that the user wants to set.
     *
     * Example:
     *
     *     MyNewPassword456
     */
    private String newPassword;


    /*
     * The user enters the new password a second time.
     *
     * This allows us to check that the user did not accidentally
     * make a typing mistake when entering the new password.
     */
    private String confirmPassword;


    /*
     * ================================================================
     * EMPTY CONSTRUCTOR
     * ================================================================
     *
     * Spring/Thymeleaf needs to be able to create an empty DTO
     * when displaying the form.
     *
     * For example:
     *
     *     new ChangePasswordRequestDTO()
     */
    public ChangePasswordRequestDTO() {
    }


    /*
     * ================================================================
     * GETTERS AND SETTERS
     * ================================================================
     *
     * Getters allow the controller to READ the values.
     *
     * Setters allow Spring/Thymeleaf to PUT submitted form values
     * into the DTO.
     *
     * For example, when the form submits:
     *
     *     currentPassword = "treasurer123"
     *
     * Spring can effectively call:
     *
     *     setCurrentPassword("treasurer123")
     */


    public String getCurrentPassword() {
        return currentPassword;
    }


    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }


    public String getNewPassword() {
        return newPassword;
    }


    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }


    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

