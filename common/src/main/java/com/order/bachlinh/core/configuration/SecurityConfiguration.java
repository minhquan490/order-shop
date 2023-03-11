package com.order.bachlinh.core.configuration;

import com.order.bachlinh.core.entities.model.Role;
import com.order.bachlinh.core.security.entry.AccessDeniedEntryPoint;
import com.order.bachlinh.core.security.filter.AuthenticationFilter;
import com.order.bachlinh.core.security.filter.ClientSecretFilter;
import com.order.bachlinh.core.security.filter.LoggingRequestFilter;
import com.order.bachlinh.core.security.handler.ClientSecretHandler;
import com.order.bachlinh.core.security.token.internal.TokenManagerProvider;
import com.order.bachlinh.core.security.token.spi.JwtDecoderFactory;
import com.order.bachlinh.core.security.token.spi.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Internal security configuration use in this project.
 *
 * @author Hoang Minh Quan
 * */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true, mode = AdviceMode.ASPECTJ)
@PropertySource("classpath:common.properties")
@Lazy
class SecurityConfiguration {
    private String urlContentBase;
    private String urlCustomer;
    private String urlAdmin;
    private String clientUrl;
    private String secretKey;
    private String loginUrl;
    private String registerUrl;
    private String homeUrl;
    private String resourceUrl;

    private ApplicationContext applicationContext;

    @Bean
    @Lazy
    AuthenticationFilter authenticationFilter() {
        Collection<String> urls = Arrays.asList(urlContentBase, loginUrl, registerUrl, homeUrl, resourceUrl);
        return new AuthenticationFilter(applicationContext, urls);
    }

    @Bean
    @Lazy
    LoggingRequestFilter loggingRequestFilter() {
        return new LoggingRequestFilter(applicationContext, clientUrl);
    }

    @Bean
    @Lazy
    ClientSecretFilter clientSecretFilter() {
        return new ClientSecretFilter(applicationContext, clientUrl);
    }

    @Bean
    @Lazy
    DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> {
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                    csrf.ignoringRequestMatchers(loginUrl, registerUrl, homeUrl, resourceUrl);
                })
                .cors(cors -> cors.configurationSource(corsConfigurationSource(clientUrl)))
                .anonymous()
                .disable()
                .formLogin()
                .disable()
                .logout()
                .disable()
                .httpBasic()
                .disable()
                .headers()
                .cacheControl()
                .disable()
                .and()
                .requiresChannel()
                .anyRequest()
                .requiresSecure()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(urlAdmin).hasAuthority(Role.ADMIN.name())
                .requestMatchers(urlCustomer).hasAnyAuthority(Role.ADMIN.name(), Role.CUSTOMER.name())
                .requestMatchers(urlContentBase, loginUrl, registerUrl, homeUrl, resourceUrl).permitAll()
                .and()
                .addFilterBefore(loggingRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(clientSecretFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedEntryPoint())
                .and()
                .build();
    }

    @Bean
    @Lazy
    TokenManager tokenManager() {
        return new TokenManagerProvider(JwtDecoderFactory.Builder.SHA256_ALGORITHM, secretKey, applicationContext)
                .getTokenManager();
    }

    @Bean
    @Lazy
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(20);
    }

    @Bean
    @Lazy
    ClientSecretHandler clientSecretHandler() {
        return new ClientSecretHandler();
    }

    private CorsConfigurationSource corsConfigurationSource(String clientUrl) {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.addAllowedOrigin(clientUrl);

        corsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return corsConfigurationSource;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Value("${secret.key}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${shop.client.url}")
    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    @Value("${shop.url.base}")
    public void setUrlContentBase(String urlContentBase) {
        this.urlContentBase = urlContentBase;
    }

    @Value("${shop.url.customer}")
    public void setUrlCustomer(String urlCustomer) {
        this.urlCustomer = urlCustomer;
    }

    @Value("${shop.url.admin}")
    public void setUrlAdmin(String urlAdmin) {
        this.urlAdmin = urlAdmin;
    }

    @Value("${shop.url.home}")
    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    @Value("${shop.url.login}")
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Value("${shop.url.register}")
    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    @Value("${shop.url.resource}")
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
