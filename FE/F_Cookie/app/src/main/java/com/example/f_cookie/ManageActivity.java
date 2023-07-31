package com.example.f_cookie;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Intent;
import android.graphics.Color;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[] dayButtons;
    private boolean isEverydaySelected = false;
    private boolean[] isDaySelected = new boolean[7];
    private Button morningButton;
    private Button afternoonButton;
    private boolean isMorningSelected = false;
    private boolean isAfternoonSelected = false;

    String divId;

    //백엔드에서 가져온 데이터 저장용 변수 선언
    String mediName;    //약_이름
    String mediImg;     //약_생김새이미지
    String mediAmt;     //약_복용량
    boolean mediArm;    //복용_알림설정여부
    String mediMsg;     //복용_알림설정경우_메시지(ex: 매일 12:30에 1정투여)

    //백엔드 GET 설정 관련 ->
    public static Gson gson = new GsonBuilder().setLenient().create();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://43.202.15.83:8080/fortunecookie/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    public static RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
    // <- 백엔드 GET 설정 관련

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medlist_mng);

        Intent getIntent = getIntent();
        divId = getIntent.getStringExtra("divId");

        //복약관리 데이터 할당 함수
        Allocating();
        // -> 이제 리사이클러뷰에서 약 이름 텍스트 설정할 때 그냥 예를 들어 textView.setText(mediName); 이런식으로 사용하시면 돼요~!
        // 이미지 설정은 Glide 클래스 사용해서 Glide.with(this).load(shapeUrl).into(looks);
        // 이거 참고하시면 될 것 같아요 여기서 shapeUrl -> mediImg / looks -> 리사이클러 뷰의 이미지뷰 이거 두개만 바꾸시면 돼요!


        View alarmMngLayout = findViewById(R.id.alarmLayout);
        alarmMngLayout.setVisibility(View.VISIBLE);

        // 뒤로가기 버튼
        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dayButtons = new Button[8];
        dayButtons[0] = findViewById(R.id.buttonMonday);
        dayButtons[1] = findViewById(R.id.buttonTuesday);
        dayButtons[2] = findViewById(R.id.buttonWednesday);
        dayButtons[3] = findViewById(R.id.buttonThursday);
        dayButtons[4] = findViewById(R.id.buttonFriday);
        dayButtons[5] = findViewById(R.id.buttonSaturday);
        dayButtons[6] = findViewById(R.id.buttonSunday);
        dayButtons[7] = findViewById(R.id.buttonEveryday);

        for (Button dayButton : dayButtons) {
            dayButton.setOnClickListener(this);
        }

        morningButton = findViewById(R.id.morning_button);
        afternoonButton = findViewById(R.id.afternoon_button);

        morningButton.setOnClickListener(this);
        afternoonButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEveryday) {
            isEverydaySelected = !isEverydaySelected;
            for (int i = 0; i < 7; i++) {
                dayButtons[i].setSelected(isEverydaySelected);
                isDaySelected[i] = isEverydaySelected;
                // 요일 버튼 텍스트 색상 변경
                dayButtons[i].setTextColor(isEverydaySelected ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
            }
            // "매일" 버튼도 선택 상태에 맞게 설정
            dayButtons[7].setSelected(isEverydaySelected);
            // "매일" 버튼 텍스트 색상 변경
            dayButtons[7].setTextColor(isEverydaySelected ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
        }
        else if (v.getId() == R.id.morning_button) {
            isMorningSelected = !isMorningSelected;
            morningButton.setSelected(isMorningSelected);
            morningButton.setTextColor(isMorningSelected ? Color.BLACK : Color.WHITE);
            // 오후 버튼 선택 해제
            isAfternoonSelected = false;
            afternoonButton.setSelected(false);
            afternoonButton.setTextColor(Color.WHITE);
        } else if (v.getId() == R.id.afternoon_button) {
            isAfternoonSelected = !isAfternoonSelected;
            afternoonButton.setSelected(isAfternoonSelected);
            afternoonButton.setTextColor(isAfternoonSelected ? Color.BLACK : Color.WHITE);
            // 오전 버튼 선택 해제
            isMorningSelected = false;
            morningButton.setSelected(false);
            morningButton.setTextColor(Color.WHITE);
        }
        else {
            Button clickedButton = findViewById(v.getId());
            int index = Arrays.asList(dayButtons).indexOf(clickedButton);
            if (isDaySelected[index]) {
                clickedButton.setSelected(false);
                isDaySelected[index] = false;
                isEverydaySelected = false;
            } else {
                clickedButton.setSelected(true);
                isDaySelected[index] = true;
                // 다른 모든 요일 버튼이 선택되었는지 확인하고, "매일" 버튼도 선택되도록 설정
                boolean allSelected = true;
                for (boolean isSelected : isDaySelected) {
                    if (!isSelected) {
                        allSelected = false;
                        break;
                    }
                }
                dayButtons[7].setSelected(allSelected);
                isEverydaySelected = allSelected;
            }

            // 클릭한 요일 버튼 텍스트 색상 변경
            clickedButton.setTextColor(isDaySelected[index] ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
            // "매일" 버튼 텍스트 색상 변경
            dayButtons[7].setTextColor(isEverydaySelected ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
        }

    }

    void Allocating() {
        retrofitAPI.getTaking(divId).enqueue(new Callback<TakeMedicine>() {
            @Override
            public void onResponse(Call<TakeMedicine> call, Response<TakeMedicine> response) {
                if (response.isSuccessful()) {
                    TakeMedicine data = response.body();
                    System.out.println(data.toString());

                    saveInfo(data.getSubName(), data.getShapeUrl(), data.getAmount(), data.getMessage(), data.getAlarm());
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <5> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TakeMedicine> call, Throwable t) {
                System.out.println("<5> 실패" + call + "\n티는 " + t);
            }
        });
    }

    void saveInfo(String name, String url, String amt, String msg, boolean arm) {
        mediName = name;
        mediImg = url;
        mediAmt = amt;
        mediMsg = msg;
        mediArm = arm;
    }
}

// 우선 지금까지 구현한 부분은 알람 설정 페이지하고 요일버튼 눌렀을 때 색상변경, 매일누르면 모두 선택되고 매일 또 누르면 전체 해제되는 부분과
// 오전, 오후 버튼 클릭했을 때 한 가지 버튼만 선택할 수 있도록 구현했습니다. 앞으로 구현해야 하는것은 요일 버튼, 오전/오후 버튼, edittext로 입력받은
// 시간을 api 호출해서 데이터를 백엔드로 넘겨주고, 백엔드에서 다시 데이터를 받아온 후 medist_mng.xml에 구현해논 리스트 양식에 맞게 삽입되는 부분입니다.
// 이렇게 부탁드려서 죄송하고 감사합니다..ㅠㅠㅠㅠ 저는 medist_mng와 관련된 기능을 ManageActivity에 구현하는 작업을 진행하겠습니다.