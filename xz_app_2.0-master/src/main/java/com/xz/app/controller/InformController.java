package com.xz.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.xz.app.forepojo.ForeInformInfor;
import com.xz.app.forepojo.ForeNews;
import com.xz.app.packag.MapHelper;
import com.xz.app.packag.XzAppUtil;
import com.xz.app.pojo.Cydetail;
import com.xz.app.pojo.Cyinfor;
import com.xz.app.pojo.Illegal_user;
import com.xz.app.pojo.Infor_fake;
import com.xz.app.pojo.Inform_infor;
import com.xz.app.pojo.Inform_record;
import com.xz.app.pojo.Volun_record;
import com.xz.app.service.CyDetailService;
import com.xz.app.service.CyInforService;
import com.xz.app.service.Illegal_userService;
import com.xz.app.service.Infor_fakeService;
import com.xz.app.service.Inform_inforService;
import com.xz.app.service.Inform_recordService;
import com.xz.app.service.RedisService;
import com.xz.app.service.VolunRecordService;

/**
 * @ClassName: InformController
 * @Description:TODO 违禁相关操作
 * @author: 杨瑞
 * @date: 2018年6月6日
 * 
 * @Copyright: 2018 www.oijkl.com Inc. All rights reserved.
 */
@SuppressWarnings("unchecked")
@Api(tags = "广告操作接口")
@Controller
@RequestMapping(value="")
public class InformController {

	@Autowired
	private Inform_inforService inform_inforService;
	@Autowired
	private Inform_recordService inform_recordService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private CyInforService cyInforService;
	@Autowired
	private CyDetailService cyDetailService;
	@Autowired
	private Infor_fakeService infor_fakeService;
	@Autowired
	private Illegal_userService illegal_userService;
	@Autowired
	private VolunRecordService volunRecordService;

