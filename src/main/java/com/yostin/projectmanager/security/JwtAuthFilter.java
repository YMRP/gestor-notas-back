package com.yostin.projectmanager.security;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/auth");
    }

    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 1️⃣ Si no hay token, sigue sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);

            // ESTA VAINA (RETURN) SOLUCIONA EL ERROR DE INCOMPLETE_CHUNKED_ENCODING 200
            // (OK)
            // El error NullPointerException en JwtAuthFilter se debe a que el filtro
            // intenta ejecutar authHeader.substring(7) incluso cuando authHeader es null.
            // Esto ocurre porque después de filterChain.doFilter(request, response) no se
            // detiene la ejecución del método.
            return;
        }

        // EXTRAYENDO EL TOKJEN
        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // extraer usuario emial
        String email = jwtService.extractEmail(token);

        // 5️⃣ Autenticar en Spring AQUI DEFINO QUE EL EMAIL ES EL QUE VALE
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 6️⃣ Continuar
        filterChain.doFilter(request, response);

    }
}
