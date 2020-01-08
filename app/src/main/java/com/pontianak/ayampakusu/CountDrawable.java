package com.pontianak.ayampakusu;

/**
 * Created by Pandhe PC on 07/03/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.util.Log;
import android.widget.Switch;

import androidx.core.content.ContextCompat;


public class CountDrawable extends Drawable {

    private Paint mBadgePaint, mBoxPaint;
    private Paint mTextPaint;
    private Rect mTxtRect = new Rect();
    private int dw=0;

    private String mCount = "";
    private boolean mWillDraw;

    public CountDrawable(Context context,int dw) {
        float mTextSize = context.getResources().getDimension(R.dimen.badge_textsize);
        this.dw=dw;

        mBadgePaint = new Paint();
        mBadgePaint.setColor(ContextCompat.getColor(context.getApplicationContext(), android.R.color.holo_red_dark));
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary));
        mBoxPaint.setAntiAlias(true);
        mBoxPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {

        if (!mWillDraw) {
            return;
        }
        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;
        Log.i("ez",String.valueOf(bounds.top)+","+String.valueOf(bounds.bottom)+","+String.valueOf(bounds.left)+","+String.valueOf(bounds.right));

        // Position the badge in the top-right quadrant of the icon.

        /*Using Math.max rather than Math.min */
        float circlemargin=(height/10);

        float radius = ( height / 2) - circlemargin;
        float textWidth = mCount.length()*10;
        float centerX = width-circlemargin-radius;
        float centerY = height/2;
        float centerYbox=(height/2);

        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f);
        float textYbox=centerYbox+ (textHeight / 2f);
        switch (dw){
            case 0:

                if(mCount.length() <= 2){
                    // Draw badge circle.

                    canvas.drawCircle(centerX, centerY, (int)(radius), mBadgePaint);

                }
                else{
                    canvas.drawCircle(centerX, centerY, (int)(radius), mBadgePaint);
                }
                // Draw badge count text inside the circle.
                mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);

                if(mCount.length() > 2)
                    canvas.drawText("99+", centerX, textY, mTextPaint);
                else
                    canvas.drawText(mCount, centerX, textY, mTextPaint);
                break;
            case 1:
                RectF rectF = new RectF(
                        bounds.left, // left
                        bounds.top+1, // top
                        width, // right
                        bounds.bottom // bottom
                );
               float cornersRadius = height/8;

                // Finally, draw the rounded corners rectangle object on the canvas



                if(mCount.length() <= 2){
                    // Draw badge circle.
                    //canvas.drawCircle(centerX, centerY, (int)(radius+3), mBadgePaint);
                    canvas.drawRoundRect(
                            rectF, // rect
                            cornersRadius, // rx
                            cornersRadius, // ry
                            mBoxPaint // Paint
                    );

                }
                else{
                    //canvas.drawCircle(centerX, centerY, (int)(radius+6.5), mBadgePaint);
                    canvas.drawRoundRect(
                            rectF, // rect
                            cornersRadius, // rx
                            cornersRadius, // ry
                            mBoxPaint // Paint
                    );
                }
                // Draw badge count text inside the circle.
                mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);

                centerX = (width - radius - 1) +5;



                //canvas.drawText(mCount, bounds.left, bounds.top, mTextPaint);
                canvas.drawText(mCount, (canvas.getWidth()/2)-radius, textYbox, mTextPaint);
                break;
        }

    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(String count) {
        mCount = count;

        // Only draw a badge if there are notifications.
        mWillDraw = !count.equalsIgnoreCase("0");
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
