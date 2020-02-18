package com.nsylmz.payx.netflixzuulapigatewayserver.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityResource {
	
	@Autowired
	private SecurityService securityService;
	
	@PostMapping("/signIn")
    @ResponseBody
    public ResponseEntity<SecurityResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = securityService.login(loginRequest.getUsername(),loginRequest.getPassword());
        HttpHeaders headers = new HttpHeaders();
        List<String> headerlist = new ArrayList<>();
        List<String> exposeList = new ArrayList<>();
        headerlist.add("Content-Type");
        headerlist.add(" Accept");
        headerlist.add("X-Requested-With");
        headerlist.add("Authorization");
        headers.setAccessControlAllowHeaders(headerlist);
        exposeList.add("Authorization");
        headers.setAccessControlExposeHeaders(exposeList);
        headers.set("Authorization", token);
        return new ResponseEntity<SecurityResponse>(new SecurityResponse(token), headers, HttpStatus.CREATED);
    }
    
	@PostMapping("/signOut")
    @ResponseBody
    public ResponseEntity<SecurityResponse> logout(@RequestHeader(value="Authorization") String token) {
        HttpHeaders headers = new HttpHeaders();
      if (securityService.logout(token)) {
          headers.remove("Authorization");
          return new ResponseEntity<SecurityResponse>(new SecurityResponse("Logged out"), headers, HttpStatus.CREATED);
      }
        return new ResponseEntity<SecurityResponse>(new SecurityResponse("Logout Failed"), headers, HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/valid/token")
    @ResponseBody
    public Boolean isValidToken (@RequestHeader(value="Authorization") String token) {
        return true;
    }


    @PostMapping("/signIn/token")
    @ResponseBody
    public ResponseEntity<SecurityResponse> createNewToken(@RequestHeader(value="Authorization") String token) {
        String newToken = securityService.createNewToken(token);
        HttpHeaders headers = new HttpHeaders();
        List<String> headerList = new ArrayList<>();
        List<String> exposeList = new ArrayList<>();
        headerList.add("Content-Type");
        headerList.add(" Accept");
        headerList.add("X-Requested-With");
        headerList.add("Authorization");
        headers.setAccessControlAllowHeaders(headerList);
        exposeList.add("Authorization");
        headers.setAccessControlExposeHeaders(exposeList);
        headers.set("Authorization", newToken);
        return new ResponseEntity<SecurityResponse>(new SecurityResponse(newToken), headers, HttpStatus.CREATED);
    }

}
