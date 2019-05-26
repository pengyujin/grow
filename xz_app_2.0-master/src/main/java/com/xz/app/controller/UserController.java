package com.xz.app.controller;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xz.app.exception.XZException;
import com.xz.app.forepojo.*;
import com.xz.app.packag.InfromHelper;
import com.xz.app.pojo.*;
import com.xz.app.service.*;
import com.xz.app.util.OSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xz.app.packag.MapHelper;
import com.xz.app.packag.PageUtil;

@Api(tags = "用户操作接口")
@Controller
@RequestMapping(value="")
@SuppressWarnings("unchecked")
public class UserController {

	Base64.Encoder encoder = Base64.getEncoder();
	@Autowired
	private UserService userService;
    @Autowired
    private CircleMemberService circleMemberService;
	@Autowired
	private ReadcardService readcardService;
	@Autowired
	private EspAttenService espAttenService ;
	@Autowired
	private CyInforService cyInforService;
	@Autowired
	private CylikeService cylikeService;
	@Autowired
	private CyDetailService cyDetailService;
	@Autowired
	private CycollectService cycollectService;
	@Autowired
	private ReviewInforService  reviewInforService;
	@Autowired
	private Inform_inforService inform_inforService;
	@Autowired
	private ReviewdetailService reviewdetailService;
	@Autowired
	UserdataService userdataService ;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ApplyInviteService applyInviteService;
	@Autowired
	private CircleinforService circleinforService;


