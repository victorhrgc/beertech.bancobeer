package beertech.becks.api.security.filter;

import beertech.becks.api.security.service.JwtService;
import beertech.becks.api.security.service.UserSecurityService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

	JwtService jwtService;
	UserSecurityService userService;

	public JwtAuthFilter(JwtService jwtService, UserSecurityService userService) {
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		String authorization = httpServletRequest.getHeader("Authorization");

		if (authorization != null && authorization.startsWith("Bearer")) {
			String token = authorization.split(" ")[1];

			if (jwtService.isValidToken(token)) {
				String userLogin = jwtService.getUserLogin(token);

				UserDetails user = userService.loadUserByUsername(userLogin);

				UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(user,
						null, user.getAuthorities());

				userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

				SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
			}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
}