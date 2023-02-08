package com.ivanfranchin.movieapi.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ivanfranchin.movieapi.security.CustomUserDetails;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
    @Bean
	public AuditorAware<String> auditorProvider() {
		return new SpringSecurityAuditAwareImpl();
	}
}

class SpringSecurityAuditAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) 
			return Optional.empty();

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		return Optional.ofNullable(userDetails.getUsername());
	}
}
