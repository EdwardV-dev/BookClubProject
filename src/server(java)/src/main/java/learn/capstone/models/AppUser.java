package learn.capstone.models;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//Roles are not required as a model object
public class AppUser extends User {

    private static final String AUTHORITY_PREFIX = "ROLE_";

    private int appUserId;

    private List<AppUserBooks> books = new ArrayList<>();


    public AppUser(int appUserId, String username, String password, boolean disabled, List<String> roles) {

        //initializing child by first initializing parent via super. Sending in disabled as a false boolean (i.e. not disabled aka isEnabled).
        //username and password and authorities are available through inheritance (getters) after initialization
        super(username, password, !disabled,
                true, true, true, convertRolesToAuthorities(roles));
        this.appUserId = appUserId;
    }

    private List<String> roles = new ArrayList<>();

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }


    public static List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (String role : roles) {
            Assert.isTrue(!role.startsWith(AUTHORITY_PREFIX),
                    () -> String.format("%s cannot start with %s (it is automatically added)", role, AUTHORITY_PREFIX));
            authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role));
        }
        return authorities;
    }

    public static List<String> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(a -> a.getAuthority().substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }
}
