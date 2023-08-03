package com.example.f_cookie;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShareActivity extends AppCompatActivity {

    String userName, userNum, medicine, divId;
    boolean ck, nameOk, numOk = false;

    TextView mName;
    ImageButton backBtn;
    Button shareBtn;
    EditText nameEdt, numEdt;

    //백엔드 GET 설정 관련 ->
    public static Gson gson = new GsonBuilder().setLenient().create();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://43.202.15.83:8080/fortunecookie/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    public static RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    postSMS postSMS;

    // <- 백엔드 GET 설정 관련

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_view);

        mName = findViewById(R.id.mName);
        backBtn = findViewById(R.id.s_backBtn);
        shareBtn = findViewById(R.id.share);
        nameEdt = findViewById(R.id.nameEdt);
        numEdt = findViewById(R.id.numEdt);

        backBtn.setOnClickListener(view -> {
            Intent goIntent = new Intent(this, MeDetailActivity.class);
            startActivity(goIntent);
        });

        Intent getIntent = getIntent();

        divId = getIntent.getStringExtra("divId");
        medicine  = getIntent.getStringExtra("medicine");
        userName = getIntent.getStringExtra("userName");
        userNum = getIntent.getStringExtra("userNum");

        mName.setText(medicine);

        if (userName == null && userNum == null) {
            ck = false;
        }
        if (userName != null && userNum != null) {
            ck = true;
        }

        System.out.println("sms 정보 확인 " + ck);

        if (ck == true) {
            nameEdt.setHint(userName);
            numEdt.setHint(userNum);

            nameEdt.setHintTextColor(Color.WHITE);
            numEdt.setHintTextColor(Color.WHITE);

            posting();
        }
        if (ck == false) {
            nameEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    nameOk = true;

                    if (nameOk == true && numOk == true) {
                        userName = nameEdt.getText().toString();
                        userNum = numEdt.getText().toString();
                        posting();
                    }
                }
            });

            numEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    numOk = true;

                    if (nameOk == true && numOk == true) {
                        userName = nameEdt.getText().toString();
                        userNum = numEdt.getText().toString();
                        posting();
                    }
                }
            });
        }
    }

    void posting() {
        shareBtn.setTextColor(Color.WHITE);

        medicine = medicine.replace("\n", "");
        postSMS = new postSMS(userName, medicine, userNum);

        retrofitAPI.postSMS(divId, postSMS).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("postSMS 성공 " + response.body());
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <11> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("<11> 실패 " + call + "\n티는 " + t);
            }
        });
    }
}
