package com.bot.terzocchio;

import android.content.Context;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private Context mContext;

    public BarcodeTrackerFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new BarcodeGraphicTracker(mContext);
    }



}

