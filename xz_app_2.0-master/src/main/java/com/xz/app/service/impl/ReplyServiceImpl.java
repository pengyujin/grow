package com.xz.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.xz.app.exception.XZException;
import com.xz.app.pojo.Questreply;
import com.xz.app.pojo.QuestreplyExample;
import com.xz.app.pojo.ReplylikeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xz.app.forepojo.ForeAnswer;
import com.xz.app.forepojo.ForeUsers;
import com.xz.app.mapper.QuestreplyMapper;
import com.xz.app.service.ReplyLikeService;
import com.xz.app.service.ReplyService;
import com.xz.app.service.UserService;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	QuestreplyMapper QuestreplyMapper;
	@Autowired
	UserService userService;
	@Autowired
	ReplyLikeService  replyLikeTimeService ;
	@Autowired
	ReplyLikeService replyLikeService;
	@Override
	public void add(Questreply reply) {
		reply.setIs_delete((byte) 0);
		QuestreplyMapper.insert(reply);
	}

	@Override
	public void update(Questreply Questreply) {
//		QuestreplyMapper.updateByPrimaryKey(Questreply);
		QuestreplyMapper.updateByPrimaryKeySelective(Questreply);

	}
	@Override
	public List<Questreply> getByQuesId(long questid) {

		QuestreplyExample example = new QuestreplyExample();
		//过滤
		example.createCriteria().andQuestidEqualTo(questid).andIs_deleteEqualTo((byte)0);

		List<Questreply> list =QuestreplyMapper.selectByExample(example);

		long replyid;

		for(int i=0,len=list.size();i<len;i++){
			replyid=list.get(i).getReplyid();
			list.get(i).setContent(QuestreplyMapper.selectByPrimaryKey(replyid).getContent());
		}
		return list;
	}

	@Override
	public Questreply getByAnswerId(long answerid) {

		return QuestreplyMapper.selectByPrimaryKey(answerid);
	}

	@Override
	public List<ForeAnswer> full(List<Questreply> list) throws Exception{

		List<ForeAnswer> answers =new ArrayList<>();
		ForeAnswer answer =new ForeAnswer();
		ForeUsers foreUsers =new ForeUsers();
		for(Questreply o:list){
			answers.add(full(foreUsers, answer, o));
		}
		return answers;
	}

	@Override
	public ForeAnswer full(ForeUsers foreUsers,ForeAnswer answer,Questreply Questreply) throws Exception {

		foreUsers = new ForeUsers(userService.get(Questreply.getUserid()));

		answer=new ForeAnswer(replyLikeTimeService.getLikeTimes(Questreply.getQuestid()),Questreply.getImage(),Questreply.getReplyid(), Questreply.getContent(), Questreply.getTime(), foreUsers);

		if(Questreply.getReplyuser()==null){
			answer.setType(0);;
		}
		else{
			Questreply Questreply2 = QuestreplyMapper.selectByPrimaryKey(Questreply.getReplyid());
			foreUsers =new ForeUsers(userService.get(Questreply2.getUserid()));
			answer.setType(1);
			answer.setQuote(Questreply2.getContent());
			answer.setTouser(foreUsers);
		}

		return answer;
	}

	@Override
	public void deleteByQuestid(long questid) {
		QuestreplyExample example = new QuestreplyExample();
		example.createCriteria().andQuestidEqualTo(questid);

		List<Questreply> QuestreplyList = QuestreplyMapper.selectByExample(example);
//删除标记，新的springboot版本还没生成
		if(QuestreplyList != null) {
			for (Questreply reply : QuestreplyList) {
				reply.setIs_delete((byte) 1);
				QuestreplyMapper.updateByPrimaryKey(reply);
			}
		}
	}


}
