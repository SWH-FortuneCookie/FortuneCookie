package com.example.f_cookie;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeDetailActivity extends AppCompatActivity {

    ImageButton backBtn;
    Button drugWarningsButton;
    Button addMngBtn, shareBtn;
    TextView name, feature, usage, store, effectTx;
    ImageView looks;
    FlexboxLayout e_view, c_view;

    String divId, subName, shapeUrl, description, dosage, storage;
    String[] efficacy, information;

    String userName, userNum;

    //백엔드 GET 설정 관련 ->
    public static Gson gson = new GsonBuilder().setLenient().create();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://43.202.15.83:8080/fortunecookie/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    public static RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    AddTaking addTaking;

    // <- 백엔드 GET 설정 관련

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_detail);

        Intent getIntent = getIntent();
        divId = getIntent.getStringExtra("divId");
        subName = getIntent.getStringExtra("subName");
        shapeUrl = getIntent.getStringExtra("shapeUrl");
        description = getIntent.getStringExtra("description");
        dosage = getIntent.getStringExtra("dosage");
        storage = getIntent.getStringExtra("storage");

        efficacy = getIntent.getStringArrayExtra("efficacy");
        System.out.println("받는거 확인 " + efficacy);
        information = getIntent.getStringArrayExtra("information");
        System.out.println("받는거 확인 info " + information);

        c_view = findViewById(R.id.c_view);
        e_view = findViewById(R.id.e_view);
        backBtn = findViewById(R.id.d_backbtn);
        shareBtn = findViewById(R.id.shareBtn);
        addMngBtn = findViewById(R.id.addlistButton);
        name = findViewById(R.id.drug_name);
        feature = findViewById(R.id.drug_featureinfo);
        usage = findViewById(R.id.drug_derections_info);
        store = findViewById(R.id.drug_storage_info);
        looks = findViewById(R.id.drug_featureimg);
        effectTx = findViewById(R.id.effectTx);

        GetMedicineInfo();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        shareBtn.setOnClickListener(view -> {
            Intent goIntent = new Intent(this, ShareActivity.class);

            goIntent.putExtra("divId", divId);
            goIntent.putExtra("medicine", subName);

            retrofitAPI.getSMS(divId).enqueue(new Callback<getSMS>() {
                @Override
                public void onResponse(Call<getSMS> call, Response<getSMS> response) {
                    if (response.isSuccessful()) {
                        getSMS data = response.body();

                        if (data.getName() != null && data.getPhone() != null) {
                            goIntent.putExtra("userName", data.getName());
                            goIntent.putExtra("userNum", data.getPhone());
                        }

                        startActivity(goIntent);
                    }
                    else {
                        try {
                            String body = response.errorBody().string();
                            Log.e(TAG, " <10> error - body : " + body);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<getSMS> call, Throwable t) {
                    System.out.println("<10> 실패 " + call + "\n티는 " + t);
                }
            });
        });

        // 사용상 주의사항 보러가기 버튼
        drugWarningsButton = findViewById(R.id.drug_warningsbtn);
        drugWarningsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, DrugWarningsActivity.class);
            intent.putExtra("name", subName);
            startActivity(intent);
        });

        // 앱 시작 시 자동으로 팝업 액티비티 띄우기
        showPopupActivity();

        addMngBtn.setOnClickListener(view -> {
            String name = subName.replace("\n", "");
            addTaking = new AddTaking(name);

            retrofitAPI.postTaking(divId, addTaking).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        System.out.println("AddTaking Post 성공 " + response.body());
                    }
                    else {
                        try {
                            String body = response.errorBody().string();
                            Log.e(TAG, " <4> error - body : " + body);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("<4> 실패 " + call + "\n티는 " + t);
                }
            });

            Intent intent = new Intent(this, ManageActivity.class);
            intent.putExtra("divId", divId);
            intent.putExtra("medicine", subName);
            startActivity(intent);
        });

        setEffData();
        setInfoData();
    }

    private void showPopupActivity() {
        Intent intent = new Intent(this, PopupActivity.class);
        startActivity(intent);

        // 팝업 액티비티의 배경을 투명하게 설정
        getWindow().setBackgroundDrawableResource(R.drawable.rounded_popup_background);
    }

    void setEffData() {
//        System.out.println("효능효과 개수 " + efficacy.length);
        TextView[] e_tv = new TextView[efficacy.length];
        for (int i = 1; i <= efficacy.length; i++) {
            int e_resId = getResources().getIdentifier("e_tv" + i, "id", "com.example.f_cookie");
            e_tv[i - 1] = findViewById(e_resId);
            e_tv[i - 1].setVisibility(View.VISIBLE);
//            System.out.println("비지빌리티 확인 : " + e_tv[i - 1].getVisibility());
            if (i == 1) {
                String t = efficacy[0].replace("[", "");
//                e_tv[0].setText(t);
                e_tv[0].setVisibility(View.GONE);
                effectTx.setText(t);
            }
            else {
                e_tv[i - 1].setText((efficacy[i-1]).replace("]", ""));
            }
        }
    }

    void setInfoData() {
//        System.out.println("성분정보 개수 " + information.length);
        TextView[] c_tv = new TextView[information.length];
        for (int i = 1; i <= information.length; i++) {
            int c_resId = getResources().getIdentifier("c_tv" + i, "id", "com.example.f_cookie");
            c_tv[i - 1] = findViewById(c_resId);
            c_tv[i - 1].setVisibility(View.VISIBLE);
//            System.out.println("비지빌리티 확인 : " + c_tv[i - 1].getVisibility());
            String t = information[i-1].replace("[", "");
            t = t.replace("]", "");
            c_tv[i - 1].setText(t);
        }
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
    }

    void saveSMS(String userName, String userNum) {
        this.userName = userName;
        this.userNum = userNum;
    }
}
