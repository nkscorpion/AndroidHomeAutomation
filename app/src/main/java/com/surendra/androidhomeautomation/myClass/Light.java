package com.surendra.androidhomeautomation.myClass;

import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import com.surendra.androidhomeautomation.R;

/**
 * Created by Surendra on 1/27/2017.
 */

public class Light {
    private char mId;
    private volatile boolean mIsOn;
    private int bulbOnResId;
    private int bulbOffResId;
    private TextView mStatusText;
    private ImageView mImage;
    private TextView mCountTimerText;
    private CountDownTimer mTimer;

    public Light(char id, int bulbOnResId, TextView statusText, ImageView imageView,
                 TextView counterText){
        mId = id;
        this.bulbOnResId = bulbOnResId;
        mStatusText = statusText;
        mCountTimerText = counterText;
        mImage = imageView;

        mIsOn = false;
        bulbOffResId = R.drawable.bulb;
        mTimer = null;
    }

    public void turnOn(){
        mIsOn = true;
        mImage.setImageResource(bulbOnResId);
        mStatusText.setText("ON");
    }

    public void turnOff(){
        mIsOn = false;
        mImage.setImageResource(bulbOffResId);
        mStatusText.setText("OFF");
    }

    public void setTimer(int timeInMillis, final boolean toTurnOn){
        if(mTimer != null)
            mTimer.cancel();

        if(mIsOn == toTurnOn)
            return;

        mTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCountTimerText.setText(formatTimerValue(toTurnOn, millisUntilFinished));
                if(mIsOn == toTurnOn){
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                mCountTimerText.setText("");
                if(toTurnOn){
                    turnOn();
                }else{
                    turnOff();
                }
            }
        }.start();
    }

    public void toggle(){
        mIsOn = !mIsOn;
        if (mIsOn) {
            turnOn();
        } else {
            turnOff();
        }
    }

    private String formatTimerValue(boolean toOn, long millis) {
        StringBuilder builder = new StringBuilder();
        builder.append(millis / 1000);
        builder.append("s to turn ");
        if (toOn) {
            builder.append(" On.");
        } else {
            builder.append(" Off.");
        }
        return builder.toString();
    }

    public char getId() {
        return mId;
    }

    public boolean isOn(){
        return mIsOn;
    }
}
