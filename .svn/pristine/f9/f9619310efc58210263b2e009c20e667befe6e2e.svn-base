package com.anjoyo.anjoyoqiushibaike;

import java.util.List;

import com.anjoyo.info.UserInfo;
import com.anjoyo.model.Model;
import com.anjoyo.net.ThreadPoolUtils;
import com.anjoyo.thread.HttpPostThread;
import com.anjoyo.utils.MyJson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private ImageView mClose;
	private RelativeLayout mLogin, mWeibo, mQQ;
	private EditText mName, mPassword;
	private TextView mRegister;
	private String NameValue = null;
	private String PasswordValue = null;
	private String url = null;
	private String value = null;
	private MyJson myJson = new MyJson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView() {
		mClose = (ImageView) findViewById(R.id.loginClose);
		mLogin = (RelativeLayout) findViewById(R.id.login);
		mWeibo = (RelativeLayout) findViewById(R.id.button_weibo);
		mQQ = (RelativeLayout) findViewById(R.id.buton_qq);
		mName = (EditText) findViewById(R.id.Ledit_name);
		mPassword = (EditText) findViewById(R.id.Ledit_password);
		mRegister = (TextView) findViewById(R.id.register);
		MyOnClickListener my = new MyOnClickListener();
		mClose.setOnClickListener(my);
		mLogin.setOnClickListener(my);
		mWeibo.setOnClickListener(my);
		mQQ.setOnClickListener(my);
		mRegister.setOnClickListener(my);

	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			switch (mId) {
			case R.id.loginClose:
				finish();
				break;
			case R.id.login:
				NameValue = mName.getText().toString();
				PasswordValue = mPassword.getText().toString();
				Log.e("qianpengyu", "NameValue" + NameValue + "  PasswordValue"
						+ PasswordValue);
				if (NameValue.equalsIgnoreCase(null)
						|| PasswordValue.equalsIgnoreCase(null)
						|| NameValue.equals("") || PasswordValue.equals("")) {
					Toast.makeText(LoginActivity.this, "别闹,先把帐号密码填上", 1).show();
				} else {
					login();
				}
				break;
			case R.id.button_weibo:
				break;
			case R.id.buton_qq:
				break;
			case R.id.register:
				Intent intent = new Intent(LoginActivity.this,
						RegistetActivity.class);
				startActivityForResult(intent, 1);

			}

		}

	}

	private void login() {
		url = Model.LOGIN;
		value = "{\"uname\":\"" + NameValue + "\",\"upassword\":\""
				+ PasswordValue + "\"}";
		Log.e("qianpengyu", value);
		ThreadPoolUtils.execute(new HttpPostThread(hand, url, value));
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(LoginActivity.this, "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(LoginActivity.this, "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				Log.e("qiangpengyu", result);
				if (result.equalsIgnoreCase("NOUSER")) {
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(LoginActivity.this, "用户名不存在", 1).show();
					return;
				} else if (result.equalsIgnoreCase("NOPASS")) {
					mPassword.setText("");
					Toast.makeText(LoginActivity.this, "密码错误", 1).show();
					return;
				} else if (result != null) {
					Toast.makeText(LoginActivity.this, "登录成功", 1).show();
					List<UserInfo> newList = myJson.getNearUserList(result);
					if (newList != null) {
						Model.MYUSERINFO = newList.get(0);
					}
					Intent intent = new Intent(LoginActivity.this,
							UserInfoActivity.class);
					Bundle bund = new Bundle();
					bund.putSerializable("UserInfo", Model.MYUSERINFO);
					intent.putExtra("value", bund);
					startActivity(intent);
					finish();
				}
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 2 && data != null) {
			NameValue = data.getStringExtra("NameValue");
			mName.setText(NameValue);
		}
	}
}