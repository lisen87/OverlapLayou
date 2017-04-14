package view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/4/13.
 * lisen
 * 重叠覆盖布局，点击里面的子view可以让此子view处于最上端不被覆盖
 */

public class OverlapLayout extends RelativeLayout {

    private static final String TAG = "OverlapLayout";
    private final int left = 60;

    private View addedView = null;
    private int index = -1;

    public OverlapLayout(Context context) {
        super(context);
    }
    public OverlapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //还原以前的状态
        resetLayout();
        return super.onTouchEvent(event);
    }
    /**
     * 还原以前的状态
     */
    public void resetLayout(){
        if (addedView != null && index != -1) {
            removeView(addedView);
            addView(addedView,index);
            resetChild(addedView);
            addedView = null;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final int count = getChildCount();
        if (count != 0){
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);

                final LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                layoutParams.leftMargin = i * left;
                child.setLayoutParams(layoutParams);
                child.setTag(i);
                child.setOnClickListener(new ClickListener(i));
            }
        }
    }

    class ClickListener implements OnClickListener{

        private int i;
        public ClickListener(int i){
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            //还原以前的状态
            if (addedView != null && index != -1) {
                removeView(addedView);
                addView(addedView,index);
                resetChild(addedView);
            }
            //记录上次child的位置
            index = i;

            //将点击的view设置到最前
            addedView = v;
            removeView(v);
            addView(addedView,getChildCount());
            animChild(addedView);

            setClick(addedView);
        }
    }

    private void setClick(final View child ) {
            if (iClick != null) {
                iClick.onIClick(child);
            }
    }

    private void removeIClick(){
    }

    public interface IClick{
        void onIClick(View view);
    }

    private IClick iClick;

    public void setiClick(IClick iClick) {
        this.iClick = iClick;
    }

    //放大view
    private void animChild(View child) {
        ViewCompat.animate(child).scaleX(1.2f).scaleY(1.2f).translationY(-20).setDuration(300).start();
    }
    //将放大的view还原
    private void resetChild(View child) {
        ViewCompat.animate(child).scaleX(1f).scaleY(1f).translationY(0).setDuration(300).start();
    }
}
