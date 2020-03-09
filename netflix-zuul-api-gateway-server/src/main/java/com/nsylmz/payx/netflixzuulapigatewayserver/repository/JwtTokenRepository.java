package com.nsylmz.payx.netflixzuulapigatewayserver.repository;

import com.nsylmz.payx.netflixzuulapigatewayserver.model.JwtToken;
import org.springframework.data.repository.CrudRepository;

public interface JwtTokenRepository extends CrudRepository<JwtToken, String>  {

}
