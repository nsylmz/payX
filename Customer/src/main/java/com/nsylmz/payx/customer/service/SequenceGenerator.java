package com.nsylmz.payx.customer.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.nsylmz.payx.customer.model.CustomerNumberSequence;

@Service
public class SequenceGenerator {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public long generateAccountNumberSequence(String seqName) {
	    CustomerNumberSequence counter = mongoOperations.findAndModify(
	    		query(where("_id").is(seqName)),
	    		new Update().inc("seq",1), 
	    		options().returnNew(true).upsert(true),
	    		CustomerNumberSequence.class);
	    return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

}
