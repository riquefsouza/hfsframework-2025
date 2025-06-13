package br.com.hfs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import br.com.hfs.base.security.BaseAccessDeniedHandler;

@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

	//@Autowired
	//private HfsUserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(c -> c.disable())
        //.cors(cors -> cors
        //		.configurationSource(corsConfigurationSource())
        //)
        .authorizeHttpRequests(authorize -> authorize
        		.requestMatchers("/fontawesome-free-6.7.2-web/**","/bootstrap-5.0.1/**", "/popper-2.9.2/**", 
        				"/fontawesome-free/**", "/jquery-3.6.0/**", "/dataTables-1.10.24/**", 
        				"/css/**", "/img/**", "/js/**").permitAll()
        		.requestMatchers("/public/**").permitAll()
        		.requestMatchers("/private/**").hasAnyRole("USER","ADMIN")
        		.requestMatchers("/login*").permitAll()        		
                .anyRequest().authenticated()                
        )
        //.httpBasic(Customizer.withDefaults())
        .formLogin(form -> form
    			.loginPage("/login.html")
    			.permitAll()
    			.defaultSuccessUrl("/home", true)
    			.failureUrl("/login-error.html")    			
    	)        		
		.logout(out -> out.logoutSuccessUrl("/index.html"))		
		.exceptionHandling(ex -> ex
				.accessDeniedHandler(accessDeniedHandler())
		);
        //.sessionManagement(session ->
        //   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //);        
		return http.build();
	}
	
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new BaseAccessDeniedHandler();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();    
    }

/*	
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*"));
        //configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200","http://localhost:8000"));  
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        //configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "OPTIONS", "DELETE", "PATCH"));
        //configuration.setAllowedHeaders(
            //Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"));
        //in case authentication is enabled this flag MUST be set, otherwise CORS requests will fail
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.withUsername("admin")
            .password(encoder.encode("abcd1234"))
            .roles("USER","ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }	


	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailsService;
	} 

	@Bean
	public AuthenticationManager configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
		
		return auth.build();
	}

*/
}
