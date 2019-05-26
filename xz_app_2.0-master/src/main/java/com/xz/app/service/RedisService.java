package com.xz.app.service;

import java.util.List;

import com.xz.app.exception.NocyinfoException;
import com.xz.app.packag.RedisSave;


public interface RedisService {

	/**
	 * 把模板消息需要的formid存进redis
	 * @param userId
	 * @param formId
	 */
	void addFormIdToRedis(int userId,String formId);

	/**
	 * 获取用户对应的formid
	 * @param userId
	 * @return
	 */
	String getFromId(int userId);
	/** 添加token到redis中
	 *
	 *  @param userId
	 *  @param token
	 */
	void addTokenToRedis(int userId, String token);

    /**从redis中获取公众号Accesstoken 若已经过期则自动获取新的
     *
     * @return AccessToken
     */
    String getAccessToken() throws Exception;
	/**
	 * 添加微信小程序的access_token到redis
	 * @return string
	 * @throws Exception
	 */
	String getAccessToken2() throws Exception;

    /**从redis中获取openId若已经过期则自动获取新的
     *
     * @return AccessToken
     */
    String getOa_openId(int userId) throws Exception;

    void addOa_openId(int UserId, String openId);

    /**从redis中获取token，用于验证用户是否在登录状态
	 *
	 * @param token
	 * @return
	 */
	int getUserId(String token);

	/**将文章和可传量加入到redis中
	 *
	 * @param circleId
	 * @param cyId
	 * @param availableTime
	 */
	void addCyinforToRedis(int circleId, long cyId, int availableTime);

	/**从Redis中获取可供使用到文章id并返回给前端
	 *
	 * @param circleId
	 * @return
	 */
	RedisSave getCyinforFromRedis(int circleId);

	/**添加活着更新文章到可传量
	 *
	 * @param circleId
	 * @param cyids
	 * @param avas
	 */
	void addAvailableTimeToRedis(int circleId, List<String> cyids, List<String> avas);

	/**根据文章的ID获取文章的可传量
	 *
	 * @param circleId
	 * @param cyId
	 * @return
	 */
	int getAvailableTimeFromRedis(int circleId, long cyId);

    /**点赞文章,点击详情,使得文章存活天数有恢复3天
	 *
	 * @param circleId
	 * @param cyId
	 * @param influecnce
	 */
	void upCyinfor(int circleId, long cyId, int influecnce);

	/**添加短信验证码到redis中
	 *
	 * @param phoneNum
	 * @param string
	 */
	void addSmsToRedis(String phoneNum, String string);

	/**从短信redis中获取短信验证码
	 *
	 * @param phoneNum
	 * @return
	 */
	String getSmsFromRedis(String phoneNum);

	/**添加志愿服务到答案到redis中
	 *
	 * @param id
	 * @param answer
	 */
	void addAnswerToRedis(String id, String answer);

	/**获取志愿服务到答案
	 *
	 * @param id
	 * @return
	 */
	String getAnswerFromRedis(String id);

	/**添加志愿者和违禁的用户到redis中
	 *
	 * @param volunterId
	 * @param userId
	 */
	void addInformUserToRedis(String volunterId, String userId);

	/**从redis中获得志愿者处理到违禁用户，并判断是否需要禁止
	 *
	 * @param volunterId
	 * @return
	 */
	List<String> getInformUserFromRedis(String volunterId);

	/**向redis中添加提醒用户被评论或者被回答到通知
	 *
	 * @param userid
	 */
	void addCommentsAndAnswerToRedis(int userid);

	/**检查是否存在有被评论或者被回答的通知
	 *
	 * @param userid
	 * @return
	 */
	int isCommentsAndAnswer(int userid);

	/**向redis中添加点赞通知
	 *
	 * @param userid
	 */
	void addUpNoticeToRedis(int userid);

	/**获得被点赞的通知
	 *
	 * @param userid
	 */
	int isUpNotice(int userid);

	/**添加特别关注的信息到redis中
	 *
	 * @param userid
	 */
	void addEspeciallyCareToRedis(int userid);

	/**获得特别关注的信息
	 *
	 * @param userid
	 */
	String getEspeciallyCareFromRedis(int userid);

	/**添加发送名片的通知到redis
	 *
	 * @param userid
	 */
	void addCardToRedis(int userid);

	/**判断是否收到名片
	 *
	 * @param userid
	 * @return
	 */
	int isExistCardinRedis(int userid);

	void addDadCircleToRedis(long circlecheckid, int dedcircles[]);

	void addCircleaToRedis(long circlecheckid, String circleas);

	void addSonCircleToRedis(long circlecheckid, int soncircles[]);

    void checkCirclecheckRedis(long circlecheckid);

	List<Integer> getDadCircleFromRedis(long circlecheckid);

	List<Integer> getSonCircleFromRedis(long circlecheckid);

	String  getCircleaFromRedis(long circlecheckid);

	List<Integer> getRecoCircleID(int circleID);

	void forbidden(int userID, int circleID);

	boolean isForbidden(int userID);


	void setAPKtoRedis(String version, String url, String description);

	String getAPKUrlfromRedis();

	String getAPKDescriptionfromRedis();

	String getAPKVersionfromRedis();

	void addUPTimetoRedis(int userID);

	boolean isOverTime(int userID);

	void addSearchrecordstoRedis(String circleName);

	void addcyColletcToRedis(int userId);

}