	@ApiOperation(value = "举报文章", notes = "举报文章")
	@RequestMapping(value="informCyinfor", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> informCy(@RequestHeader(value = "token")@ApiParam(value = "用户登陆权限")  String token,
										@RequestParam("cyId")@ApiParam(value = "文章ID")  long cyid,
										@RequestParam("informerId") @ApiParam(value = "举报者ID") int informerId,
										@RequestParam("type") @ApiParam(value = "举报类型") byte type) {

		Inform_infor infor = inform_inforService.get(cyid, type);

		if (infor == null) {
			infor = new Inform_infor(cyid, type, (byte) 1, (byte) 0, (byte) 0, informerId);

			inform_inforService.add(infor);

			Inform_record inform_record = new Inform_record(informerId, cyid, type);

			inform_record.setType(type);
			
		    inform_recordService.add(inform_record);
		} else {
			infor.setInformed_time((byte) (infor.getInformed_time() + 1));
			inform_inforService.update(infor);
		}
		return MapHelper.success();

	}

	@ApiOperation(value = "志愿信息", notes = "志愿信息")
	@RequestMapping(value="myInformTime", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> myInformTime(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限") String token) {

		int userId = redisService.getUserId(token);
		Illegal_user illegal_user = illegal_userService.get(userId);
		int informTime = 0;
		if(illegal_user!=null) {
			informTime = illegal_user.getIllegal_time();
		}
		int needTime = informTime;// 暂时等于违规的次数
		int volunTime = volunRecordService.getVolunTime(userId);
		Map<String, Object> map = MapHelper.success();
		map.put("inforTime", informTime);
		map.put("needTime", needTime);
		map.put("volunTime", volunTime);
		return map;
	}

	@ApiOperation(value = "志愿信息", notes = "志愿信息")
	@RequestMapping(value="myInformRecord", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> myInformRecord(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限")  String token) throws Exception {

		int userId = redisService.getUserId(token);
		List<Inform_infor> inform_infors = inform_inforService.listByUserId(userId);
		List<ForeInformInfor> foreInformInfors = new ArrayList<>();
		for (Inform_infor o : inform_infors) {
			Cyinfor cyinfor = cyInforService.get(o.getCyid());
			Cydetail cydetail = cyDetailService.get(o.getCyid());
			if(cydetail == null){
				throw new Exception("nullObjectException");
			}

			ForeInformInfor foreInformInfor = new ForeInformInfor();
			foreInformInfor.setForeNews(new ForeNews(cyinfor, cydetail));
			foreInformInfor.setIs_banned(o.getIs_banned());
			foreInformInfor.setType(o.getType());
			foreInformInfor.setWrite_time(o.getWrite_time());
			foreInformInfors.add(foreInformInfor);
		}
		Map<String, Object> map = MapHelper.success();
		map.put("foreInformInfors", foreInformInfors);
		return map;
	}

	@ApiOperation(value = "志愿信息", notes = "志愿信息")
	@RequestMapping(value="volunService", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> volunService(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限")  String token) throws Exception {

		Map<String, Object> map = new HashMap<>();
		int userId = redisService.getUserId(token);

		List<Infor_fake> infor_fakes = new ArrayList<>();
        List<Infor_fake> infor_fake_all = infor_fakeService.listAll();
        
		String answer = new String();

		int i[] = XzAppUtil.getRan(infor_fake_all.size()-1, 6);

		for (int l = 0; l < 6; l++) {
			infor_fakes.add(infor_fake_all.get(i[l]));
			answer = answer + infor_fake_all.get(i[l]).getAnswer();
		}

		redisService.addAnswerToRedis(String.valueOf(userId), answer);

		PageHelper.offsetPage(0 * 4, 4);

		List<Inform_infor> inform_infors = inform_inforService.list();
		List<Cyinfor> cyinfors = new ArrayList<>();

		for (Infor_fake o : infor_fakes) {
			cyinfors.add(cyInforService.get(o.getCyid()));
		}

		for (Inform_infor o : inform_infors) {
			cyinfors.add(cyInforService.get(o.getCyid()));
			redisService.addInformUserToRedis(String.valueOf(userId),
					String.valueOf(cyInforService.get(o.getCyid()).getUserid()));
		}

		List<ForeNews> foreNews = cyDetailService.full(cyinfors, userId);

		map = MapHelper.success();
		map.put("news", foreNews);
		return map;

	}

	@ApiOperation(value = "答案提交", notes = "答案提交")
	@RequestMapping(value="volunAnswer", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> volunAnswer(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限")  String token,
			@RequestParam("informAnswer") String answer) {

		int userId = redisService.getUserId(token);

		int j = 0;

		String str = redisService.getAnswerFromRedis(String.valueOf(userId));

		List<String> strList = redisService.getInformUserFromRedis(String.valueOf(userId));

		char[] answerArray = answer.toCharArray();
		char[] strArray = str.toCharArray();

		for (int i = 0; i < 6; i++) {
			if (answerArray[i] == strArray[i]) {
				j++;
			}
		}
		if (j > 5) {
			for (int i = 0; i < 4; i++) {
				if (answerArray[i + 6] == 1) {
					if (illegal_userService.get(Integer.parseInt(strList.get(i))) == null) {
						Illegal_user illegal_user = new Illegal_user();

						illegal_user.setUserid(Integer.parseInt(strList.get(i)));
						illegal_user.setIllegal_time((byte) 1);
						illegal_user.setFinish_time((byte) 0);
						illegal_userService.add(illegal_user);
					} else {
						Illegal_user illegal_user = illegal_userService.get(Integer.parseInt(strList.get(i)));

						illegal_user.setIllegal_time((byte) (illegal_user.getIllegal_time() + 1));
						illegal_userService.update(illegal_user);
					}
				}
			}
			return MapHelper.success();
		} else {
			return MapHelper.setInfor("0", "010", "Answer-error");
		}

	}

	@ApiOperation(value = "我的服务记录", notes = "我的服务记录")
	@RequestMapping(value="myVolunRecord", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> myVolunRecord(@RequestHeader(value = "token") @ApiParam(value = "用户登陆权限")  String token) {

		int userId = redisService.getUserId(token);
		List<Volun_record> volun_records = volunRecordService.myVolunRecord(userId);
		Map<String, Object> map = MapHelper.success();
		map.put("volun_records", volun_records);
		return map;
	}

}
