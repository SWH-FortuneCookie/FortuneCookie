package com.example.f_cookie;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

import org.w3c.dom.Text;

public class MeDetailActivity extends AppCompatActivity {

    ImageButton backBtn;
    Button drugWarningsButton;
    Button addMngBtn;
    TextView name, feature, usage, store;
    ImageView looks;
    FlexboxLayout e_view, c_view;

    String subName, shapeUrl, description, dosage, storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_detail);

        Intent getIntent = getIntent();
        subName = getIntent.getStringExtra("subName");
        shapeUrl = getIntent.getStringExtra("shapeUrl");
        description = getIntent.getStringExtra("description");
        dosage = getIntent.getStringExtra("dosage");
        storage = getIntent.getStringExtra("storage");

        c_view = findViewById(R.id.c_view);
        e_view = findViewById(R.id.e_view);
        backBtn = findViewById(R.id.d_backbtn);
        addMngBtn = findViewById(R.id.addlistButton);
        name = findViewById(R.id.drug_name);
        feature = findViewById(R.id.drug_featureinfo);
        usage = findViewById(R.id.drug_derections_info);
        store = findViewById(R.id.drug_storage_info);
        looks = findViewById(R.id.drug_featureimg);

        GetMedicineInfo();

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

        TextView[] e_tv = new TextView[9];
        for (int i = 1; i < 10; i++) {
            int e_resId = getResources().getIdentifier("e_tv" + i, "id", "com.example.f_cookie");
            e_tv[i - 1] = findViewById(e_resId);
        }

        TextView[] c_tv = new TextView[11];
        for (int i = 1; i < 12; i++) {
            int c_resId = getResources().getIdentifier("c_tv" + i, "id", "com.example.f_cookie");
            c_tv[i - 1] = findViewById(c_resId);
        }
    }

    private void showPopupActivity() {
        Intent intent = new Intent(this, PopupActivity.class);
        startActivity(intent);

        // 팝업 액티비티의 배경을 투명하게 설정
        getWindow().setBackgroundDrawableResource(R.drawable.rounded_popup_background);
    }

    void GetMedicineInfo() {
        //이름
        subName = subName.replace("&", "\n");
        name.setText(subName);
        //생김새
        Glide.with(this).load(shapeUrl).into(looks);
        //생김새정보
        feature.setText(description);
        //용법용량
        usage.setText(dosage);
        //저장방법
        store.setText(storage);
        //성분정보

        //효능효과
    }
}
