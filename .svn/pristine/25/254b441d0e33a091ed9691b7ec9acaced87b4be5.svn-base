package com.anjoyo.fragment;

import com.anjoyo.anjoyoqiushibaike.R;
import com.anjoyo.myview.MyListView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotesFragment extends Fragment implements OnClickListener {

	private NotesFragmentCallBack mNotesFragmentCallBack;
	private View view;
	private Context ctx;
	private ImageView Menu;
	private TextView HomeNoValue;
	private LinearLayout load_progressBar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_xiaozhitiao, null);
		ctx = view.getContext();
		initView();
		return view;
	}

	private void initView() {
		Menu = (ImageView) view.findViewById(R.id.Menu);
		HomeNoValue = (TextView) view.findViewById(R.id.HomeNoValue);
		load_progressBar = (LinearLayout) view
				.findViewById(R.id.load_progressBar);
	}

	@Override
	public void onClick(View arg0) {

	}

	// private void createListModel() {
	// ListBottem.setVisibility(View.GONE);
	// mLinearLayout.setVisibility(View.GONE);
	// load_progressBar.setVisibility(View.VISIBLE);
	// loadflag = false;
	// mStart = 0;
	// mEnd = 5;
	// url = hotUrl + "start=" + mStart + "&end=" + mEnd;
	// ThreadPoolUtils.execute(new HttpGetThread(hand, url));
	// }

	// private class MainListOnItemClickListener implements OnItemClickListener
	// {
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// Intent intent = new Intent(ctx, AshamedDetailActivity.class);
	// Bundle bund = new Bundle();
	// bund.putSerializable("AshamedInfo", list.get(arg2 - 1));
	// intent.putExtra("value", bund);
	// startActivity(intent);
	// }
	// }

	public void setCallBack(NotesFragmentCallBack mNotesFragmentCallBack) {
		this.mNotesFragmentCallBack = mNotesFragmentCallBack;
	}

	public interface NotesFragmentCallBack {
		public void callback(int flag);
	}

	// Handler hand = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// super.handleMessage(msg);
	// if (msg.what == 404) {
	// Toast.makeText(ctx, "找不到地址", 1).show();
	// listBottemFlag = true;
	// } else if (msg.what == 100) {
	// Toast.makeText(ctx, "传输失败", 1).show();
	// listBottemFlag = true;
	// } else if (msg.what == 200) {
	// String result = (String) msg.obj;
	// if (result != null) {
	// List<AshamedInfo> newList = myJson.getAshamedList(result);
	// if (newList != null) {
	// if (newList.size() == 5) {
	// ListBottem.setVisibility(View.VISIBLE);
	// mStart += 5;
	// mEnd += 5;
	// } else if (newList.size() == 0) {
	// if (list.size() == 0)
	// HomeNoValue.setVisibility(View.VISIBLE);
	// } else {
	// ListBottem.setVisibility(View.GONE);
	// }
	// if (!loadflag) {
	// list.removeAll(list);
	// }
	// for (AshamedInfo info : newList) {
	// list.add(info);
	// }
	// listBottemFlag = true;
	// } else {
	// if (list.size() == 0)
	// HomeNoValue.setVisibility(View.VISIBLE);
	// }
	// }
	// mLinearLayout.setVisibility(View.VISIBLE);
	// load_progressBar.setVisibility(View.GONE);
	// myListView.onRefreshComplete();
	// mAdapter.notifyDataSetChanged();
	// loadflag = true;
	// }
	// };
	// };
}
