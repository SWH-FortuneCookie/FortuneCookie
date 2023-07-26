package com.example.f_cookie;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DrugWarningsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_warning);

        // 뒤로가기 버튼
        ImageButton backButton = findViewById(R.id.w_backbtn);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MeDetailActivity.class);
            startActivity(intent);
        });

    }


}
