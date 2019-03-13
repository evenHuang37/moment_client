package com.anjoyo.anjoyoqiushibaike;

import com.anjoyo.model.Model;
import com.anjoyo.net.ThreadPoolUtils;
import com.anjoyo.thread.HttpPostThread;

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

public class RegistetActivity extends Activity {
	private ImageView mClose;
	private EditText mName, mPassword;
	private RelativeLayout mNext;
	private TextView mStartlogin;
	private String url = null;
	private String value = null;
	private String username = null;
	private String password = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
		mClose = (ImageView) findViewById(R.id.registClose);
		mName = (EditText) findViewById(R.id.Redit_name);
		mPassword = (EditText) findViewById(R.id.Redit_password);
		mNext = (RelativeLayout) findViewById(R.id.next);
		mStartlogin = (TextView) findViewById(R.id.startlogin);
		MyOnClickListener my = new MyOnClickListener();
		mClose.setOnClickListener(my);
		mNext.setOnClickListener(my);
		mStartlogin.setOnClickListener(my);

	}

	private void reginstet() {
		url = Model.REGISTET;
		value = "{\"uname\":\"" + username + "\",\"upassword\":\"" + password
				+ "\"}";
		Log.e("qianpengyu", value);
		ThreadPoolUtils.execute(new HttpPostThread(hand, url, value));
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			switch (mId) {
			case R.id.registClose:
				finish();
				break;
			case R.id.next:
				username = mName.getText().toString();
				password = mPassword.getText().toString();
				if (!username.equalsIgnoreCase(null)
						&& !password.equalsIgnoreCase(null)
						&& !username.equals("") && !password.equals("")) {
					reginstet();// 请求注册功能
				} else {
					Toast.makeText(RegistetActivity.this, "请先填写用户名或密码", 1)
							.show();
				}
				break;
			case R.id.startlogin:
				Intent intent = new Intent(RegistetActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}

		}
	}

	// INSERT INTO `quser`( `uname`, `upassword`) VALUES ('111111','111111');
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(RegistetActivity.this, "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(RegistetActivity.this, "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				Log.e("qiangpengyu", "result:" + result);
				if (result.equals("ok")) {
					Toast.makeText(RegistetActivity.this, "注册成功,请登陆", 1).show();
					Intent intent = new Intent();
					intent.putExtra("NameValue", username);
					intent.putExtra("PasswordValue", password);
					setResult(2, intent);
					finish();
				} else if (result.trim().equals("no")) {
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegistetActivity.this, "用户名以存在,请重新注册", 1)
							.show();
					return;
				} else {
					mName.setText("");
					mPassword.setText("");
					Toast.makeText(RegistetActivity.this, "注册失败", 1).show();
					return;
				}

			}
		};
	};
}
