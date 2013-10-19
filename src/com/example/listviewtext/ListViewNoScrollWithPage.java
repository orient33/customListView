
package com.example.listviewtext;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * custom ListView: <br> it shows current page, and never scroll.</br>
 * 自定义的ListView， 从不滚动，且显示当前的页数.
 * */
public class ListViewNoScrollWithPage extends ListView {
	
	// y轴移动距离大于30时才翻页
	private final int MIN_Y_NEXT_PAGE = 30;
	// x轴移动距离大于15时才翻页
	private final int MIN_X_NEXT_PAGE = 15;
	
	private TextView mPager;
	private int mPagerHeight=-1;
	private int mPageShowSize=-1,mPageSize=-1,mItemSize=-1,mCurrent=1,mCurrentPos=-1;
	private int mDownX = -1, mDownY = -1, mUpX = -1, mUpY = -1;
	
	public ListViewNoScrollWithPage(Context context) {
		super(context);
		mPager=new TextView(context);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mPager.setLayoutParams(params);
		mPager.setBackgroundColor(0x3300ff00);
		mPager.setGravity(Gravity.CENTER);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (-1 == mPagerHeight) {
			measureChild(mPager, widthMeasureSpec, heightMeasureSpec);
			mPagerHeight = mPager.getMeasuredHeight();
		}
		mItemSize = getCount();
		mPageShowSize = getChildCount();
		if(mPageShowSize == 0) return;
		mPageSize = mItemSize/mPageShowSize+(mItemSize%mPageShowSize==0?0:1);
		log("onMeasure.itemsize="+mItemSize+",pageshow="+mPageShowSize+",pagesize="+mPageSize);
		mPager.setText(mCurrent+" / "+mPageSize);
		mPager.layout(getLeft(), getTop(), getRight(), getTop()+mPagerHeight);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		log("dispathDraw()");
		drawChild(canvas, mPager, getDrawingTime());
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
			onPageChange(position);
	}

	private void pageDown() {
		int position = getLastVisiblePosition();
		setSelection(position);
		onPageChange(position);
	}

	private void onPageChange(int position) {
		if(mCurrentPos==position){
			return ;
		}

		mCurrentPos = position;

		if (position == 0)
			mCurrent = 1;
		else if (position + 1 == mItemSize)
			mCurrent = mPageSize;
		else
			mCurrent = (1 + position) / (mPageShowSize - 1) + 1;

		log("onPageChange()  position=" + position + "; Page=" + mCurrent);
	}

	private void log(String str) {
		Log.i("dfdun", str);
	}
}
