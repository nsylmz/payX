package com.nsylmz.payx.account.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.nsylmz.payx.account.model.AccountNumberSequence;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGenerator {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public long generateAccountNumberSequence(String seqName) {
	    AccountNumberSequence counter = mongoOperations.findAndModify(
	    		query(where("_id").is(seqName)),
	    		new Update().inc("seq",1), 
	    		options().returnNew(true).upsert(true),
	    		AccountNumberSequence.class);
	    return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

}
