package com.xz.app.controller;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.xz.app.exception.XZException;
import com.xz.app.forepojo.*;
import com.xz.app.packag.*;
import com.xz.app.pojo.*;
import com.xz.app.service.*;
import com.xz.app.util.OSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName: CircleController
 * @Description:TODO 圈子相关操作
 * @author: 杨瑞
 * @date: 2018年5月17日
 * 
 * @Copyright: 2018 www.oijkl.com Inc. All rights reserved.
 */

@SuppressWarnings("unchecked")
@Api(tags = "圈子操作接口")
@Controller
@RequestMapping(value="")
public class CircleController {
	private static Logger logger = Logger.getLogger(CircleController.class);// 添加日志
	private static final String wxgetAPPCode = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";
	private static final String wxgetAPPQRCode = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=";
	Base64.Encoder encoder = Base64.getEncoder();
	@Autowired
	private CircleinforService circleinforService;
	@Autowired
	private CircleMemberService circleMemberService;
	@Autowired
	private CirclerelationService circlerelationService;
	@Autowired
	private ApplyInviteService applyInviteService;
	@Autowired
	private UserdataService userdataService;
	@Autowired
	private UserService userService;

	/**
	 * redisService
	 */
	@Autowired
	private RedisService redisService;

	//TODO 其他圈子的创建待修改
	@ApiOperation(value = "创建圈子", notes = "用户创建圈子，可快速创建圈子")
	@RequestMapping(value = "createCirclefor", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> CreateCirclefor(
			@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
			@RequestParam("price") @ApiParam(value = "价格") BigDecimal price,
			@RequestParam("invite") @ApiParam(value = "是否是邀请制 1是0否 下同") Byte invite,
			@RequestParam("tenureSystem") @ApiParam(value = "是否是邀请制") Byte tenuserSystem,
			@RequestParam("manage") @ApiParam(value = "是否是管理员制") Byte manage,
			@RequestParam("circleName") @ApiParam(value = "圈子名") String circleName,
			@RequestParam("notice") @ApiParam(value = "圈子公告") String notice,
			@RequestParam("location") @ApiParam(value = "圈子地理位置") String location,
			@RequestParam(value = "hascircleImg", required = false,defaultValue = "0")@ApiParam(value = "是否有圈子的图片") byte hasCircleImg,
			@RequestParam(value = "sonCircles", required = false)@ApiParam(value = "子圈信息") String sons,
			@RequestParam(value = "dadCircles", required = false)@ApiParam(value = "父圈信息") String dads,
			@RequestParam(value = "circlea", required = false)@ApiParam(value = "圈子别名") String circlea) throws Exception {

		Map<String, Object> map = new HashMap<>();

		int userID = redisService.getUserId(token);
		/*
		 * 如果圈既是邀请制，又无父圈和子圈则可不通过管理员审核允许该圈直接创建，此功能为快速创建圈的功能
		 */
		if (invite == 1 && dads == null && sons == null) {
			CircleInfor circleInfor = new CircleInfor("de", circleName, userID, price,
					invite, tenuserSystem, manage, notice, 1, (long) 0, (long) 0, location);
			circleinforService.add(circleInfor);
			if (hasCircleImg == 1) {
				circleInfor.setCircleimg(String.valueOf(circleInfor.getCircleid()));
				circleinforService.update(circleInfor);
			}
			circleMemberService
					.add(new CircleMember(userID, circleInfor.getCircleid(), 10, (byte) 5));
			map.putAll(MapHelper.success());
			map.put("filename", circleInfor.getCircleimg() + ".jpg");
			return map;
		}
		/*
		 * 如果圈既是邀请制，又无父圈和子圈则可不通过管理员审核允许该圈直接创建，此功能为快速创建圈的功能
		 */

		//将用户创建圈的信息添加到圈创建申请表中
		Circlecheck circlecheck = new Circlecheck(circleName, userID, invite, manage, price,
				tenuserSystem, notice, location);

		circleinforService.CrateCirclefor(circlecheck);

		/*
		 * 父圈子圈圈别名等信息存在redis中
		 */
		if (dads != null) {
			int[] dadcircles = XzAppUtil.getIntFromString(dads);
			redisService.addDadCircleToRedis(circlecheck.getId(), dadcircles);
		}
		if (sons != null) {
			int[] soncircles = XzAppUtil.getIntFromString(sons);
			redisService.addSonCircleToRedis(circlecheck.getId(), soncircles);
		}
		if (circlea != null) {
			redisService.addCircleaToRedis(circlecheck.getId(), circlea);
		}
		/*
		 * 父圈子圈圈别名等信息存在redis中
		 */

		map.putAll(MapHelper.success());

		return map;
	}

	@ApiOperation(value = "热门搜索",notes = "返回搜索排行前六")
	@RequestMapping(value = "topSearch",method = RequestMethod.GET)
	@ResponseBody
	public Map topSearch(@RequestHeader(value="token") String token){

		Map<String, String> map=MapHelper.success();
		Jedis jedis=new Jedis("localhost");
		map=(jedis.hgetAll("hot"));
		jedis.close();
		List list=XzAppUtil.TopsearchSort(map);
		Map<String,Object> map2=MapHelper.success();
		map2.put("top6list",list);
		return map2;
	}

	@ApiOperation(value = "搜索圈子", notes = "用户按照关键字搜索圈子")
	@RequestMapping(value = "searchCircle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchCircle(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
											@RequestParam("circleName") @ApiParam(value = "圈子名") String circleName) {

        redisService.addSearchrecordstoRedis(circleName);

		List<CircleInfor> list = circleinforService.search(circleName);// 从数据库获得和circleName相似的圈子
		List<ForeCircleInfor> forelist = circleinforService.full(list, redisService.getUserId(token));//将圈信息转化成前端格式
		Map<String, Object> map = MapHelper.success();
		map.put("circlelist", forelist);
		return map;
	}


	@ApiOperation(value = "修改圈子资料",notes = "用户点击圈子管理，修改圈子资料")
	@RequestMapping(value = "alterCircleinfo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> AlterCircleinfo (
			@RequestHeader(value = "token")@ApiParam(value = "用户登录权限") String token,
			@RequestParam(value = "circleId")@ApiParam(value = "圈子ID") Integer circleId,
			@RequestParam(value = "circleName")@ApiParam(value = "圈子名称") String circleName,
			@RequestParam(value = "notice")@ApiParam(value = "圈子简介") String notice,
			@RequestParam(value = "cy_alias")@ApiParam(value = "分享栏自定名") String cy_alias,
			@RequestParam(value = "quest_alias")@ApiParam(value = "提问栏自定名") String quest_alias,
			@RequestParam(value = "circleImg", required = false)@ApiParam(value = "圈子的图片") String circleImg
	) throws Exception{

		if (circleMemberService.isAdmin(redisService.getUserId(token), circleId) == false) {//判断是否具有管理员权限
			return MapHelper.setInfor("0", "009", "不是管理员没有权限操作");
		}

		CircleInfor circleInfor = circleinforService.get(circleId);
		circleInfor.setCy_alias(cy_alias);
		circleInfor.setNotice(notice);
		circleInfor.setQuest_alias(quest_alias);
		circleInfor.setCirclename(circleName);

		if (circleImg != null) {//更新圈子头像
			circleInfor.setCircleimg(circleImg);
		}

		circleinforService.update(circleInfor);
		return MapHelper.success();
	}

	@ApiOperation(value = "圈子信息", notes = "根据圈子的ID获取圈子的信息")
	@RequestMapping(value = "circleInfor", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> circleInfor(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
										   @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId) {

		CircleInfor circleInfor = circleinforService.get(circleId);// 获得该圈子的信息
		ForeCircleInfor foreCirclrInfor = circleinforService.full(circleInfor, redisService.getUserId(token));//将圈信息转化成前端格式
		Map<String, Object> map = MapHelper.success();
		map.put("circleInfor", foreCirclrInfor);
		return map;
	}

	@ApiOperation(value = "关注圈子", notes = "用户关注圈子，成为圈子成员")
	@RequestMapping(value = "focusCircle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> FocusCircle(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
										   @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
										   @RequestParam(value = "message", required = false, defaultValue = "") @ApiParam(value = "加入圈子时的信息") String message) throws Exception {

		circleMemberService.focusCircle(redisService.getUserId(token), circleId, message);//用户关注圈
		return MapHelper.success();
	}

	@ApiOperation(value = "退出圈子", notes = "用户退出圈子")
	@RequestMapping(value = "quitCircle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> quitCircle(
			@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
			@RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId) {

		circleMemberService.quitCircle(circleId, redisService.getUserId(token));//用户退出圈
		return MapHelper.success();
	}

	@ApiOperation(value = "圈管理员审核申请列表", notes = "圈管理员审核申请列表")
	@RequestMapping(value = "checkApplication", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkApplication(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限")String token,
												Integer userId)
			throws Exception {
//		redisService.getUserId(token)
		List<Integer> circleInfors = circleMemberService.getAdminCircle(userId);//获取该用户管理的所有圈子
		List<ForeApply> applies = new ArrayList<>();
		for (Integer o : circleInfors) {
			List<ForeApply> FAs = applyInviteService.getApplyByCircleId(o);//从以上圈子中获取到申请信息
			for (ForeApply apply : FAs) {
				applies.add(apply);
			}
		}
		Map<String, Object> map = MapHelper.success();
		map.put("apply", applies);
		return map;
	}

	@ApiOperation(value = "圈管理员审核用户申请", notes = "圈管理员审核用户申请")
	@RequestMapping(value = "applicationDecide", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> applicationDecide(@RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
												 @RequestParam("userId") @ApiParam(value = "用户ID") Integer userId, @RequestParam("decide") @ApiParam(value = "管理员的决定") byte decide,
												 @RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) throws Exception {

		if (circleMemberService.isAdmin(redisService.getUserId(token), circleId) == false) {//判断是否具有管理员权限
			return MapHelper.setInfor("0", "009", "不是管理员没有权限操作");
		}
		if (decide == 0) {//decide为0则表示拒绝该用户的申请
			applyInviteService.delete(new ApplyinviteKey(userId, circleId));
		}
		if (decide == 1) {//decide为1则表示通过该用户的申请，将该用户添加到圈成员表中
			circleMemberService.add(new CircleMember(userId, circleId, 100, (byte) 5));
			applyInviteService.delete(new ApplyinviteKey(userId, circleId));
		}

		return MapHelper.success();

	}

	@ApiOperation(value = "圈管理员踢用户出圈子", notes = "圈管理员踢用户出圈子")
	@RequestMapping(value = "dislodgeFromCircle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> dislodgeFromCircle(@RequestParam("circleId") @ApiParam(value = "用户登陆权限") Integer circleId,
												  @RequestParam("otherId") @ApiParam(value = "被踢出成员的ID") Integer otherId,
												  @RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) throws Exception {

		if (circleMemberService.isAdmin(redisService.getUserId(token), circleId) == false) {
			return MapHelper.setInfor("0", "009", "不是管理员没有权限操作");
		}
		circleMemberService.quitCircle(circleId, otherId);//解除该用户的关注关系
		return MapHelper.success();
	}

	@ApiOperation(value = "管理员禁言成员", notes = "管理员禁言成员")
	@RequestMapping(value = "forbiddenUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> forbiddenUser(@RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
											 @RequestParam("otherId") @ApiParam(value = "被禁言者Id") Integer otherId,
											 @RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) throws Exception {

		if (circleMemberService.isAdmin(redisService.getUserId(token), circleId) == false) {
			return MapHelper.setInfor("0", "009", "不是管理员没有权限操作");
		}
		redisService.forbidden(otherId, circleId);//将用户添加到禁止名单中
		return MapHelper.success();
	}

	@ApiOperation(value = "查看管理员", notes = "查看管理员")
	@RequestMapping(value = "getCircleAdmin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getCircleAdmin(@RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
											  @RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) throws Exception {

		List<ForeUsers> admins = circleinforService.getForeAdmins(circleId);//获取该圈的管理员成员列表
		Map<String, Object> map = MapHelper.success();
		map.put("data", admins);
		return map;
	}

	@ApiOperation(value = "添加管理员", notes = "添加管理员")
	@RequestMapping(value = "addCircleAdmin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addCircleAdmin(@RequestParam("circleId") @ApiParam(value = "圈子的ID") Integer circleId,
											  @RequestParam("userId") @ApiParam(value = "用户ID") Integer userId,
											  @RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) throws Exception {
		// 判断是否为圈主
		int leaderid = redisService.getUserId(token);
		if (circleinforService.get(circleId).getLeaderid() != leaderid) {
			throw new XZException("009", "您不是圈主无权限进行操作");
		}

		// 判断是否为圈成员
		if (!circleMemberService.isExist(userId, circleId)) {
			throw new XZException("009", "该用户不是圈成员");
		}

		circleinforService.addAdmins(circleId, userId);

		return MapHelper.success();
	}

	@ApiOperation(value = "删除管理员", notes = "删除管理员")
	@RequestMapping(value = "delCircleAdmin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delCircleAdmin(@RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
											  @RequestParam("userId") @ApiParam(value = "用户ID") Integer userId,
											  @RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) throws Exception {
		// 判断是否为圈主
		int leaderid = redisService.getUserId(token);
		if (circleinforService.get(circleId).getLeaderid() != leaderid) {
			throw new XZException("009", "您不是圈主无权限进行操作");
		}

		circleinforService.delAdmins(circleId, userId);

		return MapHelper.success();
	}

	@ApiOperation(value = "影响力排行", notes = "影响力排行")
	@RequestMapping(value = "influenceChart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> influenceChart(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
											  @RequestParam("startPage") @ApiParam(value = "起始页") Integer start,
											  @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId) throws Exception {

		PageHelper.offsetPage(start * 10, 10);//设置分页大小为10
		List<CircleMember> list = circleMemberService.chart(circleId);//根据影响力逆序得到圈成员列表
		int total = (int) new PageInfo<>(list).getTotal();
		List<ForeInfluence> circlembers = circleMemberService.fullInfluence(list);//将影响力排行转化为前端格式
		Map<String, Object> map = MapHelper.success();
		map.put("data", circlembers);
		map.put("page", PageUtil.getPage(total, list.size(), start));
		return map;
	}

	@ApiOperation(value = "公告发布", notes = "公告发布")
	@RequestMapping(value = "noticeEdit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> noticeEdit(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
										  @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
										  @RequestParam("notice") @ApiParam(value = "公告") String notice) throws Exception {

		CircleInfor cir = circleinforService.get(circleId);
		cir.setNotice(notice);// 加入公告
		circleinforService.update(cir);//更新圈的信息
		return MapHelper.success();

	}

	@ApiOperation(value = "用户圈子列表", notes = "用户圈子列表")
	@RequestMapping(value = "seeCircle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> seeCircle(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) throws Exception {

		List<CircleMember> list = circleMemberService.list(redisService.getUserId(token));//根据用户ID获得圈成员列表

		List<ForeCirclember> forelist = circleMemberService.full(list);// 填充信息

		Map<String, Object> map = MapHelper.success();

		map.put("circlelist", forelist);
		return map;
	}

	@ApiOperation(value = "圈子推荐", notes = "圈子推荐")
	@RequestMapping(value = "recoCircle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> recoCircle(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
										  @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId) throws IOException {

		Map<String, Object> map;

		Set<Integer> family = circlerelationService.getMyFamily(circleId);//根据圈关系获得该圈的亲戚圈
		List<Integer> definedCircle = redisService.getRecoCircleID(circleId);//如果亲戚圈个数不足6个，则增加缺少的默认圈。
		map = MapHelper.success();
		map.put("recoCircle", circleinforService.recoCircle(redisService.getUserId(token), definedCircle, family));
		return map;
	}

	@ApiOperation(value = "设置圈子昵称", notes = "设置圈子昵称")
	@RequestMapping(value = "setNickName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setNickName(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
										   @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
										   @RequestParam("nickName") @ApiParam(value = "用户昵称") String nickName) throws Exception {

		CircleMember circleMember = circleMemberService.get(circleId, redisService.getUserId(token));
		if (circleMember == null) {// 判断是否关注了该圈子，未关注用户不可设置昵称
			return MapHelper.setInfor("0", "006", "未关注圈子");
		}
		circleMember.setNickname(nickName);
		circleMemberService.update(circleMember);

		return MapHelper.success();
	}

	@ApiOperation(value = "获取昵称", notes = "获取昵称")
	@RequestMapping(value = "getNickName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getNickName(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
										   @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
										   @RequestParam("userId") @ApiParam(value = "用户ID") Integer userId) throws Exception {

		CircleMember circleMember = circleMemberService.get(circleId, userId);
		if (circleMember == null) {// 判断是否关注了该圈子
			return MapHelper.setInfor("0", "006", "未关注圈子");
		}
		Map<String, Object> map = MapHelper.success();
		if (circleMember.getNickname() != null) {
			map.put("nickname", circleMember.getNickname());
			return map;
		}
		map.put("nickname", "");
		return map;
	}

	@ApiOperation(value = "搜索圈成员", notes = "搜索圈成员(昵称和用户名)")
	@RequestMapping(value = "searchCircleMember", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchCircleMember(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
												  @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
												  @RequestParam("keyword") @ApiParam(value = "关键字") String keyword) throws Exception {

		List<Users> users = circleMemberService.getUserIDByNickName(keyword, circleId);

		List<ForeUsers> foreUsersList = new ArrayList<>();

		ForeUsers foreUsers;
		for (Users user : users) {//将用户信息转化成前端格式
			foreUsers = new ForeUsers();
			foreUsers.setAuthor_id(user.getUserid());
			foreUsers.setAuthor_image(OSSUtil.getPrefixURL("headImg/") + user.getHeadimg() + ".jpg");
			foreUsers.setAuthor_name(user.getName());
			foreUsers.setSex(userdataService.findById(user.getUserid()).getSex());
			foreUsersList.add(foreUsers);
		}

		Map<String, Object> map = MapHelper.success();
		map.put("data", foreUsersList);
		return map;
	}

	@ApiOperation(value = "重命名圈", notes = "重命名圈")
	@RequestMapping(value = "renameCyinfor", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> renameCyinfor(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
											 @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
											 @RequestParam("newName") @ApiParam(value = "新名字") String newName) throws Exception {

		if (!circleMemberService.isAdmin(redisService.getUserId(token), circleId)) {
			return MapHelper.setInfor("0", "009", "不是管理员无权限");
		}
		CircleInfor circleInfor = circleinforService.get(circleId);
		circleInfor.setCy_alias(newName);
		circleinforService.update(circleInfor);
		return MapHelper.success();
	}

	@ApiOperation(value = "重命名问题板块", notes = "重命名问题板块")
	@RequestMapping(value = "renameQuestion", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> renameQuestion(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
											  @RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId,
											  @RequestParam("newName") @ApiParam(value = "新的命名") String newName) throws Exception {

		if (!circleMemberService.isAdmin(redisService.getUserId(token), circleId)) {
			return MapHelper.setInfor("0", "009", "不是管理员无权限");
		}
		CircleInfor circleInfor = circleinforService.get(circleId);
		circleInfor.setQuest_alias(newName);
		circleinforService.update(circleInfor);
		return MapHelper.success();
	}

	@ApiOperation(value = "自定义圈子排序", notes = "自定义圈子排序")
	@RequestMapping(value = "diyCircleRank", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> diyCircleRank(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
											 @RequestParam("diyRank") @ApiParam(value = "自定义的排序") String diyRank) throws Exception {

		int userId = redisService.getUserId(token);
		try {
			circleMemberService.diyCircleRank(XzAppUtil.getCircleID(diyRank), userId);
		} catch (Exception e) {
			return MapHelper.setInfor("0", "010", "更新圈子排序失败");
		}
		return MapHelper.success();
	}

	@ApiOperation(value = "圈子推荐二维码", notes = "圈子推荐二维码")
	@RequestMapping(value="circleAPPQRCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> circleAPPQRCode(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
			@RequestParam("path") @ApiParam(value = "路径") String path,
			@RequestParam("circleId") @ApiParam(value = "圈子ID") Integer circleId) throws Exception {

		Map<String, Object> map = new HashMap<>();

		URL url = new URL(wxgetAPPQRCode + XzAppUtil.getAccessToken());
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("POST");// 提交模式
		// conn.setConnectTimeout(10000);//连接超时 单位毫秒
		// conn.setReadTimeout(2000);//读取超时 单位毫秒
		// 发送POST请求必须设置如下两行
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
		// 发送请求参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("path", path);
		paramJson.put("width", 430);
		paramJson.put("auto_color", true);

		printWriter.write(paramJson.toString());
		// flush输出流的缓冲
		printWriter.flush();
		// 开始获取数据
		BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());

		OSSUtil.uplodeImages(bis, "generalizeCode/" + circleId + "f.png");
		map = MapHelper.success();

		map.put("QRCode", OSSUtil.getPrefixURL("generalizeCode/") + circleId + "f.png");// 二维码的链接地址
		return map;
	}


	@ApiOperation(value = "圈子推荐二维码", notes = "圈子推荐二维码")
	@RequestMapping(value = "circleAPPQRCodeC", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> circleAPPQRCodeC(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
												@RequestParam("scene") @ApiParam(value = "scene") String scene,
												@RequestParam("page") @ApiParam(value = "起始页") String page,
												@RequestParam("circleId") @ApiParam(value = "圈子ID") int circleId) throws Exception {

		Map<String, Object> map = new HashMap<>();

		URL url = new URL(wxgetAPPCode + XzAppUtil.getAccessToken());
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("POST");// 提交模式
		// conn.setConnectTimeout(10000);//连接超时 单位毫秒
		// conn.setReadTimeout(2000);//读取超时 单位毫秒
		// 发送POST请求必须设置如下两行
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
		// 发送请求参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("scene", scene);
		paramJson.put("page", page);
		paramJson.put("width", 430);
		paramJson.put("auto_color", true);

		printWriter.write(paramJson.toString());
		// flush输出流的缓冲
		printWriter.flush();
		// 开始获取数据
		BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
		OutputStream os = new FileOutputStream(new File("/xinzhi/Data/generalizeCode/" + circleId + ".png"));
		int len;
		byte[] arr = new byte[1024];
		while ((len = bis.read(arr)) != -1) {
			os.write(arr, 0, len);
			os.flush();
		}
		os.close();
		map = MapHelper.success();

		map.put("QRCode", "https://www.oijkl.com/xzData/generalizeCode/" + circleId + ".png");// 二维码的链接地址
		map.put("QR", "https://www.oijkl.com/xzData/generalizeCode/" + circleId + ".png");// 二维码的链接地址
		return map;
	}

    /*by Ou*/
    @ApiOperation(value = "附近的圈子", notes = "搜索附近的圈子")
    @RequestMapping(value = "neighborhood",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getCircleByLatLng(
            @RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
            @RequestParam("lat") @ApiParam(value = "经度") BigDecimal lat,
            @RequestParam("lng") @ApiParam(value = "纬度") BigDecimal lng,
            @RequestParam(value = "distance", defaultValue = "3") @ApiParam(value = "距离") int distance
    ) throws Exception {

        int userId = redisService.getUserId(token);

        double lat1 = lat.setScale(6,BigDecimal.ROUND_DOWN).doubleValue();//用户经度n

        double lng1 = lng.setScale(6,BigDecimal.ROUND_DOWN).doubleValue();//用户纬度



        BigDecimal[] latLng = GeoUtil.GetLatLng(lat1, lng1, distance);
        BigDecimal minLat = latLng[0].setScale(6,BigDecimal.ROUND_DOWN);
        BigDecimal maxLat = latLng[1].setScale(6,BigDecimal.ROUND_DOWN);
        BigDecimal minLng = latLng[2].setScale(6,BigDecimal.ROUND_DOWN);
        BigDecimal maxLng = latLng[3].setScale(6,BigDecimal.ROUND_DOWN);
        //TODO 东西经度 0° 180°处判断以及南极点北极点的情况判断未实现
        List<CircleInfor> list = circleinforService.searchByLatLng(maxLat, minLat, maxLng, minLng);
//		List<CircleInfor> list = new ArrayList<>();
        if (list.size() == 0) throw new XZException("005","附近没有圈子！");

        Map<ForeCircleInfor, String> near = new HashMap<>();
        for (CircleInfor circle : list) {
            double lat2 = circle.getLat().setScale(6,BigDecimal.ROUND_DOWN).doubleValue();//用户经度
            double lng2 = circle.getLng().setScale(6,BigDecimal.ROUND_DOWN).doubleValue();//用户纬度

            double realDistance = GeoUtil.getDistance(lat1, lng1, lat2, lng2);
            near.put(circleinforService.full(circle,userId), String.valueOf(realDistance));
        }
        List<Map.Entry<ForeCircleInfor, String>> entryList = new ArrayList<Map.Entry<ForeCircleInfor, String>>(near.entrySet());
        Collections.sort(entryList, ComparatorUtil.CmpOfMapValue());

//        Map<ForeCircleInfor, String> sortedMap = new LinkedHashMap<>();
//        for(Map.Entry<CircleInfor, String> n:entryList){
//            sortedMap.put(n.getKey(),n.getValue());
//        }
//
        Map<String, Object> map = MapHelper.success();
        map.put("data", entryList);
        return map;
	}
}
