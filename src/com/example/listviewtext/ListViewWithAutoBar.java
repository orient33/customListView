package ingenic.e_ink.listview.scrollbar;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 自定义 ListView。
 * 和原ListView的不同是 这个从不滚动(滑动)，只上下翻页,
 * 另外滚动条每次翻页显示 1 秒后隐藏。自己用TextView模拟了一滚动条
 * */
public class ListViewWithAutoBar extends ListView {

	// y轴移动距离大于30时才翻页
	private final int MIN_Y_NEXT_PAGE = 30;
	// x轴移动距离大于15时才翻页
	private final int MIN_X_NEXT_PAGE = 15;
	private TextView mScrollbar;
	private int mDownX = -1, mDownY = -1, mUpX = -1, mUpY = -1;
	private int mPageShowSize=-1,mPageSize=-1,mItemSize=-1,mCurrent=1,mCurrentPos=-1;

	public ListViewWithAutoBar(Context context) {
		super(context);
		mScrollbar = new TextView(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(10,50);
		mScrollbar.setLayoutParams(params);
		mScrollbar.setBackgroundColor(0x99000000);
	}

	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			log("receiver msg --------------------");
			switch(msg.what){
			case 1:
				mScrollbar.setAlpha(0f);
				layoutChildren();
				break;
			}
		}
	};
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureChild(mScrollbar, widthMeasureSpec, heightMeasureSpec);
		int w=mScrollbar.getMeasuredWidth(),h=mScrollbar.getMeasuredHeight();
		mItemSize = getCount();
		mPageShowSize = getChildCount();
		if(mPageShowSize == 0) return;
		mPageSize = mItemSize/mPageShowSize+(mItemSize%mPageShowSize==0?0:1);
		int top = getTop(),bottom=getBottom();
		int bar_top = top
				+ (int) ((bottom - top - h) * ((mCurrent-1) / (double) (-1+mPageSize)));
		log("l,t,r,b===="+(getRight() - w)+",,"+(bar_top)+",,"+getRight()+",,"+(h+bar_top));
		mScrollbar.setAlpha(0.6f);
		mScrollbar.layout(getRight() - w, bar_top, getRight(), bar_top + h);
		mHandler.removeMessages(1);
		mHandler.sendEmptyMessageDelayed(1, 1000);
	}
	@Override
	protected void dispatchDraw(Canvas canvas) {
		try{
		super.dispatchDraw(canvas);
		}catch(Exception e){}
		log("dispathDraw()");
		drawChild(canvas, mScrollbar, getDrawingTime());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch(action){
		case MotionEvent.ACTION_DOWN:
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
	void log(String msg){
		Log.d("dfdun","now  ... "+ msg);
	}
}