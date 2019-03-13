package com.moment.momentgo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.moment.adapter.DetailListAdapter;
import com.moment.adapter.MyListAdapter;
import com.moment.info.AshamedInfo;
import com.moment.info.CommentsInfo;
import com.moment.info.UserInfo;
import com.moment.model.Model;
import com.moment.momentgo.R;
import com.moment.myview.CircleImageView;
import com.moment.myview.MyDetailsListView;
import com.moment.net.ThreadPoolUtils;
import com.moment.thread.HttpGetThread;
import com.moment.thread.HttpPostThread;
import com.moment.utils.LoadImg;
import com.moment.utils.MyJson;
import com.moment.utils.SdCardUtil;
import com.moment.utils.LoadImg.ImageDownloadCallBack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class UserInfoActivity extends Activity {
	private UserInfo info = null;
	String fileName = String.valueOf(System.currentTimeMillis()) + ".png";
	private String data = "";
	private ImageView mUserRevise, mUserMore;
	private LinearLayout mBrief, mQiushi;
	private LinearLayout mUserBrief, mUserQiushi;
	private RelativeLayout mBack;
	private Boolean myflag = true;
	private TextView Follow, SendMessage, UserName, UserAge, UserHobbies, UserPlace,
			UserExplain, UserTime , userinfo;
	private LoadImg loadImgHeadImg;
	private CircleImageView userIcon;
	private MyJson myJson = new MyJson();
	private List<AshamedInfo> list = new ArrayList<AshamedInfo>();
	private MyListAdapter mAdapter = null;
	private Button ListBottem = null;
	private int mStart = 0;
	private int mEnd = 5;
	private String url = null;
	private boolean flag = true;
	private boolean loadflag = false;
	private boolean listBottemFlag = true;
	private MyDetailsListView Detail_List;
	private LinearLayout Detail__progressBar;
	private RelativeLayout Detail_CommentsNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userinfo);
		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("value");
		info = (UserInfo) bund.getSerializable("UserInfo");
		initView();
		createUserInfo();
		mAdapter = new MyListAdapter(UserInfoActivity.this, list);
		ListBottem = new Button(UserInfoActivity.this);
		ListBottem.setText("点击加载更多");
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					url = Model.UASHAMED + "uid=" + info.getUserid()
							+ "&start=" + mStart + "&end=" + mEnd;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(UserInfoActivity.this, "正在加载中...", 1).show();
			}
		});
		Detail_List.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		Detail_List.setAdapter(mAdapter);
		String endParames = Model.UASHAMED + "uid=" + info.getUserid()
				+ "&start=" + mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, endParames));
		// 设置个人资料"NICKNAME"
		KFIMInterfaces.setVCardField(UserInfoActivity.this, "NICKNAME",
				info.getUname());
	}

	private void initView() {
		// TODO Auto-generated method stub
		mBrief = (LinearLayout) findViewById(R.id.Brief);
		mQiushi = (LinearLayout) findViewById(R.id.Qiushi);
		mUserBrief = (LinearLayout) findViewById(R.id.UserBrief);
		mUserQiushi = (LinearLayout) findViewById(R.id.UserQiushi);
		mBack = (RelativeLayout) findViewById(R.id.UserBack);
		mUserRevise = (ImageView) findViewById(R.id.UserRevise);
		mUserMore = (ImageView) findViewById(R.id.img_UserMore);
		userIcon = (CircleImageView) findViewById(R.id.userIcon);
		SendMessage = (TextView) findViewById(R.id.SendMessage);
		Follow =(TextView) findViewById(R.id.Follow);
		UserName = (TextView) findViewById(R.id.UserName);
		UserAge = (TextView) findViewById(R.id.UserAge);
		UserHobbies = (TextView) findViewById(R.id.UserHobbies);
		UserPlace = (TextView) findViewById(R.id.UserPlace);
		UserExplain = (TextView) findViewById(R.id.UserExplain);
		UserTime = (TextView) findViewById(R.id.UserTime);
		userinfo = (TextView) findViewById(R.id.userinfo);
		Detail_List = (MyDetailsListView) findViewById(R.id.Detail_List);
		Detail__progressBar = (LinearLayout) findViewById(R.id.Detail__progressBar);
		Detail_CommentsNum = (RelativeLayout) findViewById(R.id.usernoashamed);
		MyOnClick my = new MyOnClick();
		Detail_List.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(UserInfoActivity.this,
						AshamedDetailActivity.class);
				Bundle bund = new Bundle();
				bund.putSerializable("AshamedInfo", list.get(arg2));
				intent.putExtra("value", bund);
				startActivity(intent);
			}
		});
		
		mBrief.setOnClickListener(my);
		mQiushi.setOnClickListener(my);
		mUserRevise.setOnClickListener(my);
		mUserMore.setOnClickListener(my);
		mBack.setOnClickListener(my);
		userIcon.setOnClickListener(my);
		SendMessage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				KFIMInterfaces.startChatWithUser(UserInfoActivity.this,// 上下文Context
						Model.APPKEY + info.getUname(),// 对方用户名
						info.getUname());// 自定义会话窗口标题
			}
		});
		Follow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Follow.getText() == "关注") {
					addfollow();
					Follow.setText("取消关注");
				}else {
					delfollow();
					Follow.setText("关注");
				}
			}
		});
	}

	class MyOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int mId = v.getId();
			switch (mId) {
			case R.id.Brief:
				myflag = true;
				initCont(myflag);
				break;
			case R.id.Qiushi:
				myflag = false;
				initCont(myflag);
				break;
			case R.id.UserBack:
				finish();
				break;
			case R.id.UserRevise:
				Intent intent = new Intent(UserInfoActivity.this,ReviseActivity.class);			 
				  Bundle bund = new Bundle();
				  bund.putSerializable("UserInfo", info);
				  intent.putExtra("value", bund);
				  startActivity(intent);
				break;
			case R.id.userIcon:		
				new SelectPopuWindow(UserInfoActivity.this, userIcon);
				break;
			case R.id.img_UserMore:
				logout();
				break;
			}

		}

	}
	
	public class SelectPopuWindow extends PopupWindow {

		private Context context;

		public SelectPopuWindow(Context mContext, CircleImageView parent) {

			this.context = mContext;
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			
			/**
			 * 拍照
			 */
			bt1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					System.out.println("123");
					startActivityForResult(openCameraIntent, 1);
					dismiss();
					
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (SdCardUtil.checkSdCard() == true) {
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setType("image/*");// 相片类型
						System.out.println("123");
						startActivityForResult(intent, 2);
					} else {
						Toast.makeText(UserInfoActivity.this, "SD卡不存在",
								Toast.LENGTH_LONG).show();
					}

					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}
	private void sendHead(String data2) {
		String uid = Model.MYUSERINFO.getUserid();// 用户ID
//		String qimg = "";// 糗事图片
//		if (!data.equalsIgnoreCase("")) {
//			qimg = System.currentTimeMillis() + ".png";// 糗事图片
//		}
		String uIcon = "";
		if (!data2.equalsIgnoreCase("")) {
			uIcon =(System.currentTimeMillis()) + ".png";
			System.out.println("0925");
		}
//		System.out.println(data2);
		
		String Json = "{\"uid\":\"" + uid + "\"," + "\"uhead\":\"" + uIcon + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand4, Model.HTTP_UPDATE_USERICON, Json,
				data2));
		Model.MYUSERINFO.setUhead(uIcon);
	}

	Handler hand4 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 200) {
				System.out.println("666");
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(UserInfoActivity.this, "上传头像成功", 1).show();
					
				}
			}
		};
	};
