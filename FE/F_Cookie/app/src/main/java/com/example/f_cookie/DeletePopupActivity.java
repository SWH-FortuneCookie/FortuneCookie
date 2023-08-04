package com.example.f_cookie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeletePopupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_dialog);

        Button closeButton = findViewById(R.id.closeBtn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // '확인' 버튼을 누르면 리사이클러뷰 아이템 삭제
            }
        });
    }
}
