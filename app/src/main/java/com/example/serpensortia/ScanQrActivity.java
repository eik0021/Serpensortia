package com.example.serpensortia;

import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
        return R.id.action_search;
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
                }else{
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(this);
                    builder.setMessage("Nenalezeno, Skenovat znovu ?")
                            .setCancelable(false)
                            .setPositiveButton("Znovu", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    scanQr();
                                }
                            })
                            .setNegativeButton("Domů", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(ScanQrActivity.this, AnimalMainActivity.class));
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.setTitle("Záznam nenalezen");
                    alert.show();
                }
            }
        }
    }
}