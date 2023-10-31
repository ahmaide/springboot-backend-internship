package com.ahmaide.trainingtaskmongo.services;

import com.ahmaide.trainingtaskmongo.config.TokenExtractorFilter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Service
public class HttpService {

    private static final String USER_SERVICE_URL = "/api/user-service/register";
    private static final String TASK_SERVICE_URL = "/api/user-service/authenticate";

    private static final String LOGOUT_URL = "/logout";

    private static final String LOGOUT_SUCCESSFUL_URL = "/authenticate";

     public void setPermissions(HttpSecurity http) throws Exception {
         http.authorizeRequests(authorizeRequests -> authorizeRequests
                 .requestMatchers(USER_SERVICE_URL, TASK_SERVICE_URL)
                 .permitAll()
                 .anyRequest()
                 .authenticated()
         );
     }

     public void setLogoutRequest(HttpSecurity http) throws Exception {
         http.logout(logout -> logout
                 .logoutUrl(LOGOUT_URL)
                 .logoutSuccessUrl(LOGOUT_SUCCESSFUL_URL)
                 .invalidateHttpSession(true)
         );
     }


     public void addExtraConfigurations(HttpSecurity http, TokenExtractorFilter tokenExtractorFilter) throws Exception {
         http.sessionManagement(sessionManagment -> sessionManagment
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

         http.addFilterBefore(tokenExtractorFilter, UsernamePasswordAuthenticationFilter.class);
         http.httpBasic(Customizer.withDefaults());
         http.csrf(AbstractHttpConfigurer::disable);
     }

}
