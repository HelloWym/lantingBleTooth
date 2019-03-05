package com.lantingBletooth.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

import com.lantingBletooth.MainActivity;

/**
 * Created by wym on 2018/9/20.
 */

public class FanImage extends AppCompatImageView {
    public static final int STATE_PLAYING =1;//正在播放
    public static final int STATE_PAUSE =2;//暂停
    public static final int STATE_STOP =3;//停止
    public int state;

    private float angle;//记录RotateAnimation中受插值器数值影响的角度
    private float angle2;//主要用来记录暂停时停留的角度，即View初始旋转角度
    private int viewWidth;
    private int viewHeight;
    private MusicAnim musicAnim;

    public FanImage(Context context) {
        super(context);
        init();
    }

    public FanImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FanImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        state = STATE_STOP;
        angle = 0;
        angle2 = 0;
        viewWidth = 0;
        viewHeight = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(angle2,viewWidth/2,viewHeight/2);
        super.onDraw(canvas);
    }

    public class MusicAnim extends RotateAnimation{
        public MusicAnim(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)  {
            super(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue) ;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            angle = interpolatedTime * 360;
        }
    }
    /**
     * 开启动画效果
     * @param flag  8档 500  7档1000 6档1500 5档2000 4档2500 3档3000 2档3500 1档4000
     */

    public void startAnimation(int flag){
        int s = 0;
        switch (flag){
            case 8:
                s = 500;
                break;
            case 7:
                s = 1000;
                break;
            case 6:
                s = 1500;
                break;
            case 5:
                s = 2000;
                break;
            case 4:
                s = 2500;
                break;
            case 3:
                s = 3000;
                break;
            case 2:
                s = 3500;
                break;
            case 1:
                s = 4000;
                break;
        }
        angle2 = (angle2 + angle)%360;//可以取余也可以不取，看实际的需求
        invalidate();
        MusicAnim musicAnim  = new MusicAnim(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        musicAnim.setDuration(s);
        musicAnim.setInterpolator(new LinearInterpolator());//动画时间线性渐变
        musicAnim.setRepeatCount(ObjectAnimator.INFINITE);
        startAnimation(musicAnim);
        state = STATE_PLAYING;
    }
    public void playMusic(){
        if(state == STATE_PLAYING){
            angle2 = (angle2 + angle)%360;//可以取余也可以不取，看实际的需求
            musicAnim.cancel();
            state = STATE_PAUSE;
            invalidate();
        }else {
//            musicAnim = new MusicAnim(0,360,viewWidth/2,viewHeight/2);
            MusicAnim musicAnim  = new MusicAnim(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            musicAnim.setDuration(3000);
            musicAnim.setInterpolator(new LinearInterpolator());//动画时间线性渐变
            musicAnim.setRepeatCount(ObjectAnimator.INFINITE);
            startAnimation(musicAnim);
            state = STATE_PLAYING;
        }
    }

    public void stopAnimation(){
        angle2 = 0;
        clearAnimation();
        state = STATE_STOP;
        invalidate();
    }
}
