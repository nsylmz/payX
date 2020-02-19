package com.nsylmz.payx.userservice.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Document
@CompoundIndex(def = "{'roleId':1, 'userId':1}", unique = true, name = "user_role_index")
public class UserRole {
	
	private String id;
	
	private String roleId;
	
	private String userId;

}
