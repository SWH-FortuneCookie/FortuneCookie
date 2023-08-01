package com.example.f_cookie;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrugWarningsActivity extends AppCompatActivity {

    String mediName, mediCaut;

    TextView name, warn;

    //백엔드 GET 설정 관련 ->
    public static Gson gson = new GsonBuilder().setLenient().create();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://43.202.15.83:8080/fortunecookie/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    public static RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_warning);

        name = findViewById(R.id.drug_name);
        warn = findViewById(R.id.drug_warning);

        Intent getIntent = getIntent();
        mediName = getIntent.getStringExtra("name");
        mediName = mediName.replace("\n", "");

        // 뒤로가기 버튼
        ImageButton backButton = findViewById(R.id.w_backbtn);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MeDetailActivity.class);
            startActivity(intent);
        });

        retrofitAPI.getCaution(mediName).enqueue(new Callback<GetCaution>() {
            @Override
            public void onResponse(Call<GetCaution> call, Response<GetCaution> response) {
                if (response.isSuccessful()) {
                    GetCaution data = response.body();

                    saveInfo(data.getSubName(), data.getCaution());
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <3> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCaution> call, Throwable t) {
                System.out.println("<3> 실패" + call + "\n티는 " + t);
            }
        });
    }

    void saveInfo(String subName, String caution) {
        mediName = subName;
        mediCaut = caution;

        System.out.println("<3>번 확인 " + mediName + "/" + mediCaut);

        name.setText(mediName.replace("&", "\n"));
        warn.setText(mediCaut.replace("\n", "\n\n"));
    }

}
