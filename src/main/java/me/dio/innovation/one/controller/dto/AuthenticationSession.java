package me.dio.innovation.one.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User authentication session")
public class AuthenticationSession {

    @Schema(description = "Username of the authenticated user", example = "user")
    private String login;

    @Schema(description = "JWT token for the session", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
