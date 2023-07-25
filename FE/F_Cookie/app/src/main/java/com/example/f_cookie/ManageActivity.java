package com.example.f_cookie;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.medlist_mng);

//        피그마에서 내 복약관리 첫 번째 페이지에 들어오면 medlist_mng.xml 의 레이아웃이 보이고 그 레이아웃이랑
//        연결시켜 놓은 자바 파일이 이거예요! 복약 관리 정보(사각형 정보틀)들은 리사이클러뷰로 구현하려고
//        medlist_mng에 리사이클러뷰 선언해놓고 뷰 안에 사각형은 item으로 item_medi.xml 파일 안에 만들어둬서
//        이 틀이 리사이클 되면서 보이게 되는거거든요 그래서 이 부분 구현하는 거 제가 했던 프로젝트에서 그대로 썼었어가지고
//        아래 깃허브 링크 첨부드려요 코틀린 코드이긴 한데 도움될 것 같아서요 74번째 줄인가부터 보시면 돼요!
//        변수 중에 FeedDTO 뭐라고 되어 있는건 백엔드에서 가져온 변수 저장하는 변수라서 감안해서 보시면 될 것 같아요
//        https://github.com/UzzerDiyonce/once/blob/main/app/src/main/java/com/example/once/Feed.kt
//
//        그리고 medlist_mng.xml에
//        <include layout="@layout/content_main"
//        android:id="@+id/detailLayout"
//        android:visibility="gone"/>
//        이런 식으로 코드 넣어 놓으면 medlist_mng 레이아웃 위에 레이아웃 하나를 더 띄울 수 있거든요
//        이 부분  home_main.xml 이랑 content_main.xml 파일 참고하시면 될 것 같아요
//        medlist_mng위에 alarm_mng 레이아웃 띄워서 알람설정 UI 구현하려고 만들어 뒀어요 그래서 피그마 내 복약관리
//        두번째 이미지는 alarm_mng.xml에 만들어주시면 될 것 같아요


    }
}
