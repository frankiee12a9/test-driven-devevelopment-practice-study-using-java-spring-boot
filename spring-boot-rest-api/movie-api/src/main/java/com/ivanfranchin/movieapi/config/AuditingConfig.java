package com.ivanfranchin.movieapi.config;

import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    //! enable this case: java.lang.ClassCastException: class com.ivanfranchin.movieapi.security.CustomUserDetails cannot be cast to class java.nio.file.attribute.UserPrincipal
    // @Bean
	// public AuditorAware<Long> auditorProvider() {
	// 	return new SpringSecurityAuditAwareImpl();
	// }
    // @Bean
	// public AuditorAware<String> auditorProvider() {
	// 	return new SpringSecurityAuditAwareImpl();
	// }
}

// class SpringSecurityAuditAwareImpl implements AuditorAware<String> {

	// @Override
	// public Optional<String> getCurrentAuditor() {
	// 	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	// 	if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
	// 		return Optional.empty();
	// 	}

	// 	UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

	// 	// return Optional.ofNullable(userPrincipal.getId());
	// 	return Optional.ofNullable(userPrincipal.getName());
	// }
// }
