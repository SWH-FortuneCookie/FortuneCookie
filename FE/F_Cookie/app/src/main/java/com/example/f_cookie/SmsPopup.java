package com.example.f_cookie;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SmsPopup extends AppCompatActivity {
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_pop);

        ok = findViewById(R.id.smsOkBtn);
        ok.setOnClickListener(view -> {
            finish();
        });
    }
}
