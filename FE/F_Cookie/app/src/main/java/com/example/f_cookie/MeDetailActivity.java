package com.example.f_cookie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MeDetailActivity extends AppCompatActivity {

    ImageButton backBtn;
    Button drugWarningsButton;
    Button addMngBtn;
    TextView name, feature, usage, store;
    TextView[] effect;
    TextView[] component;
    ImageView looks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_detail);

        backBtn = findViewById(R.id.d_backbtn);
        addMngBtn = findViewById(R.id.addlistButton);
        name = findViewById(R.id.drug_name);
        feature = findViewById(R.id.drug_featureinfo);
        usage = findViewById(R.id.drug_derections_info);
        store = findViewById(R.id.drug_storage_info);
//        effect = findViewById(R.id.drug_uses_text);
//        component = findViewById(R.id.drug_activeinformation_info);
        looks = findViewById(R.id.drug_featureimg);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // 사용상 주의사항 보러가기 버튼
        drugWarningsButton = findViewById(R.id.drug_warningsbtn);
        drugWarningsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, DrugWarningsActivity.class);
            startActivity(intent);
        });

        // 앱 시작 시 자동으로 팝업 액티비티 띄우기
        showPopupActivity();

        addMngBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ManageActivity.class);
            startActivity(intent);
        });
    }

        private void showPopupActivity() {
        Intent intent = new Intent(this, PopupActivity.class);
        startActivity(intent);

        // 팝업 액티비티의 배경을 투명하게 설정
        getWindow().setBackgroundDrawableResource(R.drawable.rounded_popup_background);
    }
}
