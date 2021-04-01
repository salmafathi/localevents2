package com.example.localevents;

import android.app.Application;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

public class MyApplication extends Application {
	
    private Typeface normalFont;
    private Typeface boldFont;
	
	
    // -- Fonts -- //
//    public void setTypeface(TextView textView) {
//        if(textView != null) {
//            if(textView.getTypeface() != null && textView.getTypeface().isBold()) {
//                textView.setTypeface(getBoldFont());
//            } else {
//                textView.setTypeface(getNormalFont());
//            }
//        }
//    }
    public void setTypeface(Button button) {
        if(button != null) {
            if(button.getTypeface() != null && button.getTypeface().isBold()) {
            	button.setTypeface(getBoldFont());
            } else {
            	button.setTypeface(getNormalFont());
            }
        }
    }
    private Typeface getNormalFont() {
        if(normalFont == null) {
            normalFont = Typeface.createFromAsset(getAssets(),"fonts/GOTHICB.TTF");
        }
        return this.normalFont;
    }

    private Typeface getBoldFont() {
        if(boldFont == null) {
            boldFont = Typeface.createFromAsset(getAssets(),"fonts/GOTHICB.TTF");
        }
        return this.boldFont;
    }
}