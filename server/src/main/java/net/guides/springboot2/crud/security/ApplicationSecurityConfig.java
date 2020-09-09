package net.guides.springboot2.crud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/", "index", "/css/*", "/js*").permitAll()
                .antMatchers(HttpMethod.DELETE, "/persoana/**")
                .hasAuthority(ApplicationUserPermission.PERSOANA_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/persoana/**")
                .hasAuthority(ApplicationUserPermission.PERSOANA_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/persoana/**")
                .hasAuthority(ApplicationUserPermission.PERSOANA_WRITE.getPermission()).anyRequest().authenticated()
                .and().httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails bogdanBisUser = User.builder().username("bogdanbis").password(passwordEncoder.encode("password"))
                .roles(ApplicationUserRole.STUDENT.name())
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities()).build();

        UserDetails alexPopUser = User.builder().username("alexpop").password(passwordEncoder.encode("password"))
                .roles(ApplicationUserRole.ADMIN.name()).authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails trainee = User.builder().username("trainee").password(passwordEncoder.encode("password"))
                .roles(ApplicationUserRole.STUDENTTRAINEE.name())
                .authorities(ApplicationUserRole.STUDENTTRAINEE.getGrantedAuthorities()).build();

        return new InMemoryUserDetailsManager(bogdanBisUser, alexPopUser, trainee);
    }
}
