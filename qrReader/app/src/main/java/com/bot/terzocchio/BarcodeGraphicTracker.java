package com.bot.terzocchio;

import android.content.Context;
import android.support.annotation.UiThread;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

public class BarcodeGraphicTracker extends Tracker<Barcode> {

    private BarcodeUpdateListener mBarcodeUpdateListener;


    public interface BarcodeUpdateListener {
        @UiThread
        void onBarcodeDetected(Barcode barcode);
    }

    BarcodeGraphicTracker(Context context) {
        if (context instanceof BarcodeUpdateListener) {
            this.mBarcodeUpdateListener = (BarcodeUpdateListener) context;
        } else {
            throw new RuntimeException("Hosting activity must implement BarcodeUpdateListener");
        }
    }



    @Override
    public void onNewItem(int id, Barcode item) {
        mBarcodeUpdateListener.onBarcodeDetected(item);
    }
}
