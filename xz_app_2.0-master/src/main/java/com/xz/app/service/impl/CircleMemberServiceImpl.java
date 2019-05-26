package com.xz.app.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.xz.app.service.UserService;
import com.xz.app.util.OSSUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xz.app.exception.XZException;
import com.xz.app.forepojo.ForeCirclember;
import com.xz.app.forepojo.ForeInfluence;
import com.xz.app.forepojo.ForeUsers;
import com.xz.app.mapper.ApplyinviteMapper;
import com.xz.app.mapper.CircleInforMapper;
import com.xz.app.mapper.CircleMemberMapper;
import com.xz.app.mapper.UsersMapper;

import com.xz.app.pojo.Applyinvite;
import com.xz.app.pojo.CircleInfor;
import com.xz.app.pojo.CircleMember;
import com.xz.app.pojo.CircleMemberExample;
import com.xz.app.pojo.CircleMemberKey;
import com.xz.app.pojo.Users;
import com.xz.app.pojo.UsersExample;
import com.xz.app.service.CircleMemberService;

@Service
public class CircleMemberServiceImpl implements CircleMemberService {

	@Autowired
	CircleMemberMapper circleMemberMapper;
	@Autowired
	CircleInforMapper circleInforMapper;
	@Autowired
	ApplyinviteMapper applyinviteMapper;
	@Autowired
	UsersMapper usersMapper;
	@Autowired
    UserService userService;

	@Override
	public void add(CircleMember circleMember) {

		circleMemberMapper.insert(circleMember);

		//更新circleInfor里的membernumber todo
        Integer circleid = circleMember.getCircleid();
        CircleMemberExample example = new CircleMemberExample();
        example.createCriteria().andCircleidEqualTo(circleid);
        List<CircleMember> circleMembers = circleMemberMapper.selectByExample(example);

        CircleInfor circleInfor = circleInforMapper.selectByPrimaryKey(circleid);
        circleInfor.setMembernumber(circleMembers.size());
        //circleInfor.setMembernumber(circleInfor.getMembernumber()+1);
        circleInforMapper.updateByPrimaryKeySelective(circleInfor);

    }

	@Override
	public void delete(CircleMemberKey circleMemberKey){

		circleMemberMapper.deleteByPrimaryKey(circleMemberKey);

        //更新circleInfor里的membernumber
        Integer circleid = circleMemberKey.getCircleid();
        CircleInfor circleInfor = circleInforMapper.selectByPrimaryKey(circleid);
        circleInfor.setMembernumber(circleInfor.getMembernumber()-1);
        circleInforMapper.updateByPrimaryKeySelective(circleInfor);

    }
	@Override
	public void update(CircleMember circleMember) {
		// TODO Auto-generated method stub
		circleMemberMapper.updateByPrimaryKey(circleMember);
	}
	@Override
	public boolean isExist(int userId, Integer circleId) throws Exception {

		if (circleMemberMapper.selectByPrimaryKey(new CircleMemberKey(userId, circleId))!= null)
			return true;

		else {
			throw new XZException("006", "未关注该圈");
		}
	}

	@Override
	public CircleMember get(int circleId, int userId) {
		return circleMemberMapper.selectByPrimaryKey(new CircleMemberKey(userId, circleId));
	}

	@Override
	public List<CircleMember> chart(int circleId) {

		CircleMemberExample example = new CircleMemberExample();

		example.createCriteria().andCircleidEqualTo(circleId);

		example.setOrderByClause("userid desc");

		List<CircleMember> result = circleMemberMapper.selectByExample(example);

		return result;
	}

	@Override
	public List<CircleMember> list(int userId)  {

		CircleMemberExample example = new CircleMemberExample();

		example.createCriteria().andUseridEqualTo(userId);
		example.setOrderByClause("circleid desc");
		List<CircleMember> result = circleMemberMapper.selectByExample(example);
        
		if(!result.isEmpty()) {
			if(result.get(0).getNo()!=null) {
				CircleMemberExample example2 = new CircleMemberExample();
				example2.createCriteria().andUseridEqualTo(userId);
				example2.setOrderByClause("no asc");
				return  circleMemberMapper.selectByExample(example2);
			}
		}
		return result;
	}

	@Override
	public ForeCirclember full(CircleMember circleMember, CircleInfor circleInfor, ForeCirclember foreCirclember) {

		/*
		 * 获取资料
		 */
		foreCirclember = new ForeCirclember();

		/*
		 * 填充资料
		 */
		foreCirclember.setCircleId(circleMember.getCircleid());
		foreCirclember.setInfluence(circleMember.getInfluence());
		foreCirclember.setCircleName(circleInfor.getCirclename());
		foreCirclember.setCircle_img(OSSUtil.getPrefixURL("circleImg/") + circleInfor.getCircleimg() + ".jpg");
		if(circleInfor.getCy_alias()==null) {
			foreCirclember.setCy_alias("分享");
		}
		else {
			foreCirclember.setCy_alias(circleInfor.getCy_alias());
		}
		if(circleInfor.getQuest_alias()==null) {
			foreCirclember.setQuest_alias("问答");
		}
		else {
			foreCirclember.setQuest_alias(circleInfor.getQuest_alias());
		}

		return foreCirclember;
	}

