package learn.capstone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }


    // This method allows configuring web based security for specific http requests.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // we are using HTML forms in our app
        //but disable CSRF (Cross Site Request Forgery) for now
      http.csrf().disable();

        // this configures Spring Security to allow
        //CORS related requests (such as preflight checks)
        http.cors();

        // the order of the antMatchers() method calls is important
        // as they're evaluated in the order that they're added
        http.authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/create_account").permitAll()
                // Allow refresh token for authenticated users
                .antMatchers("/refresh_token").authenticated()
                //Getting all books for mybookslist for user and admin
                .antMatchers(HttpMethod.GET,
                        "/books", "/books/*").hasAnyRole("USER", "ADMIN")
                //Getting all books from the books table for admin view
                .antMatchers(HttpMethod.GET, "/booksAdmin", "/booksAdmin/*").hasAnyRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/authorBooks").hasAnyRole("USER", "ADMIN")

                //This post request is for adding a book from mybookslist to the books table, even
                //if the information is incorrect
                .antMatchers(HttpMethod.POST,
                        "/booksAdmin").hasAnyRole("USER", "ADMIN")

                //Adding an association between the user and the book that they added
                .antMatchers(HttpMethod.POST, "/books").hasAnyRole("USER", "ADMIN")
                //Purpose: Update completion status for a user book
                .antMatchers(HttpMethod.PUT,
                        "/booksUser/*").hasAnyRole("USER")
                //Purpose: Update incorrect information in the books table
                .antMatchers(HttpMethod.PUT, "/booksAdmin/*").hasAnyRole("ADMIN")
                //Deleting a book from MybooksList
                .antMatchers(HttpMethod.DELETE,
                        "/books/*").hasAnyRole("USER", "ADMIN")
                .antMatchers("/**").denyAll()
                // if we get to this point, let's deny all requests
                .and()
                // new ...
                .addFilter(new JwtRequestFilter(authenticationManager(), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //This bean is injected into JwtRequestFilter
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        // Configure CORS globally versus
        // controller-by-controller.
        // Can be combined with @CrossOrigin.
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //Mapping encompasses /books and /books/[whatever]
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }

}
