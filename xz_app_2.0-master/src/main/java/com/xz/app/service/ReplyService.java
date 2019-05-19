package com.xz.app.service;

import java.util.List;

import com.xz.app.forepojo.ForeAnswer;
import com.xz.app.forepojo.ForeUsers;
import com.xz.app.pojo.Questreply;

public interface ReplyService {
	
	//@DataSource("slave")
	void add(Questreply reply);
	
	//@DataSource("slave")
	void update(Questreply Questreply);
	
	//@DataSource("master")
	List<Questreply> getByQuesId(long questid);
	
	//@DataSource("master")
	Questreply getByAnswerId(long answerid);
	
	//@DataSource("master")
	List<ForeAnswer> full(List<Questreply> list) throws Exception;
	
	//@DataSource("master")
	ForeAnswer full(ForeUsers foreUsers, ForeAnswer answer, Questreply Questreply) throws Exception;

    void deleteByQuestid(long questid);
}
