package com.moment.adapter;

import java.util.List;

import com.moment.info.AshamedInfo;
import com.moment.info.UserInfo;
import com.moment.model.Model;
import com.moment.momentgo.R;
import com.moment.momentgo.ReviseActivity;
import com.moment.momentgo.UserInfoActivity;
import com.moment.net.ThreadPoolUtils;
import com.moment.thread.HttpPostThread;
import com.moment.utils.LoadImg;
import com.moment.utils.MyJson;
import com.moment.utils.LoadImg.ImageDownloadCallBack;
import com.appkefu.lib.interfaces.KFIMInterfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyListAdapter extends BaseAdapter {

	private List<AshamedInfo> list;
	private UserInfo info = new UserInfo();
	private MyJson myJson = new MyJson();
	private Context ctx;
	private LoadImg loadImgHeadImg;
	private LoadImg loadImgMainImg;
	private boolean upFlag = false;
	private boolean downFlag = false;

	public MyListAdapter(Context ctx, List<AshamedInfo> list) {
		this.list = list;
		this.ctx = ctx;
		// 加载图片对象
		loadImgHeadImg = new LoadImg(ctx);
		loadImgMainImg = new LoadImg(ctx);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final Holder hold;
		if (arg1 == null) {
			hold = new Holder();
			arg1 = View.inflate(ctx, R.layout.mylistview_item, null);
			hold.UserHead = (ImageView) arg1.findViewById(R.id.Item_UserHead);
			hold.UserName = (TextView) arg1.findViewById(R.id.Item_UserName);
			hold.MainText = (TextView) arg1.findViewById(R.id.Item_MainText);
			hold.MainImg = (ImageView) arg1.findViewById(R.id.Item_MainImg);
			hold.Up = (LinearLayout) arg1.findViewById(R.id.Item_Up);
			hold.Up_Img = (ImageView) arg1.findViewById(R.id.Item_Up_img);
			hold.Up_text = (TextView) arg1.findViewById(R.id.Item_Up_text);
			hold.Down = (LinearLayout) arg1.findViewById(R.id.Item_Down);
			hold.Down_Img = (ImageView) arg1.findViewById(R.id.Item_Down_img);
			hold.Down_text = (TextView) arg1.findViewById(R.id.Item_Down_text);
			hold.address = (TextView) arg1.findViewById(R.id.address);
			hold.time = (TextView) arg1.findViewById(R.id.time);
			
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}

		hold.UserName.setText(list.get(arg0).getUname());
		hold.MainText.setText(list.get(arg0).getQvalue());
		hold.Up_text.setText(list.get(arg0).getQlike());
		hold.Down_text.setText("-" + list.get(arg0).getQunlike());
		if(list.get(arg0).getAddress()==null)
		{
			hold.address.setText("");
		}else{
			hold.address.setText(list.get(arg0).getAddress());
		}
		if(list.get(arg0).getTime()==null)
		{
			hold.time.setText("");
		}else{
			hold.time.setText(list.get(arg0).getTime());
		}

		// 设置监听
		hold.Up.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(hold.Up.getTag().equals("0")){
					String num;
					hold.Up.setBackgroundResource(R.drawable.button_vote_active);
				    hold.Up_Img.setImageResource(R.drawable.icon_for_active);
				    hold.Up_text.setTextColor(Color.RED);
				    hold.Up.setTag("1");
					num = String.valueOf(Integer.parseInt(list.get(arg0)
							.getQlike()) + 1);
					hold.Up_text.setText(num);
					list.get(arg0).setQlike(num);
					upFlag = true;
					updateLike(arg0);
				}
			}
		});
		hold.Down.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(hold.Down.getTag().equals("0")){
					String num;
					hold.Down.setBackgroundResource(R.drawable.button_vote_active);
					hold.Down_Img.setImageResource(R.drawable.icon_for_active);
					hold.Down_text.setTextColor(Color.RED);
					hold.Down.setTag("1");
					num = String.valueOf(Integer.parseInt(list.get(arg0)
							.getQunlike()) + 1);
					hold.Down_text.setText("-" + num);
					list.get(arg0).setQunlike(num);
					downFlag = true;
					updateLike(arg0);
				}
			}
		});
		hold.UserHead.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Toast.makeText(ctx, "点击发送小纸条", 1).show();
				if (Model.MYUSERINFO != null) {
					getInfo(arg0);
				} else {
					Toast.makeText(ctx, "请先登录才能查看他人用户信息哦", 1).show();
				}
			}
		});
		

		hold.Up.setTag("0");
		hold.Down.setTag("0");
		// Log.e("liuxiaowei", hold.Up.getTag().toString());
		hold.Up.setBackgroundResource(R.drawable.button_vote_enable);
		hold.Up_Img.setImageResource(R.drawable.icon_against_enable);
		hold.Up_text.setTextColor(Color.parseColor("#815F3D"));
		if (hold.Up.getTag().equals("1")) {
			hold.Up.setBackgroundResource(R.drawable.button_vote_active);
			hold.Up_Img.setImageResource(R.drawable.icon_for_active);
			hold.Up_text.setTextColor(Color.RED);
		}
		hold.Down.setBackgroundResource(R.drawable.button_vote_enable);
		hold.Down_Img.setImageResource(R.drawable.icon_against_enable);
		hold.Down_text.setTextColor(Color.parseColor("#815F3D"));
		if (hold.Down.getTag().equals("1")) {
			hold.Down.setBackgroundResource(R.drawable.button_vote_active);
			hold.Down_Img.setImageResource(R.drawable.icon_for_active);
			hold.Down_text.setTextColor(Color.RED);
		}
		hold.UserHead.setImageResource(R.drawable.default_users_avatar);
		if (list.get(arg0).getUhead().equalsIgnoreCase("")) {
			hold.UserHead.setImageResource(R.drawable.default_users_avatar);
		} else {
			hold.UserHead.setTag(Model.USERHEADURL + list.get(arg0).getUhead());
			Bitmap bitHead = loadImgHeadImg.loadImage(hold.UserHead,
					Model.USERHEADURL + list.get(arg0).getUhead(),
					new ImageDownloadCallBack() {
						@Override
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							if (arg0 >= list.size()) {
								if (hold.UserHead
										.getTag()
										.equals(Model.USERHEADURL
												+ list.get(arg0 - 1).getUhead())) {
									hold.UserHead.setImageBitmap(bitmap);
								}
							} else {
								if (hold.UserHead.getTag().equals(
										Model.USERHEADURL
												+ list.get(arg0).getUhead())) {
									hold.UserHead.setImageBitmap(bitmap);
								}
							}

						}
					});
			if (bitHead != null) {
				hold.UserHead.setImageBitmap(bitHead);
			}
		}
		hold.MainImg.setImageResource(R.drawable.default_content_pic);
		if (list.get(arg0).getQimg().equalsIgnoreCase("")) {
			hold.MainImg.setVisibility(View.GONE);
		} else {
			hold.MainImg.setVisibility(View.VISIBLE);
			hold.MainImg.setTag(Model.USERHEADURL + list.get(arg0).getQimg());
			final Bitmap bitMain = loadImgMainImg.loadImage(hold.MainImg,
					Model.QIMGURL + list.get(arg0).getQimg(),
					new ImageDownloadCallBack() {
						@Override
						public void onImageDownload(ImageView imageView,
								Bitmap bitmap) {
							if (arg0 >= list.size()) {
								if (hold.MainImg.getTag().equals(
										Model.QIMGURL
												+ list.get(arg0 - 1).getQimg())) {
									hold.MainImg.setImageBitmap(bitmap);
								}
							} else {
								if (hold.MainImg.getTag().equals(
										Model.QIMGURL
												+ list.get(arg0).getQimg())) {
									hold.MainImg.setImageBitmap(bitmap);
								}
							}

						}
					});
			if (bitMain != null) {
				hold.MainImg.setImageBitmap(bitMain);
				hold.MainImg.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Toast.makeText(ctx, "查看大图被点击", 1).show();
						LayoutInflater inflater = LayoutInflater.from(ctx);
						View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null);
						final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
						ImageView img = (ImageView)imgEntryView.findViewById(R.id.large_image);
						img.setImageBitmap(bitMain);
						dialog.setView(imgEntryView);
						dialog.show();
						imgEntryView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.cancel();
							}
							
						});
					}
				});
			}
		}

		return arg1;
	}
	
	private void updateLike(int arg0){
		String uid = list.get(arg0).getUid();
		String qlike = list.get(arg0).getQlike();
		String qunlike = list.get(arg0).getQunlike();
		String Json = "{\"uid\":\"" + uid + "\","+"\"qlike\":\"" + qlike + "\"," + "\"qunlike\":\"" + qunlike +"\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand1, Model.UPDATELIKE, Json));
	}
	
	private void getInfo(int arg0) {
		String userid = list.get(arg0).getUid();
		String Json = "{\"userid\":\"" + userid+ "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.GETUSER, Json));
	}
	
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(ctx, "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if(result!= null)
				{
					List<UserInfo> newList = myJson.getNearUserList(result);
					if(newList!= null)
					{
						info = newList.get(0);
						Toast.makeText(ctx, "成功", 1).show();
					}
					Intent intent = new Intent(ctx, UserInfoActivity.class);
					Bundle bund = new Bundle();
					bund.putSerializable("UserInfo", info);
					intent.putExtra("value", bund);
					ctx.startActivity(intent);
				}
			}
		}
	};
	
	Handler hand1 = new Handler() {
		public void handleMessage (android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(ctx, "", 1)
							.show();
				}
			}
		}
	};
	

	static class Holder {
		ImageView UserHead;
		TextView UserName;
		TextView MainText;
		ImageView MainImg;
		LinearLayout Up;
		ImageView Up_Img;
		TextView Up_text;
		LinearLayout Down;
		ImageView Down_Img;
		TextView Down_text;
		TextView address;
		TextView time;
	}
	
}
