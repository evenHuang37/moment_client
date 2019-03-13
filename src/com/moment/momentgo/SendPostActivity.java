 package com.moment.momentgo;

import com.moment.model.Model;
import com.moment.net.ThreadPoolUtils;
import com.moment.thread.HttpPostThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SendPostActivity extends Activity{
	private ImageView mClose, mSend;
	private EditText mBTime, mETime, mBAddress, mEAddress, mSaying;
	private String data="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sendpost);
		initView();
	}

	private void initView() {
		mClose = (ImageView) findViewById(R.id.close);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		mSend = (ImageView) findViewById(R.id.Send);
		mBTime = (EditText) findViewById(R.id.BTime);
		mETime = (EditText) findViewById(R.id.ETime);
		mBAddress = (EditText) findViewById(R.id.BAddress);
		mEAddress = (EditText) findViewById(R.id.EAddress);
		mSaying = (EditText) findViewById(R.id.Saying);
		mClose.setOnClickListener(mOnclickListener);
		mSend.setOnClickListener(mOnclickListener);
	}
	
	private class MyOnclickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int ID = v.getId();
			switch (ID) {
			case R.id.close:
				SendPostActivity.this.finish();
				break;
			case R.id.Send:
				if(Model.USERHEADURL != null) {
					senMeth();
				} else {
					Intent intent = new Intent(SendPostActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			}
		}

		private void senMeth() {
			String uid = Model.MYUSERINFO.getUserid();
			String pbeginTime = mBTime.getText().toString();
			String pendTime = mETime.getText().toString();
			String pstartAddress = mBAddress.getText().toString();
			String ptravalAddress = mEAddress.getText().toString();
			String psaying = mSaying.getText().toString();
			if(pbeginTime.equals("")) {
				Toast.makeText(SendPostActivity.this, "开始时间不能为空", 1).show();
				return;
			} else if(pendTime.equals("")) {
				Toast.makeText(SendPostActivity.this, "结束时间不能为空", 1).show();
				return;
			} else if(pstartAddress.equals("")) {
				Toast.makeText(SendPostActivity.this, "出发地不能为空", 1).show();
				return;
			} else if(ptravalAddress.equals("")) {
				Toast.makeText(SendPostActivity.this, "目的地不能为空", 1).show();
				return;
			}
			String Json = "{\"uid\":\"" + uid + "\"," + "\"pbeginTime\":\"" + pbeginTime + "\","
					+ "\"pendTime\":\"" + pendTime + "\"," + "\"pstartAddress\":\"" + pstartAddress
					+ "\"," + "\"ptravalAddress\":\"" + ptravalAddress + "\"," + "\"psaying\":\""
					+ psaying + "\"}";
			ThreadPoolUtils.execute(new HttpPostThread(hand, Model.ADDPOST, Json));
			SendPostActivity.this.finish();
			
		}
		
		Handler hand = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				if(msg.what == 200) {
					String result = (String) msg.obj;
					if (result !=null && result.equals("ok")) {
						Toast.makeText(SendPostActivity.this, "发帖成功", 1).show();
						SendPostActivity.this.finish();
					}else {
						Toast.makeText(SendPostActivity.this, result, 1).show();
					}
				}
			};
		};
		
	}

}