	@ApiOperation(value = "我的信息", notes = "我的信息")
	@RequestMapping(value="myInformation", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> myInformation(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token) throws Exception{

		        Map<String, Object> map = new HashMap<>();

				int userId = redisService.getUserId(token);
				Users u = userService.get(userId);
				Userdata userdata =userdataService.get(userId);

				ForeUserInfor foreUserInfor = new ForeUserInfor();
				foreUserInfor.setAuthor_id(userId);
				foreUserInfor.setAuthor_image(OSSUtil.getPrefixURL("headImg/") +u.getHeadimg()+".jpg");
				foreUserInfor.setAuthor_name(u.getName());
				foreUserInfor.setPublishs(cyInforService.listByUserid(userId));
				foreUserInfor.setCollects(cycollectService.listByUserid(userId));
				foreUserInfor.setDefaultInfluence(100);
				foreUserInfor.setSex(userdata.getSex());
				map=MapHelper.success();
				map.put("user", foreUserInfor);
				return map;
	}


	@ApiOperation(value = "用户信息更新", notes = "用户信息更新")
	@RequestMapping(value="updateMessage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadMessage(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token,
			@RequestParam("name") @ApiParam(value = "名字") String name,
			@RequestParam("sex") @ApiParam(value = "性别") byte sex,
            @RequestParam(value = "introduction", required = false) @ApiParam(value = "介绍")String introduction,
            @RequestParam(value = "email", required = false) @ApiParam(value = "email") String email,
			@RequestParam(value = "hasFile", required = false, defaultValue = "0") @ApiParam(value = "头像文件") byte hasFile) throws Exception {
			Map<String, Object> map = new HashedMap();

			int userId = redisService.getUserId(token);

			BigDecimal balance =new BigDecimal("0");
			Users u = userService.get(userId);
			if(InfromHelper.isInform(name)==1){
				return MapHelper.setInfor("0", "011", "名字包含敏感信息");
			}
			Userdata userdata = userdataService.get(userId);
			if(userdata==null){
				userdata=new Userdata();
				userdata.setUserid(userId);
				userdata.setSex(sex);
                if(introduction!=null){
                    userdata.setIntroduction(introduction);
                }
                if(email!=null){
                    userdata.setEmail(email);
                }
				userdataService.add(userdata);
			}else{
                if(introduction!=null){
                    userdata.setIntroduction(introduction);
                }
                if(email!=null){
                    userdata.setEmail(email);
                }
                userdata.setSex(sex);
				userdataService.update(userdata);
			}

			if(hasFile == 1){
				//xu弄个文件夹存文件
				u.setHeadimg(String.valueOf(userId));
				map.put("filename", u.getHeadimg() + ".jpg");
			}

			u.setName(name);
			u.setBalance(balance);
			userService.update(u);

			map.putAll(MapHelper.success());
			return map;
	}

	@ApiOperation(value = "用户首次信息更新", notes = "用户首次信息更新")
	@RequestMapping(value="updateFristMessage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateFristMessage(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token,
			@RequestParam("name") @ApiParam(value = "名字") String name,
			@RequestParam("sex") @ApiParam(value = "性别") byte sex,
			@RequestParam(value = "introduction", required = false) @ApiParam(value = "介绍")String introduction,
			@RequestParam(value = "email", required = false) @ApiParam(value = "email") String email)throws Exception {
			int userid = redisService.getUserId(token);

			BigDecimal balance =new BigDecimal("0");
			Users u = userService.get(userid);

			Userdata userdata = userdataService.get(userid);
			if(userdata==null){
				userdata=new Userdata();
				userdata.setUserid(userid);
				userdata.setSex(sex);
                if(introduction!=null){
                    userdata.setIntroduction(introduction);
                }
                if(email!=null){
                    userdata.setEmail(email);
                }
                userdataService.add(userdata);
			}else{
				userdata.setSex(sex);
                if(introduction!=null){
                    userdata.setIntroduction(introduction);
                }
                if(email!=null){
                    userdata.setEmail(email);
                }
                userdataService.update(userdata);
			}

			u.setHeadimg(String.valueOf(userid));
			u.setName(name);
			u.setBalance(balance);
			userService.update(u);
			return MapHelper.success();
	}


	@ApiOperation(value = "用户信息更新纯文字", notes = "用户信息更新纯文字")
	@RequestMapping(value="updateTxtMessage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateTxtMessage(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token,
			@RequestParam("name") @ApiParam(value = "名字") String name,
			@RequestParam("sex") @ApiParam(value = "性别") byte sex,
			@RequestParam(value = "introduction", required = false) @ApiParam(value = "介绍")String introduction,
			@RequestParam(value = "email", required = false) @ApiParam(value = "email") String email) throws Exception {
			int userId = redisService.getUserId(token);

			BigDecimal balance =new BigDecimal("0");
			Users u = userService.get(userId);

			Userdata userdata = userdataService.get(userId);
			if(userdata==null){
				userdata=new Userdata();
				userdata.setUserid(userId);
				userdata.setSex(sex);
                if(introduction!=null){
                    userdata.setIntroduction(introduction);
                }
                if(email!=null){
                    userdata.setEmail(email);
                }
                userdataService.add(userdata);
			}else{
                if(introduction!=null){
                    userdata.setIntroduction(introduction);
                }
                if(email!=null){
                    userdata.setEmail(email);
                }
                userdata.setSex(sex);
				userdataService.update(userdata);
			}
			u.setName(name);
			u.setBalance(balance);
			userService.update(u);
			return MapHelper.success();
	}


	@ApiOperation(value = "查看影响力", notes = "查看影响力")
	@RequestMapping(value="checkInfluence", method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object>  lookInfluence(
    		@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token,
    		@RequestParam("circleId") @ApiParam(value = "圈子ID") int circleId) throws Exception{

		    Map<String, Object> map = new HashMap<>();
			int userId = redisService.getUserId(token);

			CircleMember circleMember =circleMemberService.get(circleId,userId);
			if(circleMember==null){
				return MapHelper.setInfor("0", "006", "未关注圈子");
			}
			map=MapHelper.success();
			map.put("influence", circleMember.getInfluence());
			return map;
	}

	@ApiOperation(value = "用户查看用户", notes = "用户查看用户")
	@RequestMapping(value="seeUser", method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object>  seeUser(
    		@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token,
    		@RequestParam("otherId")  @ApiParam(value = "被查看用户的ID")int otherId) throws Exception{

			int userId = redisService.getUserId(token);
			ForeUsers foreUsers = userService.full(userService.get(otherId));
			foreUsers.setSex(userdataService.get(userId).getSex());
        Map map = MapHelper.success();
        map.put("user", foreUsers);
        //xu名片推送
        map.put("has_send", readcardService.isExist(new ReadcardKey(userId, otherId)));
			map.put("has_foucs",espAttenService.isExist(new Esp_attenKey(userId, otherId)));
			return map;
	}

	@ApiOperation(value = "特别关注某人", notes = "特别关注某人")
	@RequestMapping(value="espAttenSb", method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object>  espAttenSb(
    		@RequestHeader(value="token") @ApiParam(value = "用户登陆权限")String token,
    		@RequestParam("touserId") @ApiParam(value = "被关注的ID") int touserId){

		    Map<String, Object> map = new HashMap<>();
			int userId = redisService.getUserId(token);
			Esp_attenKey attenKey= new Esp_attenKey();

			attenKey.setAtten(touserId);
			attenKey.setUserid(userId);

			if(espAttenService.get(attenKey).size()==0){
				espAttenService.add(attenKey);
				return MapHelper.success();
			}
			else{
				map.put("status", "0");
				map.put("errmsg", "006");
				return MapHelper.setInfor( "0", "005");
			}
	}

    @ApiOperation(value = "取消特别关注某人", notes = "取消特别关注某人-")
    @RequestMapping(value="calEspAttenSb", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object>  calEspAttenSb(
            @RequestHeader(value="token")@ApiParam(value = "用户登陆权限")  String token,
            @RequestParam("touserId") @ApiParam(value = "被取消关注点ID") int touserId){

        Map<String, Object> map = new HashMap<>();
        int userId = redisService.getUserId(token);
        Esp_attenKey attenKey= new Esp_attenKey();

        attenKey.setAtten(touserId);
        attenKey.setUserid(userId);

        if(espAttenService.get(attenKey).size()!=0){
            espAttenService.delete(attenKey);
            return MapHelper.success();
        }
        else{
            map.put("status", "0");
            map.put("errmsg", "005");
            return MapHelper.setInfor( "0", "005");
        }
    }

	@ApiOperation(value = "特别关注", notes = "特别关注")
	@RequestMapping(value="espAtten", method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object>  espAtten(
    		@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token) throws Exception{

		    Map<String, Object> map = new HashMap<>();
			int userId = redisService.getUserId(token);

			List<Esp_attenKey> attenKeys= espAttenService.list(userId);

		    List<List<Cyinfor> >list= new ArrayList<>();

		    List<Cyinfor> cylist= new ArrayList<>();

			for(Esp_attenKey o : attenKeys){
				list.add(cyInforService.listByUserId(o.getAtten()));
			}

			for(List<Cyinfor> o:list){
				for(Cyinfor p :o){
					cylist.add(p);
				}
			}

			List<ForeNews> foreNew=cyDetailService.full(cylist,userId);

			map=MapHelper.success();
			map.put("News",foreNew);
			return map;

	}


	@ApiOperation(value = "我的消息", notes = "我的消息")
	@RequestMapping(value="myMessage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> myMessage(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token){
		int userId = redisService.getUserId(token);
		Map<String, Object> map=MapHelper.success();
		//xu upMessage是否被点赞（1/0）、commentMessage是否被评论(1/0)、careMessage特别关注的动态（1/0）、cardMessage是否收到名片、collectMessage是否被收藏
		map.put("careMessage",redisService.getEspeciallyCareFromRedis(userId));
		map.put("commentMessage",redisService.isCommentsAndAnswer(userId));
		map.put("upMessage", redisService.isUpNotice(userId));
		map.put("cardMessage",redisService.isExistCardinRedis(userId));
		return map;
	}

	@ApiOperation(value = "我的评论", notes = "我的评论")
	@RequestMapping(value="myComment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> myComment(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token,
			@RequestParam("startPage") @ApiParam(value = "起始页") Integer start) throws Exception {
		int userid =redisService.getUserId(token);
		List<Reviewinfor> reviewinfors = reviewInforService.getByAuthor(userid);

		List<ForeReview> reviewdetails = reviewdetailService.full(reviewinfors,userid);

		PageHelper.offsetPage(start * 10, 10);// 设置下次查询10条信息
		int total = (int) new PageInfo<>(reviewdetails).getTotal();
		Map<String, Object> map = MapHelper.success();
		map.put("reviews", reviewdetails);
		map.put("page",PageUtil.getPage(total, reviewdetails.size(), start));
		return map;
	}

	@ApiOperation(value = "我被点赞信息", notes = "我被点赞信息")
	@RequestMapping(value="myPraise", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> myPraise(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token,
			@RequestParam("startPage") @ApiParam(value = "起始页") int start) throws Exception {

		Map<String, Object> map = MapHelper.success();
		PageHelper.offsetPage(start * 10, 10);// 设置下次查询10条信息
		List<ForePraise> foreNews=cylikeService.getPraise(redisService.getUserId(token));
		int total = (int) new PageInfo<>(foreNews).getTotal();
		map.put("praise",foreNews);
		map.put("page",PageUtil.getPage(total, foreNews.size(), start));
		return map;
	}


    @ApiOperation(value ="我的违规消息",notes = "我的违规消息")
    @RequestMapping(value ="myViolationNotice",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> myViolationNotice(@RequestHeader(value = "token") @ApiParam(value = "用户登录权限") String token) throws  Exception{

		Map<String,Object> map=new HashMap<>();
		int userId=redisService.getUserId(token);
		List<Inform_infor> list=userService.selectViolationNotice(inform_inforService.listByUserId(userId));
		List<ForeInformInfor> noticelist=new ArrayList<>();
		for(Inform_infor example:list){
			Cyinfor cyinfor=cyInforService.get(example.getCyid());
			Cydetail cydetail=cyDetailService.get(example.getCyid());
			ForeNews foreNews=new ForeNews(cyinfor,cydetail);
			ForeInformInfor foreInformInfor=new ForeInformInfor(foreNews,example.getType(),example.getWrite_time(),
					example.getIs_banned(),example.getUserid());
			noticelist.add(foreInformInfor);
		}
		map.put("violation",noticelist);
		return  map;
	}


	@ApiOperation(value = "我的收藏消息",notes = "我的收藏消息")
	@RequestMapping(value = "myCollectionNotice",method = RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> myCollection(
			@RequestHeader(value = "token") @ApiParam(value = "用户登录权限") String token) throws Exception{
		Map<String, Object> map = new HashMap<>();
		int userId=redisService.getUserId(token);
		List<Cycollect> list = cycollectService.listCycollectByUserid(userId);
		List<Cydetail> list1 = new ArrayList<>();
		for(Cycollect cycollect:list){
			Cydetail cydetail =cyDetailService.get(cycollect.getCyid());
			list1.add(cydetail);
		}
		map.put("cycollectnotice",list);
		map.put("cydetail",list1);
		return  map;
	}

	@ApiOperation(value = "我的待审核消息",notes = "我的待审核消息")
	@RequestMapping(value = "mycheckNotice",method = RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> mycheckNotice(@RequestHeader(value = "token") @ApiParam(value = "用户登录权限") String token) throws Exception{
		Map<String, Object> map = new HashMap<>();
		int userId=redisService.getUserId(token);

		List<Applyinvite> list = applyInviteService.getApplyByUserId(userId);
		List<CircleInfor> circleInforList = new ArrayList<>();
 		for(Applyinvite applyinvite:list){
			CircleInfor circleInfor = circleinforService.get(applyinvite.getCircleid());
			circleInforList.add(circleInfor);
		}
		List<ForeCircleInfor> foreCircleInfors = circleinforService.full(circleInforList,userId);
		map.put("mycheck",list);
		map.put("circle",foreCircleInfors);
		return  map;
	}


	@ApiOperation(value = "编辑名片",notes = "编辑名片")
	@RequestMapping(value = "mycardMessage",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> mycardMessage(@RequestHeader(value = "token") @ApiParam("用户登陆权限") String token,
											@RequestParam(value = "name") String name,
											@RequestParam(value = "email") String email,
											@RequestParam(value = "wxid") String wxid) throws Exception{

		Map<String, Object> map = new HashMap<>();
		int userId = redisService.getUserId(token);
		Users users = userService.get(userId);
		users.setName(name);
		userService.update(users);
		Userdata userdata = userdataService.get(userId);
		userdata.setEmail(email);
		userdata.setWxid(wxid);
		userdataService.update(userdata);
		return MapHelper.success();
	}


	@ApiOperation(value = "我的名片", notes = "我的名片")
	@RequestMapping(value="myIdCard", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  myIdCard(
			@RequestHeader(value="token")@ApiParam(value = "用户登陆权限") String token) throws Exception{

		Map<String, Object> map = new HashMap<>();

		int userId = redisService.getUserId(token);

		ForeCard card = userService.full(userId);

		map=MapHelper.success();
		map.put("user", card);
		return map;
	}

	@ApiOperation(value = "发送名片", notes = "发送名片")
	@RequestMapping(value="sendIdCard", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  sendIdCard(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限")String token,
			@RequestParam("touserId") @ApiParam(value = "接受名片的ID") int touserId){

		ReadcardKey readcardKey = new ReadcardKey();

		int userId = redisService.getUserId(token);

		readcardKey.setFromid(userId);
		readcardKey.setToid(touserId);

		if(readcardService.get(readcardKey).size()==0){
			readcardService.add(readcardKey);
			redisService.addCardToRedis(touserId);
		}
		return MapHelper.success();
	}
	@ApiOperation(value = "检查收到的名片", notes = "检查收到的名片")
	@RequestMapping(value="checkCard", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  checkCard(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token){

		Map<String, Object> map = new HashMap<>();

		int userId = redisService.getUserId(token);

		if(redisService.isExistCardinRedis(userId)==1){//改
			map=MapHelper.success();
			map.put("has_msg",1);
			return map;
		}
		map=MapHelper.success();
		map.put("has_msg",0);
		return map;
	}


	@ApiOperation(value = "发送的名片", notes = "发送的名片")
	@RequestMapping(value="mysendIdCard", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  sendCard(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token) throws Exception{

		Map<String, Object> map = new HashMap<>();

		int userId = redisService.getUserId(token);

		List<ReadcardKey> list =readcardService.list2(userId);

		List<ForeCard> card = new ArrayList<>();

		for(ReadcardKey o:list){
			card.add(userService.full(o.getToid()));
		}
		map=MapHelper.success();
		map.put("card_list",card);
		return map;
	}

	@ApiOperation(value = "收到的名片", notes = "收到的名片")
	@RequestMapping(value="receiveIdCard", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  receiveIdCard(
			@RequestHeader(value="token") @ApiParam(value = "用户登陆权限") String token) throws Exception{

		Map<String, Object> map = new HashMap<>();

		int userId = redisService.getUserId(token);

		List<ReadcardKey> list =readcardService.list(userId);

		List<ForeCard> card = new ArrayList<>();

		for(ReadcardKey o:list){
			card.add(userService.full(o.getFromid()));
		}

		map=MapHelper.success();
		map.put("card_list",card);
		return map;
	}

	@ApiOperation(value = "把文章添加到我的主页", notes = "把文章添加到我的主页")
	@RequestMapping(value="addCyToZone", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addCyToZone(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token,
										   @RequestParam("cyId") @ApiParam(value = "文章ID") long cyId) throws Exception {
		int userId = redisService.getUserId(token);

		Cyinfor cyinfor = cyInforService.get(cyId);

		//确认用户操作的是自己的文章
		if (cyinfor.getUserid() == userId) {
			cyinfor.setTo_zone((byte) 1);
			cyInforService.update(cyinfor);
		} else throw new XZException("009", "你不是这篇传阅的发布者");


		return MapHelper.success();
	}

	@ApiOperation(value = "查询用户是否关注公众号", notes = "查询用户是否关注公众号")
	@RequestMapping(value="isFocus", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> isFocus(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) throws Exception {
		int userId = redisService.getUserId(token);

		Users users = userService.get(userId);
		Map map = MapHelper.success();
		if(users.getOa_openid() == null) {
			map.put("isFocus",false);
		}else map.put("isFocus",true);

		return map;
	}
}
