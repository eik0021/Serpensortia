package com.example.serpensortia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

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
        if(result != null){
            if(result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Výsledek čtění");

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}