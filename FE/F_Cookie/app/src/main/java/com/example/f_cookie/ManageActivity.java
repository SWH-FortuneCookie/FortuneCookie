package com.example.f_cookie;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[] dayButtons;
    private boolean isEverydaySelected = false;
    private boolean[] isDaySelected = new boolean[7];
    private Button morningButton;
    private Button afternoonButton;
    private boolean isMorningSelected = false;
    private boolean isAfternoonSelected = false;
    private String selectedDay = ""; // 선택한 요일을 저장하는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medlist_mng);

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

        // 알람 설정하기 버튼 초기화
        Button setBtn = findViewById(R.id.setBtn);
        setBtn.setOnClickListener(this);

        // 수정하기 버튼 초기화
        Button modifyBtn = findViewById(R.id.modifyBtn);
        modifyBtn.setOnClickListener(this);
        modifyBtn.setVisibility(View.GONE); // 일단 숨김 처리

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

        // 알람을 설정한 후에는 '수정하기' 버튼을 보이도록 설정
        Button setBtn = findViewById(R.id.setBtn);
        Button modifyBtn = findViewById(R.id.modifyBtn);
        setBtn.setVisibility(View.GONE);
        modifyBtn.setVisibility(View.VISIBLE);

        // 시와 분을 가져와서 변수로 저장
        EditText editTextHour = findViewById(R.id.hourEditText);
        EditText editTextMinute = findViewById(R.id.minuteEditText);

        int hour = 0;
        int minute = 0;

        if (!editTextHour.getText().toString().isEmpty()) {
            hour = Integer.parseInt(editTextHour.getText().toString());
        }

        if (!editTextMinute.getText().toString().isEmpty()) {
            minute = Integer.parseInt(editTextMinute.getText().toString());
        }

        if (v.getId() == R.id.setBtn || v.getId() == R.id.modifyBtn) {
            selectedDay = ""; // 기존에 저장된 요일 초기화
            if (isEverydaySelected) {
                selectedDay = "매일";
            } else {
                String[] dayOfWeek = {"월", "화", "수", "목", "금", "토", "일"};
                for (int i = 0; i < isDaySelected.length; i++) {
                    if (isDaySelected[i]) {
                        selectedDay += dayOfWeek[i] + " ";
                    }
                }
            }
        }
    }
}