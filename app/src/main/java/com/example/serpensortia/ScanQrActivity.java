package com.example.serpensortia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.serpensortia.model.Reptile;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQrActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_scan_qr;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_notification;
    }

    @Override
    void init() {
        scanQr();
    }

    private void scanQr() {
        IntentIntegrator iterator = new IntentIntegrator(this);
        iterator.setCaptureActivity(CaptureAct.class);
        iterator.setOrientationLocked(false);
        iterator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        iterator.setPrompt("skenování kód");
        iterator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Reptile reptile = Reptile.findByQR(result.getContents());
                if (reptile != null) {
                    Log.d("qr_scan", "onActivityResult:  read value is : " + result.getContents() + "found in db :" + reptile.qrcode);
                    finish();
                    startActivity(new Intent(this, AnimalMainActivity.class));
                    Intent openReptile = new Intent(this, ShowReptileActivity.class);
                    openReptile.putExtra("reptile_qrcode", result.getContents());
                    startActivity(openReptile);
                }
            }
        }
    }
}