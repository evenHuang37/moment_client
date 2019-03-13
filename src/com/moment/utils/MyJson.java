package com.moment.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.moment.info.AshamedInfo;
import com.moment.info.CommentsInfo;
import com.moment.info.PostInfo;
import com.moment.info.UserInfo;

/**
 * Json字符串解析工具类
 * 
 * @author 苦涩
 */

public class MyJson {

	// 解析糗事方法
	public List<AshamedInfo> getAshamedList(String value) {
		List<AshamedInfo> list = null;
		try {
			JSONArray jay = new JSONArray(value);
			list = new ArrayList<AshamedInfo>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				AshamedInfo info = new AshamedInfo();
				info.setQid(job.getString("QID"));
				info.setUid(job.getString("UID"));
				info.setTid(job.getString("TID"));
				info.setQimg(job.getString("QIMG"));
				info.setQvalue(job.getString("QVALUE"));
				info.setQlike(job.getString("QLIKE"));
				info.setQunlike(job.getString("QUNLIKE"));
				info.setQshare(job.getString("QSHARE"));
				info.setUname(job.getString("UNAME"));
				info.setUhead(job.getString("UHEAD"));
				info.setAddress(job.getString("ADDRESS"));
				info.setTime(job.getString("TIME"));
				list.add(info);
			}
		} catch (JSONException e) {
		}
		return list;
	}
	
	//解析找驴友消息贴的方法
	public List<PostInfo> getPostList(String value){
		List<PostInfo> list = null;
		try{
			JSONArray jay = new JSONArray(value);
			list = new ArrayList<PostInfo>();
			for (int i = 0; i< jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				PostInfo info = new PostInfo();
				info.setPid(job.getString("PID"));
				info.setUid(job.getString("UID"));
				info.setUname(job.getString("UNAME"));
				info.setUage(job.getString("UAGE"));
				info.setUhead(job.getString("UHEAD"));
				info.setPtime(job.getString("PTIME"));
				info.setPbeginTime(job.getString("PBEGINTIME"));
				info.setPendTime(job.getString("PENDTIME"));
				info.setPstartAddress(job.getString("PSTARTADDRESS"));
				info.setPtravalAddress(job.getString("PTRAVALADDRESS"));
				info.setPsaying(job.getString("PSAYING"));
				list.add(info);
			}
		} catch (JSONException e){
		}
		return list;
		
	}

	// 解析糗事评论的方法
	public List<CommentsInfo> getAhamedCommentsList(String value) {
		List<CommentsInfo> list = null;
		try {
			JSONArray jay = new JSONArray(value);
			list = new ArrayList<CommentsInfo>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				CommentsInfo info = new CommentsInfo();
				info.setCid(job.getString("CID"));
				info.setCvalue(job.getString("CVALUE"));
				info.setQid(job.getString("QID"));
				info.setUid(job.getString("UID"));
				info.setCtime(job.getString("CTIME"));
				info.setUname(job.getString("UNAME"));
				info.setUhead(job.getString("UHEAD"));
				list.add(info);
			}
		} catch (JSONException e) {
		}
		return list;
	}

	// 解析附近用户的方法
	public List<UserInfo> getNearUserList(String result) {
		List<UserInfo> list = null;
		try {
			JSONArray jay = new JSONArray(result);
			list = new ArrayList<UserInfo>();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				UserInfo info = new UserInfo();
				info.setUserid(job.getString("USERID"));
				info.setUname(job.getString("UNAME"));
				info.setUhead(job.getString("UHEAD"));
				info.setUage(job.getString("UAGE"));
				info.setUhobbles(job.getString("UHOBBIES"));
				info.setUplace(job.getString("UPLACE"));
				info.setUexplain(job.getString("UEXPLAIN"));
				info.setUtime(job.getString("UTIME"));
				info.setUsex(job.getString("USEX"));
				list.add(info);
			}
		} catch (JSONException e) {
		}
		return list;
	}
	

}
