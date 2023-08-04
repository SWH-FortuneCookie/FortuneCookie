package com.example.f_cookie;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drugdetail_dialog);

        TextView textView = findViewById(R.id.messageTextView);

        String fullText = "해당 서비스의 원활한\n이용을 위해 약 상자와 함께\n보관해주세요.";
        SpannableString spannableString = new SpannableString(fullText);

        // 색을 바꿀 글자 범위 지정
        int startIndex = 19; // 변경할 텍스트의 시작 인덱스
        int endIndex = 28;  // 변경할 텍스트의 끝 인덱스

        spannableString = new SpannableString(fullText);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0093A7")), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 텍스트뷰에 SpannableString 설정
        textView.setText(spannableString);

        // 레이아웃 가져오기
        LinearLayout layout = findViewById(R.id.drugdetail_popup);

        // 배경을 투명하게 설정
        layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        Button closeButton = findViewById(R.id.closeBtn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // '확인' 버튼을 누르면 액티비티를 닫음
            }
        });
    }
}
