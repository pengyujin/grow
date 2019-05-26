package com.xz.app.service;

import java.util.List;

import com.xz.app.forepojo.ForeCirclember;
import com.xz.app.forepojo.ForeInfluence;
import com.xz.app.pojo.CircleInfor;
import com.xz.app.pojo.CircleMember;
import com.xz.app.pojo.CircleMemberKey;
import com.xz.app.pojo.Users;

public interface CircleMemberService {

	// @DataSource("slave")
	void add(CircleMember circleMember);

	void update(CircleMember circleMember);
	// @DataSource("slave")
	void delete(CircleMemberKey circleMemberKey);

	// @DataSource("master")
	CircleMember get(int circleId, int userId);
	
	boolean focusCircle(int userId, int circleId, String message) throws Exception;
	
	boolean quitCircle(int circleId, int userId);

	List<Integer> getAdminCircle(int userId) throws Exception;
	// @DataSource("master")
	List<CircleMember> chart(int circleId);

	// @DataSource("master")
	List<CircleMember> list(int userId) throws Exception;

	// @DataSource("master")
	ForeCirclember full(CircleMember circleMember, CircleInfor circleInfor, ForeCirclember foreCirclember);

	// @DataSource("master")
	List<ForeCirclember> full(List<CircleMember> list) throws Exception;

	// @DataSource("master")
	ForeInfluence full(CircleMember circleMember, ForeInfluence foreInfluence, Users u);

	// @DataSource("master")
	List<ForeInfluence> fullInfluence(List<CircleMember> list);

	// @DataSource("master")
	boolean isExist(int userId, Integer circleId) throws Exception;
	
	boolean isAdmin(int userId, int circleId) throws Exception;

	List<Users> getUserIDByNickName(String keyWord, int circleId) throws Exception;

	void diyCircleRank(Integer[] diyRank, int userId) throws Exception;
	
}
