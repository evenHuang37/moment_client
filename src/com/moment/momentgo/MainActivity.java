package com.moment.momentgo;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.util.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.utils.KFSLog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.moment.fragment.PartnerFragment;
import com.moment.fragment.PartnerFragment.PartnerFragmentCallBack;
import com.moment.fragment.NearFragment;
import com.moment.fragment.NiceFragment;
import com.moment.fragment.NotesFragment;
import com.moment.fragment.FollowFragment;
import com.moment.fragment.FollowFragment.FollowFragmentCallBack;
import com.moment.fragment.IndexFragment;
import com.moment.fragment.IndexFragment.IndexFragmentCallBack;
import com.moment.fragment.NearFragment.NearFragmentCallBack;
import com.moment.fragment.NiceFragment.NiceFragmentCallBack;
import com.moment.fragment.NotesFragment.NotesFragmentCallBack;
import com.moment.info.UserInfo;
import com.moment.model.Model;
import com.moment.momentgo.R;
import com.moment.utils.MyJson;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	private KFSettingsManager mSettingsMgr;
	private View mLeftView;
	// 第三方的抽屉菜单管理工具类
	private SlidingMenu mSlidingMenu;
	//首页的碎片
	private IndexFragment mIndexFragment;
	// 精华的碎片
	private NiceFragment mNiceFragment;
	//关注的碎片
	private FollowFragment mFollowFragment;
	//找驴友的碎片
	private PartnerFragment mPartnerFragment;
	// 小纸条的碎片
	private NotesFragment mNotesFragment;
	// 附近的碎片
	private NearFragment mNearFragment;
	// 定义fragment管理器:向界面增加修改删除fragment
	private FragmentManager mFragmentManager;
	// 获取fragment栈
	private FragmentTransaction mFragmentTransaction;
	private List<Fragment> myFragmentList = new ArrayList<Fragment>();
	// leftView里面的控件
	private LinearLayout mLoginThisAPP;// 登录糗百按钮
	private TextView myUserName;
	private ImageView mSettingBtn;// 设置按钮
	// leftview中下面的按钮
	private RelativeLayout mLeftHot, mLeftNice, mLeftPicture, mLeftCross,
			mLeftScrip, mLeftNear, mLeftCheck; // leftview中下面的按钮
	private int fragmentFlag = 0;
	private MyJson myJson = new MyJson();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//
		mSettingsMgr = KFSettingsManager.getSettingsManager(this);

		// 设置开发者调试模式，默认为true，如要关闭开发者模式，请设置为false
		KFIMInterfaces.enableDebugMode(this, true);
		initView();
		login();
		SharedPreferences sp = MainActivity.this.getSharedPreferences(
				"UserInfo", MODE_PRIVATE);
		String result = sp.getString("UserInfoJson", "none");
		Log.e("SharedPreferencesOld", result);
		if (!result.equals("none")) {
			List<UserInfo> newList = myJson.getNearUserList(result);
			if (newList != null) {
				Model.MYUSERINFO = newList.get(0);
				myUserName.setText(Model.MYUSERINFO.getUname());
			}
		}
	}

	private void initView() {
		mLeftView = View.inflate(MainActivity.this, R.layout.leftview, null);
		mLoginThisAPP = (LinearLayout) mLeftView
				.findViewById(R.id.LoginThisAPP);
		mSettingBtn = (ImageView) mLeftView.findViewById(R.id.SettingBtn);
		mLeftHot = (RelativeLayout) mLeftView.findViewById(R.id.LeftHot);
		mLeftNice = (RelativeLayout) mLeftView.findViewById(R.id.LeftNice);
		mLeftPicture = (RelativeLayout) mLeftView
				.findViewById(R.id.LeftPicture);
		mLeftScrip = (RelativeLayout) mLeftView.findViewById(R.id.LeftScrip);
		mLeftNear = (RelativeLayout) mLeftView.findViewById(R.id.LeftNear);
		mLeftCross = (RelativeLayout) mLeftView.findViewById(R.id.LeftCross);
		mLeftCheck = (RelativeLayout) mLeftView.findViewById(R.id.LeftCheck);
		myUserName = (TextView) mLeftView.findViewById(R.id.myUserName);
		mLoginThisAPP.setOnClickListener(MainActivity.this);
		mSettingBtn.setOnClickListener(MainActivity.this);
		mLeftHot.setOnClickListener(MainActivity.this);
		mLeftNice.setOnClickListener(MainActivity.this);
		mLeftPicture.setOnClickListener(MainActivity.this);
		mLeftScrip.setOnClickListener(MainActivity.this);
		mLeftNear.setOnClickListener(MainActivity.this);
		mLeftCross.setOnClickListener(MainActivity.this);
		mLeftCheck.setOnClickListener(MainActivity.this);
		mLeftHot.setBackgroundResource(R.drawable.side_menu_background_active);
		mIndexFragment = new IndexFragment();
		myFragmentList.add(mIndexFragment);
		mNiceFragment = new NiceFragment();
		myFragmentList.add(mNiceFragment);
		mFollowFragment = new FollowFragment();
		myFragmentList.add(mFollowFragment);
		mPartnerFragment = new PartnerFragment();
		myFragmentList.add(mPartnerFragment);
		mNotesFragment = new NotesFragment();
		myFragmentList.add(mNotesFragment);
		mNearFragment = new NearFragment();
		myFragmentList.add(mNearFragment);
		mSlidingMenu = this.getSlidingMenu();
		this.setBehindContentView(mLeftView);
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mFragmentManager = MainActivity.this.getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		createFargment(5);
		createFargment(1);
		FragmentTransaction mFragmentTransaction = mFragmentManager
				.beginTransaction();
		mFragmentTransaction.replace(R.id.main, mIndexFragment);
		mFragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		int mID = v.getId();
		// MainActivity.this.toggle();
		switch (mID) {
		case R.id.LoginThisAPP:
			if (Model.MYUSERINFO != null) {
				Intent intent = new Intent(MainActivity.this,
						UserInfoActivity.class);
				Bundle bund = new Bundle();
				bund.putSerializable("UserInfo", Model.MYUSERINFO);
				intent.putExtra("value", bund);
				startActivity(intent);
			} else {
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.SettingBtn:
			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.LeftHot:
			createleftviewbg();
			mLeftHot.setBackgroundResource(R.drawable.side_menu_background_active);
			createFargment(1);
			break;
		case R.id.LeftNice:
			createleftviewbg();
			mLeftNice
					.setBackgroundResource(R.drawable.side_menu_background_active);
			createFargment(2);
			break;
		case R.id.LeftPicture:
			if (Model.MYUSERINFO == null) {
				Toast.makeText(MainActivity.this, "请先登录，再查看关注用户的动态哦！", 1).show();
			} else {
				createleftviewbg();
				mLeftPicture
						.setBackgroundResource(R.drawable.side_menu_background_active);
				
				createFargment(3);
			}
			break;
		case R.id.LeftCross:
			createleftviewbg();
			mLeftCross
					.setBackgroundResource(R.drawable.side_menu_background_active);
			createFargment(4);
			break;
		case R.id.LeftScrip:
			if (Model.MYUSERINFO == null) {
				Toast.makeText(MainActivity.this, "请先登录，再查看关注用户的动态哦！", 1).show();
			}else {
				createleftviewbg();
				mLeftScrip
						.setBackgroundResource(R.drawable.side_menu_background_active);
				createFargment(5);
			}
			break;
		case R.id.LeftNear:
			if (Model.MYUSERINFO == null) {
				Toast.makeText(MainActivity.this, "请先登录，再查看关注用户的动态哦！", 1).show();
			}else {
				createleftviewbg();
				mLeftNear
						.setBackgroundResource(R.drawable.side_menu_background_active);
				createFargment(6);
			}
			break;
		case R.id.LeftCheck:
			createleftviewbg();
			mLeftCheck
					.setBackgroundResource(R.drawable.side_menu_background_active);
			// 这里是跳转到审核糗事的activity中
			Intent intent1 = new Intent(MainActivity.this, AuditActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	// 设置右边的fragment加载的控件
	private void createFargment(int flag) {
		// 如果正在加载的fragment是传过来的 那么就不操作否则就去加载
		MainActivity.this.toggle();
		if (fragmentFlag != flag) {
			switch (flag) {
			case 1:// 首页的Fragment
				indexFragmentCallBack();
				break;
			case 2:// 精选的Fragment
				niceFragmentCallBack();
				break;
			case 3:// 关注的Fragment
				followFragmentCallBack();
				break;
			case 4:// 找驴友的Fragment
				partnerFragmentCallBack();
				break;
			case 5:// 小纸条的Fragment
				notesfragmentCallBack();
				break;
			case 6:// 关注列表的Fragment
				nearfragmentCallBack();
				break;
			}
			if (fragmentFlag != 0) {
				mFragmentTransaction.remove(myFragmentList
						.get(fragmentFlag - 1));
			}
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction.replace(R.id.main,
					myFragmentList.get(flag - 1));
			// 提交保存杠杆替换或者添加的fragment
			mFragmentTransaction.commit();
			fragmentFlag = flag;
		}
	}

	// 从notesfragment里面回调回来的事件监听设置方法
	private void notesfragmentCallBack() {
		mNotesFragment.setCallBack(new MyNotesFragmentCallBack());
	}

	// 从picturefragment里面回调回来的事件监听设置方法
	private void partnerFragmentCallBack() {
		mPartnerFragment.setCallBack(new MyPartnerFragmentCallBack());
	}

	// 从followfragment里面回调回来的事件监听设置方法
	private void followFragmentCallBack() {
		mFollowFragment.setCallBack(new MyFollowFragmentCallBack());
	}

	// 从nicefragment里面回调回来的事件监听设置方法
	private void niceFragmentCallBack() {
		mNiceFragment.setCallBack(new MyNiceFragmentCallBack());
	}

	// 从indexfragment里面回调回来的事件监听设置方法
	private void indexFragmentCallBack() {
		mIndexFragment.setCallBack(new MyIndexFragmentCallBack());
	}

	// 从nearfragment里面回调回来的事件监听设置方法
	private void nearfragmentCallBack() {
		mNearFragment.setCallBack(new MyNearFragmentCallBack());
	}

	private class MyNotesFragmentCallBack implements NotesFragmentCallBack {
		@Override
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:
				MainActivity.this.toggle();
				break;
			default:
				break;
			}
		}
	}

	private class MyIndexFragmentCallBack implements IndexFragmentCallBack {
		@Override
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:
				MainActivity.this.toggle();
				break;
			case R.id.SendAshamed:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(MainActivity.this,
							UploadActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}

	// 实现nicefragment里面接口的子类
	private class MyPartnerFragmentCallBack implements PartnerFragmentCallBack {
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:// 用户点击hotfragment上面的导航按钮
				// 设置开启或关闭抽屉
				MainActivity.this.toggle();
				break;
			case R.id.SendAshamed:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(MainActivity.this,
							UploadActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}

	// 实现nicefragment里面接口的子类
	private class MyFollowFragmentCallBack implements FollowFragmentCallBack {
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:// 用户点击hotfragment上面的导航按钮
				// 设置开启或关闭抽屉
				MainActivity.this.toggle();
				break;
			case R.id.SendAshamed:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(MainActivity.this,
							UploadActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}

	// 实现nicefragment里面接口的子类
	private class MyNiceFragmentCallBack implements NiceFragmentCallBack {
		public void callback(int flag) {
			switch (flag) {
			case R.id.Menu:// 用户点击hotfragment上面的导航按钮
				// 设置开启或关闭抽屉
				MainActivity.this.toggle();
				break;
			case R.id.SendAshamed:
				if (Model.MYUSERINFO != null) {
					Intent intent = new Intent(MainActivity.this,
							UploadActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}

	private class MyNearFragmentCallBack implements NearFragmentCallBack {
		@Override
		public void callback(int flag) {
			switch (flag) {
			case R.id.Near_Back:
				MainActivity.this.toggle();
				break;
			default:
				break;
			}
		}
	}

	// 设置leftview控件的默认背景色
	private void createleftviewbg() {
		mLeftHot.setBackgroundResource(R.drawable.leftview_list_bg);
		mLeftNice.setBackgroundResource(R.drawable.leftview_list_bg);
		mLeftPicture.setBackgroundResource(R.drawable.leftview_list_bg);
		mLeftScrip.setBackgroundResource(R.drawable.leftview_list_bg);
		mLeftNear.setBackgroundResource(R.drawable.leftview_list_bg);
		mLeftCross.setBackgroundResource(R.drawable.leftview_list_bg);
		mLeftCheck.setBackgroundResource(R.drawable.leftview_list_bg);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (Model.MYUSERINFO != null) {
			myUserName.setText(Model.MYUSERINFO.getUname());
		} else {
			myUserName.setText("登录");
		}
		// createFargment(fragmentFlag);
	}

	@Override
	protected void onStart() {
		if (Model.MYUSERINFO != null) {
			myUserName.setText(Model.MYUSERINFO.getUname());
			KFIMInterfaces.setVCardField(MainActivity.this, "NICKNAME",
					Model.MYUSERINFO.getUname());
		} else {
			myUserName.setText("登录");
		}
		super.onStart();
	}

	private void login() {
		// 检查 用户名/密码 是否已经设置,如果已经设置，则登录
		if (!"".equals(mSettingsMgr.getUsername())
				&& !"".equals(mSettingsMgr.getPassword()))
			KFIMInterfaces.login(this, mSettingsMgr.getUsername(),
					mSettingsMgr.getPassword());
		// 设置个人资料"NICKNAME"
	}

}
