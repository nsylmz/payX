package com.nsylmz.payx.netflixzuulapigatewayserver.security;

import com.nsylmz.payx.netflixzuulapigatewayserver.exception.AuthException;
import com.nsylmz.payx.netflixzuulapigatewayserver.proxy.UserServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SecurityService implements UserDetailsService {

	
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserServiceProxy userServiceProxy;
    
    public String login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            List<String> userRoles = userServiceProxy.getUserRolesByEmail(username);
            if (userRoles == null || userRoles.size() == 0 || userRoles.get(0) == null) {
                throw new AuthException("Invalid username or password.");
            }
            //NOTE: normally we dont need to add "ROLE_" prefix. Spring does automatically for us.
            //Since we are using custom token using JWT we should add ROLE_ prefix
            String token =  jwtTokenProvider.createToken(username, userRoles.stream()
                    .map((String role)-> "ROLE_"+role).filter(Objects::nonNull).collect(Collectors.toList()));
            return token;

        } catch (AuthenticationException e) {
            throw new AuthException("Invalid username or password.");
        }
    }

    public boolean logout(String token) {
    	userServiceProxy.removeToken(token);
    	return true;
    }

    public Boolean isValidToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public String createNewToken(String token) {
        String username = jwtTokenProvider.getUsername(token);
        List<String>roleList = jwtTokenProvider.getRoleList(token);
        String newToken =  jwtTokenProvider.createToken(username,roleList);
        return newToken;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new PayXUserDetails(userServiceProxy.getUserByEmail(username).getBody());
	}
}
