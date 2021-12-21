package com.example.tegnelabben.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Class for filtering authentication with jwt
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  @Autowired
  private AdminDetailsService adminDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * Method for filtering authentication requests with jwt
   * @param httpServletRequest
   * @param httpServletResponse
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    String authorisationHeader = httpServletRequest.getHeader("Authorization");

    String username = null;
    String jwt = null;

    if(authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {
      jwt = authorisationHeader.substring(7);
      try {
        username = jwtUtil.extractUsername(jwt);
      } catch (Exception ignored) { }
    }

    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.adminDetailsService.loadUserByUsername(username);
      if(jwtUtil.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
