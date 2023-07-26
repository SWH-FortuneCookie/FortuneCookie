package com.example.f_cookie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatActivity;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[] dayButtons;
    private boolean isEverydaySelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    @Override
    public void onClick(View v) {
        // '매일' 버튼을 클릭한 경우
        if (v.getId() == R.id.buttonEveryday) {
            isEverydaySelected = !isEverydaySelected;
            if (isEverydaySelected) {
                // '매일' 버튼이 선택된 경우
                for (Button dayButton : dayButtons) {
                    dayButton.setBackgroundColor(Color.WHITE);
                    dayButton.setTextColor(Color.BLACK);
                }
            } else {
                // '매일' 버튼이 선택되지 않은 경우
                for (Button dayButton : dayButtons) {
                    dayButton.setBackgroundColor(Color.parseColor("#181818"));
                    dayButton.setTextColor(Color.WHITE);
                }
            }
        } else {
            // 다른 요일 버튼을 클릭한 경우
            Button clickedButton = findViewById(v.getId());
            clickedButton.setBackgroundColor(Color.WHITE);
            clickedButton.setTextColor(Color.BLACK);
            // '매일' 버튼 선택 상태 해제
            dayButtons[7].setBackgroundColor(Color.parseColor("#181818"));
            dayButtons[7].setTextColor(Color.WHITE);
            isEverydaySelected = false;
        }
    }
}
