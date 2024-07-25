package me.dio.innovation.one.security;

import me.dio.innovation.one.domain.model.Role;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JWTObject {

    private Long id;
    private String subject; //nome do usuário
    private Date issuedAt; //data de criação do token
    private Date expiration; //data de expiração do token
    private List<Role> roles; //perfis de acesso

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setRoles(Role... roles) {
        this.roles = Arrays.asList(roles);
    }
}
