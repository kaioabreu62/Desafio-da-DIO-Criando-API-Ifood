package me.dio.innovation.one.security;

import io.jsonwebtoken.*;
import me.dio.innovation.one.domain.model.Role;

import java.util.List;
import java.util.stream.Collectors;

public class JWTCreator {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ROLES_AUTHORITIES = "authorities";

    public static String create(String prefix, String key, JWTObject jwtObject) {
        String token = Jwts.builder()
                .setSubject(jwtObject.getSubject())
                .setIssuedAt(jwtObject.getIssuedAt())
                .setExpiration(jwtObject.getExpiration())
                .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return prefix + " " + token;
    }

    public static JWTObject create(String token, String prefix, String key)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        JWTObject object = new JWTObject();
        token = token.replace(prefix, "").trim(); // Remove o prefixo e quaisquer espaços extras
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        object.setSubject(claims.getSubject());
        object.setExpiration(claims.getExpiration());
        object.setIssuedAt(claims.getIssuedAt());

        List<String> roles = claims.get(ROLES_AUTHORITIES, List.class);
        object.setRoles(roles.stream().map(Role::new).collect(Collectors.toList())); // Ajustar se necessário para converter strings em objetos Role

        return object;
    }

    private static List<String> checkRoles(List<Role> roles) {
        return roles.stream()
                .map(role -> "ROLE_".concat(role.getName().replace("ROLE_", "")))
                .collect(Collectors.toList());
    }
}
