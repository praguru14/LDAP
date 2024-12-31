package org.pg.ldap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                .formLogin().and()
                .csrf().disable();
        return http.build();
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        DefaultLdapAuthoritiesPopulator authoritiesPopulator =
                new DefaultLdapAuthoritiesPopulator(contextSource(), null);
        authoritiesPopulator.setIgnorePartialResultException(true);
        return new LdapAuthenticationProvider(bindAuthenticator(), authoritiesPopulator);
    }

    @Bean
    public BindAuthenticator bindAuthenticator() {
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource());
      bindAuthenticator.setUserDnPatterns(new String[]{"uid={0}"});
        return bindAuthenticator;
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://ldap.forumsys.com:389"); // LDAP server URL with port
        contextSource.setBase("dc=example,dc=com");           // Base DN for LDAP
        return contextSource;
    }
}
