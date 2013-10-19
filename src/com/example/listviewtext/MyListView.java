package com.example.listviewtext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
/**
 * 自定义 ListView。
 * 和原ListView的不同是 这个从不滚动(滑动)，只上下翻页,另外滚动条总显示。
 * */
public class MyListView extends ListView {

	// y轴移动距离大于30时才翻页
	private final int MIN_Y_NEXT_PAGE = 30;
	// x轴移动距离大于15时才翻页
	private final int MIN_X_NEXT_PAGE = 15;
	
	private int mDownX = -1, mDownY = -1, mUpX = -1, mUpY = -1;
	PageChanger mPageChanger;
	private int mChild = 0; 
	void setPageChanger(PageChanger pc){
		mPageChanger = pc;
	}

	interface PageChanger{
		void onPageChange(int position );
		void onInit(int displaySize);
	}
	public MyListView(Context context) {
		super(context);
		setScrollbarFadingEnabled(false);
	}
	public MyListView(Context con,AttributeSet at){
		super(con,at);
		setScrollbarFadingEnabled(false);
	}
	
	@Override
	public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
		// just make scroll bar  show always 
		super.setScrollbarFadingEnabled(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mPageChanger != null && mChild <= 0) {
			mChild = getChildCount();
			mPageChanger.onInit(mChild);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch(action){
		case MotionEvent.ACTION_DOWN:
			super.setScrollbarFadingEnabled(false);
			mUpX = mUpY = -1;
			mDownX = (int) ev.getX();
			mDownY = (int) ev.getY();
			return true; // must true
		case MotionEvent.ACTION_MOVE:
			return false;
		case MotionEvent.ACTION_UP:
			mUpX = (int) ev.getX();
			mUpY = (int) ev.getY();
			final int delY = mUpY - mDownY;
			final int delX = mUpX - mDownX;
			
			if (Math.abs(delY) > MIN_Y_NEXT_PAGE) {
				if (delY < 0)
					pageDown();
				else
					pageUp();
				return true;
			} else if (Math.abs(delX) > MIN_X_NEXT_PAGE) {
				if (delX < 0)
					pageDown();
				else
					pageUp();
				return true;
			}
			break;
		default:
		}
		return super.onTouchEvent(ev);
	}

	private void pageUp() {
		int position = this.getFirstVisiblePosition() - getChildCount() + 1;
		setSelection(position);
		if (mPageChanger != null)
			mPageChanger.onPageChange(position);
	}

	private void pageDown() {
		int position = getLastVisiblePosition();
		setSelection(position);
		if (mPageChanger != null)
			mPageChanger.onPageChange(position);
	}
}