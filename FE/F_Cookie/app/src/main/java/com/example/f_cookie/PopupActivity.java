package com.example.f_cookie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drugdetail_dialog);

        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // '확인' 버튼을 누르면 액티비티를 닫음
            }
        });
    }
}
