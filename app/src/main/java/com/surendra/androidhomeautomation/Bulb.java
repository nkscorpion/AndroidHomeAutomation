package com.surendra.androidhomeautomation;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Surendra on 1/25/2017.
 */

public class Bulb {
    public final char bulbId;
    public boolean mIsOn;
    public TextView mStatusText;
    public ImageView mImageView;
    public int imageResId;

    public Bulb(char bulbId, ImageView imageView, TextView statusText, int imageResId) {
        this.bulbId = bulbId;
        mImageView = imageView;
        mIsOn = false;
        mStatusText = statusText;
        mStatusText.setText("OFF");
        this.imageResId = imageResId;
        mImageView.setImageResource(R.drawable.bulb);
    }

    public void toggle(){
        mIsOn = !mIsOn;
        if (mIsOn) {
            turnOn();
        } else {
            turnOff();
        }
    }

    public void turnOn(){
        mStatusText.setText("ON");
        mImageView.setImageResource(imageResId);
        //TODO: send this data to arduino or other microcontroller
    }

    public void turnOff(){
        mStatusText.setText("OFF");
        mImageView.setImageResource(R.drawable.bulb);
        //TODO: send this data to arduino or other microcontroller
    }
}
