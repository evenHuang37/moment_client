package com.moment.momentgo;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;

import com.moment.model.Model;
import com.moment.momentgo.R;
import com.moment.momentgo.CameralActivity.IMGCallBack;
import com.moment.momentgo.PhotoAct.IMGCallBack1;
import com.moment.net.ThreadPoolUtils;
import com.moment.thread.HttpPostThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class UploadActivity extends Activity implements
OnItemClickListener,OnClickListener {

	private ImageView mClose, mUpLoadEdit, mCamera, mAlbum;
	private EditText mNeirongEdit;
	private String data = "";
	private TextView showCurrentPosition;
	String addrStr=null;
	
	// 百度地图 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		private LocationMode mCurrentMode;
		BitmapDescriptor mCurrentMarker;

		MapView mMapView;
		BaiduMap mBaiduMap;

		// UI相关
		OnCheckedChangeListener radioButtonListener;
		Button requestLocButton;
		boolean isFirstLoc = true;// 是否首次定位

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(this.getApplication());  
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upload);
		initView();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//百度地图
				mCurrentMode = LocationMode.NORMAL;

				// 地图初始化
				mMapView = (MapView) findViewById(R.id.bmapView);
				mBaiduMap = mMapView.getMap();
				// 开启定位图层
				mBaiduMap.setMyLocationEnabled(true);
				// 定位初始化
				mLocClient = new LocationClient(this);
				mLocClient.registerLocationListener(myListener);
				LocationClientOption option = new LocationClientOption();
				option.setAddrType("all");
				option.setOpenGps(true);// 打开gps
				option.setCoorType("bd09ll"); // 设置坐标类型
				option.setScanSpan(1000);
				mLocClient.setLocOption(option);
				mLocClient.start();
	}

	private void initView() {
		// 获取关闭按钮id
		mClose = (ImageView) findViewById(R.id.close);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		// 发表按钮
		mUpLoadEdit = (ImageView) findViewById(R.id.UpLoadEdit);
		// 相机按钮
		mCamera = (ImageView) findViewById(R.id.camera);
		// 图片按钮
		mAlbum = (ImageView) findViewById(R.id.album);
		mNeirongEdit = (EditText) findViewById(R.id.neirongEdit);
		showCurrentPosition=(TextView)findViewById(R.id.showCurrentPosition);
		showCurrentPosition.setOnClickListener(this);
		mClose.setOnClickListener(mOnclickListener);
		mUpLoadEdit.setOnClickListener(mOnclickListener);
		mCamera.setOnClickListener(mOnclickListener);
		mAlbum.setOnClickListener(mOnclickListener);
		CameralActivity.setIMGcallback(new IMGCallBack() {

			@Override
			public void callback(String data) {
				UploadActivity.this.data = data;
			}
		});
		PhotoAct.setIMGcallback(new IMGCallBack1() {

			@Override
			public void callback(String data) {
				UploadActivity.this.data = data;
			}
		});
	}

	private class MyOnclickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int ID = v.getId();
			switch (ID) {
			case R.id.close:
				UploadActivity.this.finish();
				break;
			case R.id.UpLoadEdit:
				if (Model.MYUSERINFO != null) {
					sendMeth();
				} else {
					Intent intent = new Intent(UploadActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.camera:
				Intent intent = new Intent(UploadActivity.this,
						CameralActivity.class);
				startActivity(intent);
				break;
			case R.id.album:
				Intent intent3 = new Intent(UploadActivity.this, PhotoAct.class);
				startActivity(intent3);
				break;
			case R.id.showCurrentPosition:
				Intent intent4 = new Intent(UploadActivity.this, CurrentLoactionMapActivity.class);
				startActivity(intent4);
				break;
			}
		}
	}
	
	/**
	 * 百度地图   定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			
			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				addrStr = location.getAddrStr();
				showCurrentPosition.setText(addrStr);
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//地图
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
	}

	private void sendMeth() {
		if (mNeirongEdit.getText().toString().equals("")) {
			Toast.makeText(UploadActivity.this, "请先填写糗事文字再提交", 1).show();
			return;
		}
		String uid = Model.MYUSERINFO.getUserid();// 用户ID
		String tid = "1";// 糗事类型ID
		String qvalue = mNeirongEdit.getText().toString();// 糗事内容
		String qimg = "";// 糗事图片
		String address = addrStr;
		if (!data.equalsIgnoreCase("")) {
			qimg = System.currentTimeMillis() + ".png";// 糗事图片
		}
		String Json = "{\"uid\":\"" + uid + "\"," + "\"tid\":\"" + tid + "\","
				+ "\"qimg\":\"" + qimg + "\"," + "\"address\":\"" + address + "\"," + "\"qvalue\":\""  + qvalue
				+ "\"," + "\"qlike\":\"0\"," + "\"qunlike\":\"0\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.ADDVALUE, Json,
				data));
		UploadActivity.this.finish();
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(UploadActivity.this, "糗事发送成功", 1).show();
					UploadActivity.this.finish();
				}
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}

}
