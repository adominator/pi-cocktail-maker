package net.alex9849.cocktailmaker.payload.response;

import net.alex9849.cocktailmaker.payload.dto.UserDto;

import java.util.Date;

public class JwtResponse {
    private String accessToken;
    private Date tokenExpiration;
    private String tokenType = "Bearer";
    private UserDto user;

    public JwtResponse(String accessToken, Date tokenExpiration, UserDto user) {
        this.accessToken = accessToken;
        this.tokenExpiration = tokenExpiration;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Date tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}