package com.moment.momentgo;



import com.moment.info.UserInfo;
import com.moment.model.Model;
import com.moment.net.ThreadPoolUtils;
import com.moment.thread.HttpPostThread;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ReviseActivity extends Activity {
	private ImageView mBack;
	private UserInfo userInfo=null;
	private ImageView saveInfo,savePsw;
	private EditText nicheng,nianling,qianming,chumodi,like,password;
	private String data = "";
	RadioButton male,female;
	RadioGroup rgSex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reviseinfo);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		userInfo = (UserInfo) bund.getSerializable("UserInfo");
		initview();
		createUserInfo();
	}

	
	private void initview() {
		// TODO Auto-generated method stub
		mBack = (ImageView) findViewById(R.id.img_back);
		nicheng=(EditText) findViewById(R.id.xianshi_nicheng);
		male=(RadioButton) findViewById(R.id.male);
		female=(RadioButton) findViewById(R.id.female);
		rgSex=(RadioGroup) findViewById(R.id.rgSex);
		nianling=(EditText) findViewById(R.id.xianshi_nianling);
		qianming=(EditText) findViewById(R.id.xianshi_qianming);
		chumodi=(EditText) findViewById(R.id.xianshi_chumodi);
		like=(EditText) findViewById(R.id.xianshi_like);
        password=(EditText) findViewById(R.id.xianshi_newpassword);
       

		saveInfo=(ImageView) findViewById(R.id.img_edit_name);
		savePsw=(ImageView) findViewById(R.id.img_edit_password);
		MyOnClick my = new MyOnClick();		
		saveInfo.setOnClickListener(my);
		savePsw.setOnClickListener(my);
		mBack.setOnClickListener(my);
//		CameralActivity.setIMGcallback(new IMGCallBack() {
//
//			@Override
//			public void callback(String data) {
//				ReviseActivity.this.data = data;
//			}
//		});
//		PhotoAct.setIMGcallback(new IMGCallBack1() {
//
//			@Override
//			public void callback(String data) {
//				ReviseActivity.this.data = data;
//			}
//		});

	}

	class MyOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int mId = v.getId();
			switch (mId) {
			case R.id.img_back:
				ReviseActivity.this.finish();
				break;		
			case R.id.img_edit_name:
				updateInfo();				
					Intent intent = new Intent(ReviseActivity.this, UserInfoActivity.class);
					Bundle bund = new Bundle();
					bund.putSerializable("UserInfo", Model.MYUSERINFO);
					intent.putExtra("value", bund);
					startActivity(intent);							
				break;
			case R.id.img_edit_password:
				updatePsw();
				break;
			
			default:
				break;
			}
		}

	}
	private void updateInfo() {
		String userId=userInfo.getUserid();
		String uName=nicheng.getText().toString();	
		String uSex="";
		
		if(male.isChecked()) 	           
		{
			uSex="1";
		}
		else if(female.isChecked())
		{
			uSex="0";			
		}
		System.out.println("111");
		System.out.println(uSex);
		System.out.println("222");
		String uAge=nianling.getText().toString();	
		String uExplain=qianming.getText().toString();		
		String uPlace=chumodi.getText().toString();		
		String uHobbies=like.getText().toString();
		String Json = "{\"userId\":\"" + userId +"\","+"\"uName\":\"" + uName + "\"," + "\"uAge\":\"" + uAge + "\","
					+ "\"uHobbies\":\"" + uHobbies + "\"," + "\"uPlace\":\"" + uPlace +"\"," 
					+ "\"uExplain\":\"" + uExplain +"\"," + "\"uSex\":\"" + uSex +"\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.UPDATEINFO, Json));
		Model.MYUSERINFO.setUname(uName);
		Model.MYUSERINFO.setUsex(uSex);
		Model.MYUSERINFO.setUage(uAge);
		Model.MYUSERINFO.setUexplain(uExplain);
		Model.MYUSERINFO.setUplace(uPlace);
		Model.MYUSERINFO.setUhobbles(uHobbies);


		//ReviseActivity.this.finish();
		
	}
	
	private void updatePsw() {
		String userId=userInfo.getUserid();
		String uPass=password.getText().toString();				
		String Json = "{\"userId\":\"" + userId +"\","+"\"uPass\":\"" + uPass +"\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.UPDATEPSW, Json));
		Toast.makeText(ReviseActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
		//ReviseActivity.this.finish();
		
	}
	
	
	
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(ReviseActivity.this, "修改个人信息成功", 1)
							.show();
//					ReviseActivity.this.finish();
				}
			}
		}
	};
	

	private void createUserInfo() {
		nicheng.setText(userInfo.getUname());
		if (!userInfo.getUsex().equals("null")) {
			if (userInfo.getUsex().equals("0")) {
				female.setChecked(true);
			} else if (userInfo.getUsex().equals("1")) {
				male.setChecked(true);
			}
		}
		if (!userInfo.getUage().equals("null")) {
			nianling.setText(userInfo.getUage());
		}
		if (!userInfo.getUplace().equals("null")) {
			chumodi.setText(userInfo.getUplace());
		}
		if (!userInfo.getUexplain().equals("null")) {
			qianming.setText(userInfo.getUexplain());
		}
				
		if (!userInfo.getUhobbles().equals("null")) {
			like.setText(userInfo.getUhobbles());
		}
					
	}
}
