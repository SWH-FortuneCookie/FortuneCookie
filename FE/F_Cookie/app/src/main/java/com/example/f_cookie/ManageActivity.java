package com.example.f_cookie;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String selectedDay = ""; // 선택한 요일을 저장하는 변수
    private Button save;
    // 리사이클러뷰와 어댑터 관련 멤버 변수
    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<MedicineItem> medicineList;

    String divId;
    int count;

    //백엔드에서 가져온 데이터 저장용 변수 선언
    String[] mediName;    //약_이름
    String[] mediImg;     //약_생김새이미지
    String[] mediAmt;     //약_복용량
    boolean mediArm;    //복용_알림설정여부
    String[] mediMsg;     //복용_알림설정경우_메시지(ex: 매일 12:30에 1정투여)

    //테스트용으로 써주세요!
    public class MedicineItem {
        String name;  //약 이름
        String img;   //약 생김새이미지
        String amt;   //약 복용량
        String arm;   //복용 알림설정여부
        String msg;   //복용 알림설정경우 메시지

        public MedicineItem(String subName, String shapeUrl, String amount, String message, boolean alarm) {
            this.name = subName;
            this.img = shapeUrl;
            this.amt = amount;
            this.msg = message;
            this.arm = String.valueOf(alarm);
        }
    }

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
        divId = getIntent.getStringExtra("MainToDivId");
        System.out.println("매니지 액티비티 확인 " + divId);

        //복약관리 데이터 할당 함수
        Allocating();

        // medlist_mng = visible / alarm_mng = gone

        // ******* 삭제하기 버튼 리스너 안에 할당할 함수 *******
        //Delete();

        // -> 이제 리사이클러뷰에서 약 이름 텍스트 설정할 때 그냥 예를 들어 textView.setText(mediName); 이런식으로 사용하시면 돼요~!
        // 이미지 설정은 Glide 클래스 사용해서 Glide.with(this).load(shapeUrl).into(looks);
        // 이거 참고하시면 될 것 같아요 여기서 shapeUrl -> mediImg / looks -> 리사이클러 뷰의 이미지뷰 이거 두개만 바꾸시면 돼요!

        View alarmMngLayout = findViewById(R.id.alarmLayout);
        alarmMngLayout.setVisibility(View.VISIBLE);

        View alarmScrollview = findViewById(R.id.alarm_scroll);
        alarmScrollview.setVisibility(View.GONE);

        // 뒤로가기 버튼
        ImageButton backButton = findViewById(R.id.backBtn);
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

        // 리사이클러뷰 초기화
        recyclerView = findViewById(R.id.mediListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineList = new ArrayList<>();
        adapter = new MedicineAdapter(medicineList);
        recyclerView.setAdapter(adapter);

        // 저장하기 버튼 초기화 및 클릭 리스너 설정
        Button save = findViewById(R.id.save);
        save.setOnClickListener(this);

    }

    private void setAlarm() {
        Button setBtn = findViewById(R.id.setBtn);
        Button modifyBtn = findViewById(R.id.modifyBtn);
        View alarmScrollview = findViewById(R.id.alarm_scroll);

        // 알람 설정 버튼 클릭 시 alarm_mng 스크롤뷰 보이기
        setBtn.setOnClickListener(view -> {
            alarmScrollview.setVisibility(View.VISIBLE);
        });
        modifyBtn.setOnClickListener(view -> {
        });
        modifyBtn.setVisibility(View.GONE);
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
        } else if (v.getId() == R.id.morning_button) {
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
        } else if (v.getId() == R.id.save) {
            addNewItem();
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

        // 선택한 요일을 가져와서 변수로 저장
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

    void Allocating() {
        retrofitAPI.getTaking(divId).enqueue(new Callback<List<TakeMedicine>>() {
            @Override
            public void onResponse(Call<List<TakeMedicine>> call, Response<List<TakeMedicine>> response) {
                if (response.isSuccessful()) {
                    List<TakeMedicine> data = response.body();
                    System.out.println(data.toString());

                    List<String> oldList = Arrays.asList(data.toString().split(","));
                    String[] mArrays = oldList.toArray(new String[oldList.size()]);

                    count = mArrays.length;
//                    System.out.println(count + "개수");
                    saveInfo(count, mArrays);

//                    saveInfo(data.getSubName(), data.getShapeUrl(), data.getAmount(), data.getMessage(), data.getAlarm());

                    // 받아온 데이터를 MedicineItem으로 변환하여 medicineList에 추가
                    medicineList.clear();
                    for (TakeMedicine medicine : data) {
                        MedicineItem item = new MedicineItem(
                                medicine.getSubName(),
                                medicine.getShapeUrl(),
                                medicine.getAmount(),
                                medicine.getMessage(),
                                medicine.getAlarm()
                        );
                        medicineList.add(item);
                    }
                    // 어댑터 갱신
                    adapter.notifyDataSetChanged();

                } else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <5> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TakeMedicine>> call, Throwable t) {
                System.out.println("<5> 실패" + call + "\n티는 " + t);
            }
        });
    }

    void Delete() {
        String name = "";

        //일단 복약관리 5개까지만 커버..
        if (count == 5) {
            name = mediName[0].replace("\n", "");
        }
        if (count == 10) {
            name = mediName[1].replace("\n", "");
        }
        if (count == 15) {
            name = mediName[2].replace("\n", "");
        }
        if (count == 20) {
            name = mediName[3].replace("\n", "");
        }
        if (count == 25) {
            name = mediName[4].replace("\n", "");
        }
        if (count == 30) {
            name = mediName[5].replace("\n", "");
        }
        if (count == 35) {
            name = mediName[6].replace("\n", "");
        }
        if (count == 40) {
            name = mediName[7].replace("\n", "");
        }
        if (count == 45) {
            name = mediName[8].replace("\n", "");
        }
        if (count == 50) {
            name = mediName[9].replace("\n", "");
        }

        deleteTaking deleteTaking = new deleteTaking(name);

        retrofitAPI.deleteTake(divId, deleteTaking).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("taking delete 성공 " + response.body());
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <9> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("<9> 실패 " + call + "\n티는 " + t);
            }
        });
    }

    void saveInfo(int count, String[] arry) {
        mediName = new String[count / 5];
        mediImg = new String[count / 5];
        mediAmt = new String[count / 5];
        mediMsg = new String[count / 5];
//        mediArm

        for (int i = 0; i < count / 5; i++) {
            mediName[i] = arry[i * 5];
            mediImg[i] = arry[i * 5 + 1];
            mediAmt[i] = arry[i * 5 + 2];
            mediMsg[i] = arry[i * 5 + 3];
            //알람여부
        }
//        System.out.println("확인 " + mediName[1]);
    }

    // 리사이클러뷰 어댑터 클래스
    private class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

        private List<MedicineItem> items;

        public MedicineAdapter(List<MedicineItem> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_medi, parent, false);
            return new ViewHolder(view);
        }

        // 이름이랑 복용량 변수 할당한 부분이 밑에 부분입니다..!

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MedicineItem item = items.get(position);

            // 약 생김새 이미지
            Glide.with(holder.itemView.getContext()).load(mediImg).into(holder.shapeImageView);

            // 약 이름 설정
            String mediName = item.name.replace("&", "\n");
            holder.mediNameTextView.setText(mediName);

            // 약 복용량 설정
            holder.mediAmtTextView.setText(item.amt);

            // 복용 알림설정 경우 메시지 설정
            holder.medimsgTextView.setText(item.msg);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView shapeImageView;
            TextView mediNameTextView;
            TextView mediAmtTextView;
            TextView medimsgTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                shapeImageView = itemView.findViewById(R.id.medi_shape);
                mediNameTextView = itemView.findViewById(R.id.medi_name);
                mediAmtTextView = itemView.findViewById(R.id.medi_amt);
                medimsgTextView = itemView.findViewById(R.id.alarm_msg);
            }
        }
    }
    private void addNewItem() {
        // 새로운 아이템 생성 및 리스트에 추가
        MedicineItem newItem = new MedicineItem("New Medicine", "url_new_medi_Image", "New Amount", "New Message", true);
        medicineList.add(newItem);

        // 어댑터에 변경 사항 알림
        adapter.notifyDataSetChanged();
    }
}

