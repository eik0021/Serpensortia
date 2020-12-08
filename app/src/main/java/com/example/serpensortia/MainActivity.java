package com.example.serpensortia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private EditText pswdTxt;
    private String password;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PSWD = "password";
    private SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        password = sharedpreferences.getString(PSWD, "");

        TextView msg_text = findViewById(R.id.txt_msg);
        Button login_btn = findViewById(R.id.login_btn);

        pswdTxt = findViewById(R.id.editTextTextPassword);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                msg_text.setText("Můžete použít senzor pro přihlášení");
                msg_text.setTextColor(Color.parseColor("#Fafafa"));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msg_text.setText("Zařízení nedisponuje senzorem otisku prstu");
                login_btn.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msg_text.setText("Senzor otisků prstů není dostupný");
                login_btn.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msg_text.setText("Zařízení nemá uložený otisk prstu, prosím nastavte si otisk pro přístup do aplikace");
                login_btn.setVisibility(View.GONE);
                break;
        }

        final Executor executor = ContextCompat.getMainExecutor(this);
        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Úspěšné přihlášení", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(),
                        AnimalMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("LOGIN")
                .setDescription("Použite otisk prstu pro přihlášení do aplikace")
                .setNegativeButtonText("zavřít")
                .build();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });


    }

    public void loginWithPswd(View view) {
        if(password.equals(pswdTxt.getText().toString())){
            Intent intent = new Intent(getApplicationContext(),
                    AnimalMainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Špatné heslo!!!", Toast.LENGTH_SHORT).show();
        }
    }
}