package com.moment.adapter;

import java.util.List;

import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.moment.info.AshamedInfo;
import com.moment.info.PostInfo;
import com.moment.model.Model;
import com.moment.momentgo.R;
import com.moment.utils.LoadImg;
import com.moment.utils.LoadImg.ImageDownloadCallBack;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PostListAdapter extends BaseAdapter{

	private List<PostInfo> list;
	private Context ctx;
	private LoadImg loadImgHeadImg;
	
	public PostListAdapter(Context ctx, List<PostInfo> list) {
		this.ctx = ctx;
		this.list = list;
		// 加载图片对象
		loadImgHeadImg = new LoadImg(ctx);
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
		if(arg1 == null){
			hold = new Holder();
			arg1 = View.inflate(ctx, R.layout.postlist, null);
			hold.UserHead = (ImageView) arg1.findViewById(R.id.Item_UserHead);
			hold.SendTime = (TextView) arg1.findViewById(R.id.sendTime);
			hold.UserName = (TextView) arg1.findViewById(R.id.userName);
			hold.Age = (TextView) arg1.findViewById(R.id.Age);
			hold.Item_travalTime = (TextView) arg1.findViewById(R.id.Item_travalTime);
			hold.TeavalTime= (TextView) arg1.findViewById(R.id.travalTime);
			hold.Item_travalAddress = (TextView) arg1.findViewById(R.id.Item_travalAddress);
			hold.travalAddress = (TextView) arg1.findViewById(R.id.travalAddress);
			hold.Item_startAddress = (TextView) arg1.findViewById(R.id.Item_startAddress);
			hold.startAddress = (TextView) arg1.findViewById(R.id.startAddress);
			hold.Item_saying = (TextView) arg1.findViewById(R.id.Item_saying);
			hold.saying = (TextView) arg1.findViewById(R.id.saying);
			hold.chat = (Button) arg1.findViewById(R.id.chat);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}
		
		hold.UserName.setText(list.get(arg0).getUname());
		hold.SendTime.setText(list.get(arg0).getPtime());
		hold.Age.setText(list.get(arg0).getUage());
		hold.TeavalTime.setText(list.get(arg0).getPbeginTime()+"-"+list.get(arg0).getPendTime());
		hold.travalAddress.setText(list.get(arg0).getPtravalAddress());
		hold.startAddress.setText(list.get(arg0).getPstartAddress());
		hold.saying.setText(list.get(arg0).getPsaying());
		
		hold.UserHead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Model.MYUSERINFO != null) {
					KFIMInterfaces.startChatWithUser(ctx, 
							Model.APPKEY + list.get(arg0).getUname(), 
							list.get(arg0).getUname());
				} else {
					Toast.makeText(ctx, "请先登录才能发送小纸条哦", 1).show();
				}
			}
		});
		
		hold.UserHead.setImageResource(R.drawable.default_users_avatar);
		if (list.get(arg0).getUhead().equalsIgnoreCase("")) {
			hold.UserHead.setTag(Model.USERHEADURL + list.get(arg0).getUhead());
			Bitmap bitHead = loadImgHeadImg.loadImage(hold.UserHead, 
					Model.USERHEADURL + list.get(arg0).getUhead(), 
					new ImageDownloadCallBack() {

						@Override
						public void onImageDownload(ImageView imageView, Bitmap bitmap) {
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
										+list.get(arg0).getUhead())) {
									hold.UserHead.setImageBitmap(bitmap);
								}
							}
						}
			});
			if(bitHead != null) {
				hold.UserHead.setImageBitmap(bitHead);
			}
		}
		hold.chat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Model.MYUSERINFO != null) {
					KFIMInterfaces.startChatWithUser(ctx,// 上下文Context
							Model.APPKEY + list.get(arg0).getUname(),// 对方用户名
							list.get(arg0).getUname());// 自定义会话窗口标题
				} else {
					Toast.makeText(ctx, "请先登录才能发送小纸条哦", 1).show();
				}
			}
		});
		return arg1;
	}
	
	static class Holder
	{
		ImageView UserHead;
		TextView SendTime;
		TextView UserName;
		TextView Age;
		TextView Item_travalTime;
		TextView TeavalTime;
		TextView Item_travalAddress;
		TextView travalAddress;
		TextView Item_startAddress;
		TextView startAddress;
		TextView Item_saying;
		TextView saying;
		Button chat;	
	}

}
