package bsoft.nl.waardelijst.security.configuration;

import bsoft.nl.waardelijst.database.repository.UserRepo;
import bsoft.nl.waardelijst.services.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new MyUserDetailsService(userRepository);
    }
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        /*
        auth.inMemoryAuthentication()
                .withUser("testuser").password("12345").roles("USER").and()
                .withUser("admin").password("admin").roles("USER", "ADMIN");
        */
        auth.userDetailsService(userDetailsServiceBean());

        logger.info("Configured in memory security");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/console/**").authenticated()
                .antMatchers("/users/authenticate").permitAll()
                .antMatchers("/users/**").authenticated()
                .antMatchers("/waardelijst/**").authenticated()
                .antMatchers("/waardelijsten/**").authenticated()
                .and().httpBasic();
        ;
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        logger.info("Configured httpsecurity");
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        logger.info("Created passwordEncoder()");
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
