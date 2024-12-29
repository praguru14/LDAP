package org.pg.ldap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class LDAPSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()
                )
                .formLogin();
        return http.build();
    }
    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        DefaultLdapAuthoritiesPopulator authoritiesPopulator =
                new DefaultLdapAuthoritiesPopulator(contextSource(), null); // Configure null or a search base
        authoritiesPopulator.setIgnorePartialResultException(true);

        return new LdapAuthenticationProvider(bindAuthenticator(), authoritiesPopulator);
    }

    @Bean
    public BindAuthenticator bindAuthenticator() {
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource());
        bindAuthenticator.setUserDnPatterns(new String[] {"uid={0},ou=users"}); // Adjust the DN pattern as per your setup
        return bindAuthenticator;
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://your-ldap-server");
        contextSource.setBase("dc=example,dc=com"); // Base DN
        contextSource.setUserDn("your-user-dn");    // Optional
        contextSource.setPassword("your-password"); // Optional
        return contextSource;
    }
}
