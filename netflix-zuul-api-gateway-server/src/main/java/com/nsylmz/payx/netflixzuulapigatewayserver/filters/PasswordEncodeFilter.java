package com.nsylmz.payx.netflixzuulapigatewayserver.filters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.nsylmz.payx.netflixzuulapigatewayserver.exception.ExceptionResponse;
import com.nsylmz.payx.netflixzuulapigatewayserver.proxy.SignUpBean;

@Component
public class PasswordEncodeFilter extends ZuulFilter {
	
	private final static String SERVICE_ID_KEY = "serviceId";
	private final static String SERVICE_ID_VALUE = "user-service";
	private final static String REQUEST_URI_KEY = "requestURI";
	private final static String REQUEST_URI_VALUE = "/signUp";
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ValidatorFactory validatorFactory; 
	
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean shouldFilter() {
		return RequestContext.getCurrentContext().get(SERVICE_ID_KEY).equals(SERVICE_ID_VALUE) 
				&& RequestContext.getCurrentContext().get(REQUEST_URI_KEY).equals(REQUEST_URI_VALUE);
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		try {
			InputStream in = (InputStream) context.get("requestEntity");
			if (in == null) {
				in = context.getRequest().getInputStream();
				String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
				ObjectReader reader = objectMapper.readerFor(SignUpBean.class);
				SignUpBean signUp = reader.readValue(body);
				validateSingUpBean(signUp);
				signUp.setPassword(passwordEncoder.encode(signUp.getPassword()));
				ObjectWriter objectWriter = objectMapper.writerFor(SignUpBean.class);
				context.setRequest(prepareNewRequest(context, objectWriter.writeValueAsBytes(signUp)));
			} else {
				String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
				ObjectReader reader = objectMapper.readerFor(SignUpBean.class);
				SignUpBean signUp = reader.readValue(body);
				validateSingUpBean(signUp);
				signUp.setPassword(passwordEncoder.encode(signUp.getPassword()));
				ObjectWriter objectWriter = objectMapper.writerFor(SignUpBean.class);
				context.set("requestEntity", new ByteArrayInputStream(objectWriter.writeValueAsBytes(signUp)));
			}
			
		} catch (ConstraintViolationException cve) {
			context.setResponseStatusCode(500);
			context.getResponse().setContentType("application/json");
			ObjectWriter objectWriter = objectMapper.writerFor(ExceptionResponse.class);
			ExceptionResponse response = new ExceptionResponse();
			response.setTimestamp(new Date());
			response.setMessage("Validation Failed!!");
			response.setDetails(cve.getMessage());
			try {
				context.set("responseEntity", new ByteArrayInputStream(objectWriter.writeValueAsString(response).getBytes("UTF-8")));
			} catch (UnsupportedEncodingException uee) {
				throw new ZuulException(uee.getCause(), uee.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, "Can't Parse Request Body!!!");
			} catch (JsonProcessingException jpe) {
				throw new ZuulException(jpe.getCause(), jpe.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR, "Can't Parse Request Body!!!");
			}
		} catch (IOException e) {
			throw new ZuulException(e.getCause(), e.getMessage(), HttpStatus.SC_BAD_REQUEST, "Can't Parse Request Body!!!");
		} 
		return null;
	}
	
	private void validateSingUpBean(SignUpBean signUpBean) throws ConstraintViolationException {
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(signUpBean);
	    if (!violations.isEmpty()) {
	    	throw new ConstraintViolationException(violations);
	    }
	}
	
	private HttpServletRequest prepareNewRequest(RequestContext context, byte[] requestEntityBytes) {
		return new HttpServletRequestWrapper(context.getRequest()) {
			@Override
            public ServletInputStream getInputStream() throws IOException {
				return new ServletInputStreamWrapper(requestEntityBytes);
            }
			@Override
            public int getContentLength() {
				return requestEntityBytes.length;
            }
            @Override
            public long getContentLengthLong() {
            	return requestEntityBytes.length;
            }
        };
	}
	
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 10;
	}
}