	@Override
	public List<ForeCirclember> full(List<CircleMember> list) {

		List<ForeCirclember> forelist = new ArrayList<>();

		ForeCirclember foreCirclember = new ForeCirclember();

		List<CircleInfor> circleInfors = new ArrayList<>();
		for(CircleMember o:list) {
			circleInfors.add(circleInforMapper.selectByPrimaryKey(o.getCircleid()));
		}

		for (int i = 0, length = list.size(); i < length; i++) {
            
			if (circleInfors.get(i) == null || circleInfors.get(i).getCircleid() == 0||circleInfors.get(i).getCircleid() == 71) {
				continue;
			}
			forelist.add(full(list.get(i), circleInfors.get(i), foreCirclember));
		}
		return forelist;
	}

	@Override
	public ForeInfluence full(CircleMember circleMember, ForeInfluence foreInfluence,Users u) {

		foreInfluence = new ForeInfluence();
		ForeUsers c = new ForeUsers(u);
		foreInfluence = new ForeInfluence();
		foreInfluence.setInfluence(circleMember.getInfluence());
		foreInfluence.setUser(c);

		return foreInfluence;
	}

	@Override
	public List<ForeInfluence> fullInfluence(List<CircleMember> list){

		List<ForeInfluence> forelist = new ArrayList<>();

		ForeInfluence foreInfluence = null;

		for (CircleMember o:list) {
		    Users users = userService.get(o.getUserid());
			forelist.add(full(o, foreInfluence,users));
		}
		return forelist;
	}
	@Override
	public boolean focusCircle(int userId, int circleId, String message) throws Exception {

		try {
		isExist(userId, circleId);
		}catch( XZException exception) {
			CircleInfor circleInfor = circleInforMapper.selectByPrimaryKey(circleId);
      
			if (circleInfor.getInvite() == 1) {// 如果是私有圈子则需要审核

				Applyinvite applyinvite = new Applyinvite(userId, circleId, (byte) 1, message);

				applyinviteMapper.insert(applyinvite);

				throw new XZException("007","已提交入圈申请");
			}
			CircleMember Member = new CircleMember(userId, circleId, 10, (byte) 5);
            Member.setNo(1);
			add(Member);

			return true;
		}
		throw new XZException("006","重复的关注,UserId:"+userId+"CircleId:"+circleId);
	}

	@Override
	public boolean isAdmin(int userId, int circleId) throws Exception{

		CircleInfor circleInfor = circleInforMapper.selectByPrimaryKey(circleId);
		if(circleInfor.getLeaderid()==userId) {
			return true;
		}
		for (int i=1; i < 10 + 1; i++) {
			Method method = circleInfor.getClass().getMethod("getAdmin" + i);
			int ID = (int) method.invoke(circleInfor);
			if(ID==userId) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean quitCircle(int circleId, int userId) {

		delete(new CircleMemberKey(userId, circleId));
		
		return true;
	}

	@Override
	public List<Integer> getAdminCircle(int userId) throws Exception {
        // Auto-generated method stub
        List<Integer> circles = new ArrayList<>();
        CircleMemberExample example = new CircleMemberExample();
		example.createCriteria().andUseridEqualTo(userId);
		List<CircleMember> circleMembers = circleMemberMapper.selectByExample(example);
		for(CircleMember o:circleMembers) {
			
			if(isAdmin(userId, o.getCircleid())) {
				circles.add(o.getCircleid());
			}
		}
		
		return circles;
	}

    @Override
    public List<Users> getUserIDByNickName(String keyWord,int circleId) throws Exception {

	    //查询圈内成员的userId们
        CircleMemberExample example = new CircleMemberExample();
        example.createCriteria().andCircleidEqualTo(circleId);
        List<CircleMember> circleMems = circleMemberMapper.selectByExample(example);

        List<Users> usersList = new ArrayList<>();

        for(CircleMember c:circleMems){
            Users users = userService.get(c.getUserid());
            if(users!=null) {
                if(users.getName().contains(keyWord)){
                    usersList.add(users);
                }
            }
        }

        //圈内昵称部分
        example = new CircleMemberExample();
        CircleMemberExample.Criteria Criteria = example.createCriteria();

        if (StringUtils.isNotBlank(keyWord)) {

            keyWord = "%" + keyWord + "%";

            Criteria.andNicknameLike(keyWord).andCircleidEqualTo(circleId);
        }

        List<CircleMember> circleMembers = circleMemberMapper.selectByExample(example);

        for(CircleMember c:circleMembers){
            Users users = userService.get(c.getUserid());
            if(users!=null) usersList.add(users);
        }

        if (usersList.isEmpty()) throw new Exception("没有包含这样关键词的用户名");

        return usersList;

    }

	@Override
	public void diyCircleRank(Integer[] diyRank, int userId) throws Exception {
		// TODO Auto-generated method stub
		for(int i=0,l=diyRank.length;i<l;i++) {
			CircleMember circleMember = circleMemberMapper.selectByPrimaryKey(new CircleMemberKey(userId, diyRank[i]));
			circleMember.setNo(i);
			circleMemberMapper.updateByPrimaryKey(circleMember);
		}
	}

}
