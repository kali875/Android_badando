package com.example.beadando.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private com.example.beadando.ui.login.LoggedInUserView success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable com.example.beadando.ui.login.LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    com.example.beadando.ui.login.LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}