//public void saveImageToFile(Bitmap bitmap) {
//		
//		FileOutputStream fos = null;
//		String fileName = SdCardUtil.getSdPath() + SdCardUtil.FILEDIR + "/"
//				+ SdCardUtil.FILEUSER + "/icon" + "/" + "Userimg"
//				+ String.valueOf(System.currentTimeMillis()) + ".png";
//
//		File f = new File(fileName);
//		if (f.exists()) {
//			f.delete();
//		}
//		try {
//			fos = new FileOutputStream(fileName);
//			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				fos.flush();
//				if (bitmap != null) {
//					bitmap.recycle();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally {
//				Toast.makeText(UserInfoActivity.this, fileName + "上传成功",
//						Toast.LENGTH_LONG).show();
////				initProgressDialog();
//				AjaxParams ap = new AjaxParams();
//				try {
//					ap.put("file", new File(fileName));
//					ap.put("id", info.getUserid());
//					FinalHttp fh = new FinalHttp();
//					fh.post(Model.HTTP_UPDATE_USERICON, ap,
//							new AjaxCallBack<Object>() {
//
//								@Override
//								public void onFailure(Throwable t, int errorNo,
//										String strMsg) {
//									// TODO Auto-generated method stub
//									super.onFailure(t, errorNo, strMsg);
//									Toast.makeText(UserInfoActivity.this, "修改图像失败",
//											Toast.LENGTH_LONG).show();
////									close();
//								}
//
//								@Override
//								public void onSuccess(Object t) {
//									// TODO Auto-generated method stub
//									super.onSuccess(t);
////									close();
//									Toast.makeText(UserInfoActivity.this,
//											String.valueOf(t),
//											Toast.LENGTH_LONG).show();
//								}
//
//							});
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
////					close();
//				}
//			}
//		}
//}
protected void onActivityResult(int requestCode, int respCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, respCode, data);
	System.out.print(requestCode);

	if (requestCode == 1 && respCode == RESULT_OK) {		
		Uri uri = null;
		if (data != null) {
			uri = data.getData();
			System.out.println("Data");
		}else{
			uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
		}
		
		cropImage(uri, 98, 98, 3);
		
		
	} else if (requestCode == 2 && respCode == RESULT_OK) {
		ContentResolver resolver = getContentResolver();
		Uri uri = data.getData();
		try {
			/*Bitmap bitmap = MediaStore.Images.Media
					.getBitmap(resolver, uri);*/
			cropImage(uri, 98, 98, 3);
			/*Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(bitmap, 88,
					88);
			userIcon.setImageBitmap(resizeBmp);
			saveImageToFile(bitmap);*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	} else if(requestCode==3 && respCode==RESULT_OK){
		Bitmap photo = null;
		Uri photoUri = data.getData();
		if (photoUri != null) {
			photo = BitmapFactory.decodeFile(photoUri.getPath());
		}
		if (photo == null) {
			Bundle extra = data.getExtras();
			if (extra != null) {
                photo = (Bitmap)extra.get("data");  
                ByteArrayOutputStream stream = new ByteArrayOutputStream();  
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }  
		}
		if(photo!=null){
			
			Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(photo, 98, 98);
			userIcon.setImageBitmap(resizeBmp);
//			saveImageToFile(photo);
			float mWeight = 480f;
			float mHight = 854f;
			float scaleWidth;   
			float scaleHeight;  
			scaleWidth = ((float)mWeight)/resizeBmp.getWidth();
			scaleHeight = ((float)mHight)/resizeBmp.getHeight();	
			Matrix matrix = new Matrix(); 
			matrix.postScale(scaleWidth, scaleHeight);  
			Bitmap mbit = null;
			mbit = Bitmap.createBitmap(resizeBmp, 0, 0,   
					resizeBmp.getWidth(), resizeBmp.getHeight(), matrix, true); 
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			mbit.compress(CompressFormat.PNG, 100, bStream);
			byte[] bytes = bStream.toByteArray();
			String data2 = Base64.encodeToString(bytes, Base64.DEFAULT);
			System.out.println("999");
			sendHead(data2);
		}
		
	}
}
//截取图片
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode){
		Intent intent = new Intent("com.android.camera.action.CROP");  
      intent.setDataAndType(uri, "image/*");  
      intent.putExtra("crop", "true");  
      intent.putExtra("aspectX", 1);  
      intent.putExtra("aspectY", 1);  
      intent.putExtra("outputX", outputX);   
      intent.putExtra("outputY", outputY); 
      intent.putExtra("outputFormat", "JPEG");
      intent.putExtra("noFaceDetection", true);
      intent.putExtra("return-data", true);  
	    startActivityForResult(intent, requestCode);
	}

	private void addfollow() {
		String auser = Model.MYUSERINFO.getUserid();
		String buser = info.getUserid();
		String Json = "{\"auser\":\"" + auser  + "\"," + "\"buser\":\"" + buser + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand1, Model.ADDFOLLOW, Json));
	}
	
	private void delfollow() {
		String auser = Model.MYUSERINFO.getUserid();
		String buser = info.getUserid();
		String Json = "{\"auser\":\"" + auser  + "\"," + "\"buser\":\"" + buser + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand2, Model.DELFOLLOW, Json));
	}
	
	private void iffollow() {
		String auser = Model.MYUSERINFO.getUserid();
		String buser = info.getUserid();
		String Json = "{\"auser\":\"" + auser  + "\"," + "\"buser\":\"" + buser + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand3, Model.IFFOLLOW, Json));
	}

	// 退出登录
	private void logout() {
		if (Model.MYUSERINFO != null) {
			if (info.getUname().equals(Model.MYUSERINFO.getUname())) {
				new AlertDialog.Builder(this)
						.setMessage("确认退出登录?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Model.MYUSERINFO = null;
										SharedPreferences sp = getSharedPreferences(
												"UserInfo", MODE_PRIVATE);
										Editor editor = sp.edit();
										editor.clear();
										editor.commit();
										// 退出登录
										KFIMInterfaces
												.Logout(UserInfoActivity.this);
										Intent intent = new Intent(
												UserInfoActivity.this,
												LoginActivity.class);
										startActivity(intent);
										finish();
									}
								}).setNegativeButton("取消", null).create()
						.show();
			}
		}
	}

	@SuppressLint("HandlerLeak")
	Handler hand = new Handler() {

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(UserInfoActivity.this, "请求失败，服务器故障", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(UserInfoActivity.this, "服务器无响应", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<AshamedInfo> newList = myJson.getAshamedList(result);
					if (newList != null) {
						if (newList.size() == 5) {
							Detail_List.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.VISIBLE);
							mStart += 5;
							mEnd += 5;
						} else if (newList.size() == 0) {
							if (list.size() == 0)
								Detail_CommentsNum.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
							Detail_List.setVisibility(View.GONE);
						} else {
							Detail_List.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
						}
						for (AshamedInfo info : newList) {
							list.add(info);
						}
						listBottemFlag = true;
					} else {
						Detail_List.setVisibility(View.GONE);
						Detail_CommentsNum.setVisibility(View.VISIBLE);
					}
				}
				Detail__progressBar.setVisibility(View.GONE);
				mAdapter.notifyDataSetChanged();
			}
			mAdapter.notifyDataSetChanged();
		};

	};
	
	Handler hand1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(msg.what == 200) {
				String result = (String) msg.obj;
				if (result !=null && result.equals("ok")) {
					Toast.makeText(UserInfoActivity.this, "关注成功", 1).show();
				}else {
					Toast.makeText(UserInfoActivity.this, result, 1).show();
				}
			}
		};
	};
	
	Handler hand2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(msg.what == 200) {
				String result = (String) msg.obj;
				if (result !=null && result.equals("ok")) {
					Toast.makeText(UserInfoActivity.this, "取关成功", 1).show();
				}else {
					Toast.makeText(UserInfoActivity.this, result, 1).show();
				}
			}
		};
	};
	
	Handler hand3 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(msg.what == 200) {
				String result = (String) msg.obj;
				if (result !=null && result.equals("yes")) {
					Follow.setText("取消关注");
				}else {
					Follow.setText("关注");
				}
			}
		};
	};

	private void initCont(Boolean myflag) {
		if (myflag) {
			mBrief.setBackgroundResource(R.drawable.cab_background_top_light);
			mQiushi.setBackgroundResource(R.drawable.ab_stacked_solid_light);
			mUserBrief.setVisibility(View.VISIBLE);
			mUserQiushi.setVisibility(View.GONE);
		} else {
			mBrief.setBackgroundResource(R.drawable.ab_stacked_solid_light);
			mQiushi.setBackgroundResource(R.drawable.cab_background_top_light);
			mUserBrief.setVisibility(View.GONE);
			mUserQiushi.setVisibility(View.VISIBLE);
		}
	}

	private void createUserInfo() {
		if (Model.MYUSERINFO != null) {
			if (info.getUname().equals(Model.MYUSERINFO.getUname())) {
				SendMessage.setVisibility(View.GONE);
				Follow.setVisibility(View.GONE);
				mUserRevise.setVisibility(View.VISIBLE);
				mUserMore.setVisibility(View.VISIBLE);
			}else {		
				iffollow();
				mUserRevise.setVisibility(View.GONE);
				mUserMore.setVisibility(View.GONE);
			}
		} else {
			Intent intent = new Intent(UserInfoActivity.this,
					LoginActivity.class);
			startActivity(intent);
		}
		UserName.setText(info.getUname());
		if (!info.getUage().equals("null")) {
			UserAge.setText(info.getUage());
			if (info.getUsex().equals("0")) {
				UserAge.setBackgroundResource(R.drawable.nearby_gender_female);
			} else if (info.getUsex().equals("1")) {
				UserAge.setBackgroundResource(R.drawable.nearby_gender_male);
			}
		}
		if (!info.getUhobbles().equals("null")) {
			UserHobbies.setText("兴趣爱好　" + info.getUhobbles());
		}
		if (!info.getUplace().equals("null")) {
			UserPlace.setText("常出没地　" + info.getUplace());
		}
		if (!info.getUexplain().equals("null")) {
			UserExplain.setText("个人说明　" + info.getUexplain());
			userinfo.setText(info.getUexplain());
		}	
		UserTime.setText("注册时间　" + info.getUtime());
		if (!info.getUhead().equals("null")) {
			loadImgHeadImg = new LoadImg(UserInfoActivity.this);
			Bitmap bit = loadImgHeadImg.loadImage(userIcon,
					Model.USERHEADURL + info.getUhead(),
					new ImageDownloadCallBack() {
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							userIcon.setImageBitmap(bitmap);
						}
					});
			if (bit != null) {
				userIcon.setImageBitmap(bit);
			}
		}
	}

}
