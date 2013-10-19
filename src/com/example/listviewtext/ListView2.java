package com.example.listviewtext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.listviewtext.MyListView.PageChanger;

/**
 * this may be a fail.
 * linearlayout包含ListView，已经不符合原意了。
 *  
 * */
public class ListView2 extends LinearLayout implements PageChanger{

	private LinearLayout mLl;
	private MyListView mListView;
	private TextView mPager;
	private int mPageShowSize=1,mPageSize=1,mItemSize=1;
	
	public ListView2(Context context) {
		super(context);
		mLl = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.linear, this);
		mListView=(MyListView)mLl.findViewById(R.id.listview);
		mPager = (TextView)mLl.findViewById(R.id.page);
		mListView.setPageChanger(this);
		Log.d("dfdun", mListView+ "\n"+mPager);
	}

	public ListView getListView(){
		return mListView;
	}
	
	@Override
	public void onPageChange(int position ) {
		mItemSize = mListView.getCount();
		mPageSize=mItemSize/mPageShowSize+(mItemSize%mPageShowSize==0?0:1);
		int currentPage = 0;
//		Log.i("dfdun"," pageSize="+mPageSize+";itemsize="+mItemSize+" ; first="+firstPosition+
//				" ; lastPosition="+lastPosition +"...position="+position);
		if (position == 0)
			currentPage = 1;
		else if (position+1 == mItemSize)
			currentPage = mPageSize;
		else {
			currentPage=(1+position)/(mPageShowSize-1) +1;
		}
		mPager.setText(currentPage +" / "+ mPageSize);
	}

	@Override
	public void onInit(int displaySize) {
		mPageShowSize = displaySize;
		if (displaySize > 0)
			onPageChange(0);
	}
}
