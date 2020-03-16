// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.sys;

import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ComponentScan({ "com.attendance" })
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private DataSource dataSource;
    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;
    
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().usersByUsernameQuery(this.usersQuery).authoritiesByUsernameQuery(this.rolesQuery).dataSource(this.dataSource).passwordEncoder((PasswordEncoder)this.bCryptPasswordEncoder);
    }
    

    

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
	
		 http.authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
		   http.
           authorizeRequests()
           .antMatchers("/").permitAll()
           .antMatchers("/add/**").permitAll()
           .antMatchers("/new/dist/**").permitAll()
           .antMatchers("/new/dist/img/**").permitAll()
           .antMatchers("/new/vendors/**").permitAll()
           .antMatchers("/login").permitAll()
         
          // .antMatchers("/admin/dashboard/**").permitAll()
           .antMatchers("/admin").hasAnyRole("SystemAdministrator", "Manager", "Head of Department", "Clerk" ).anyRequest()
           .authenticated().and().csrf().disable().formLogin()
           .loginPage("/login").failureUrl("/login?error=true")
        //  .defaultSuccessUrl("/admin/my/profile")
           .successHandler(successHandler())
           .usernameParameter("email")
           .passwordParameter("password")
           .and().logout()
           .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
           .logoutSuccessUrl("/login").and().exceptionHandling()
           .accessDeniedPage("/html/404");
	}

    
    public void configure(final WebSecurity web) throws Exception {
    //    web.ignoring().antMatchers(new String[] { "/resources/**", "/assets/**", "/static/**", "/css/**", "/js/**", "/images/**" });
        web.ignoring().antMatchers(new String[] { "/resources/**", "/new/vendors/****","/new/dist/****","**/dist/img/**", "/new/src/***", "/assets/**", "/static/**", "/css/**", "/js/**", "/images/**" });

    
    }
    
    @Bean
    public RoleAuthenticationSuccess successHandler() {
        return new RoleAuthenticationSuccess();
    }
}
