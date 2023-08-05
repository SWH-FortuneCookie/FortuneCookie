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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
    private EditText hourEditText;
    private EditText minuteEditText;
    View alarmLayout;
    TextView manTxt;
    ImageButton backButton, mngBack;

    String divId, medicine;
    int count;

    int hour, minute;

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
        if (divId == null) {
            divId = getIntent.getStringExtra("divId");
        }
        medicine = getIntent.getStringExtra("medicine");
        System.out.println("매니지 액티비티 확인 " + divId);

        alarmLayout = findViewById(R.id.alarmLayout);

        //복약관리 데이터 할당 함수
        Allocating();

        // medlist_mng = visible / alarm_mng = gone

        // ******* 삭제하기 버튼 리스너 안에 할당할 함수 *******
        //Delete();

        // -> 이제 리사이클러뷰에서 약 이름 텍스트 설정할 때 그냥 예를 들어 textView.setText(mediName); 이런식으로 사용하시면 돼요~!
        // 이미지 설정은 Glide 클래스 사용해서 Glide.with(this).load(shapeUrl).into(looks);
        // 이거 참고하시면 될 것 같아요 여기서 shapeUrl -> mediImg / looks -> 리사이클러 뷰의 이미지뷰 이거 두개만 바꾸시면 돼요!

//        View alarmMngLayout = findViewById(R.id.alarmLayout);
//        alarmMngLayout.setVisibility(View.VISIBLE);

//        View alarmScrollview = findViewById(R.id.alarm_scroll);
//        alarmScrollview.setVisibility(View.VISIBLE);

        // 시, 분 edittext 배경 활성화
        hourEditText = findViewById(R.id.hourEditText);
        minuteEditText = findViewById(R.id.minuteEditText);

        hourEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    hourEditText.setBackgroundResource(R.drawable.time_button); // 포커스를 받을 때 배경 변경
                } else {
                    hourEditText.setBackgroundResource(R.drawable.time_button); // 포커스를 잃을 때 배경 변경
                }
            }
        });

        minuteEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    minuteEditText.setBackgroundResource(R.drawable.time_button); // 포커스를 받을 때 배경 변경
                } else {
                    minuteEditText.setBackgroundResource(R.drawable.time_button_inactive); // 포커스를 잃을 때 배경 변경
                }
            }
        });
        // 뒤로가기 버튼
        backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mngBack = findViewById(R.id.mng_backBtn);
        mngBack.setOnClickListener(view -> {
            backButton.setVisibility(View.VISIBLE);
            manTxt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            alarmLayout.setVisibility(View.GONE);
        });

        dayButtons = new Button[8];
        dayButtons[0] = findViewById(R.id.buttonSunday);
        dayButtons[1] = findViewById(R.id.buttonMonday);
        dayButtons[2] = findViewById(R.id.buttonTuesday);
        dayButtons[3] = findViewById(R.id.buttonWednesday);
        dayButtons[4] = findViewById(R.id.buttonThursday);
        dayButtons[5] = findViewById(R.id.buttonFriday);
        dayButtons[6] = findViewById(R.id.buttonSaturday);
        dayButtons[7] = findViewById(R.id.buttonEveryday);

        for (Button dayButton : dayButtons) {
            dayButton.setOnClickListener(this);
        }

        manTxt = findViewById(R.id.manTxt);
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
        save.setOnClickListener(view -> {
            postAlarm();
        });

        // 아이템이 없을 때 noneDrugLayout을 보여주고, 아이템이 추가될 때 숨김 (카운트 세는걸로 수정)
        LinearLayout noneDrugLayout = findViewById(R.id.none_drug);

//        if (adapter.getItemCount() == 0) {
//            noneDrugLayout.setVisibility(View.VISIBLE);
//        } else {
//            noneDrugLayout.setVisibility(View.GONE);
//        }
    }

    private void setAlarm() {
        backButton.setVisibility(View.GONE);
        manTxt.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        alarmLayout.setVisibility(View.VISIBLE);

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
        }
        else if (v.getId() == R.id.save) {
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

            // '삭제하기' 버튼을 눌렀을 때 GONE 설정
            if (v.getId() == R.id.deleteBtn) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                View dialogView = getLayoutInflater().inflate(R.layout.delete_dialog, null);

                Button dialogDeleteButton = dialogView.findViewById(R.id.closeBtn);

                alertDialogBuilder.setView(dialogView);
                AlertDialog alertDialog = alertDialogBuilder.create();

                dialogDeleteButton.setOnClickListener(view -> {
                    // 해당 아이템의 visibility를 GONE으로 변경하고 어댑터에 변경 사항 알림
                    int position = recyclerView.getChildAdapterPosition(dialogDeleteButton);
                    if (position != RecyclerView.NO_POSITION) {
                        MedicineItem item = medicineList.get(position);
                        View itemView = recyclerView.getChildAt(position);
                        if (itemView != null) {
                            itemView.setVisibility(View.GONE);
                        }
                        medicineList.remove(position);
                        adapter.notifyItemRemoved(position);

                        Delete();
                    }
                    alertDialog.dismiss();
                });

                alertDialog.show();
            }
        }

        // 시와 분을 가져와서 변수로 저장
        EditText editTextHour = findViewById(R.id.hourEditText);
        EditText editTextMinute = findViewById(R.id.minuteEditText);

        hour = 0;
        minute = 0;

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

        // 시간, 요일이 모두 선택되었을 경우 '저장하기' 버튼의 배경을 변경
        Button saveButton = findViewById(R.id.save);
        if (!selectedDay.isEmpty() && (isMorningSelected || isAfternoonSelected) && (hour > 0 && hour <= 12) && (minute >= 0 && minute < 60)) {
            saveButton.setBackgroundResource(R.drawable.save_active); // 저장 가능한 상태
        } else {
            saveButton.setBackgroundResource(R.drawable.save_inactive); // 저장 불가능한 상태
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
                    System.out.println("아이템 개수 " + adapter.getItemCount());
                    if (adapter.getItemCount() != 0) {
                        // 아이템이 추가된 후에 noneDrugLayout을 숨김
                        LinearLayout noneDrugLayout = findViewById(R.id.none_drug);
                        noneDrugLayout.setVisibility(View.GONE);
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

    void deletePopup(int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.delete_dialog, null);

        Button dialogDeleteButton = dialogView.findViewById(R.id.closeBtn);

        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.create();

        dialogDeleteButton.setOnClickListener(view -> {
            // 해당 아이템의 visibility를 GONE으로 변경하고 어댑터에 변경 사항 알림

            if (position != RecyclerView.NO_POSITION) {
                MedicineItem item = medicineList.get(position);
                View itemView = recyclerView.getChildAt(position);
                if (itemView != null) {
                    itemView.setVisibility(View.GONE);
                }
                medicineList.remove(position);
                adapter.notifyItemRemoved(position);

                Delete();
            }
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    void Delete() {
        String name = "";

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
            Glide.with(holder.itemView.getContext()).load(item.img).into(holder.shapeImageView);

            // 약 이름 설정
            String mediName = item.name.replace("&", "\n");
            holder.mediNameTextView.setText(mediName);

            // 약 복용량 설정
            holder.mediAmtTextView.setText(item.amt);

            // 복용 알림설정 경우 메시지 설정
            holder.medimsgTextView.setText(item.msg);

            holder.setBtn.setOnClickListener(view -> {
                setAlarm();

//                putAlarm putAlarm = new putAlarm(mediName, day, hour, minute);
//
//                retrofitAPI.putAlarm(divId, putAlarm).enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (response.isSuccessful()) {
//                            System.out.println("putAlarm 성공 " + response.body());
//                        }
//                        else {
//                            try {
//                                String body = response.errorBody().string();
//                                Log.e(TAG, " <8> error - body : " + body);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        System.out.println("<8> 실패 " + call + "\n티는 " + t);
//                    }
//                });
            });

            holder.deleteBtn.setOnClickListener(view -> {
                deletePopup(position);
            });
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
            Button setBtn, deleteBtn;

            public ViewHolder(View itemView) {
                super(itemView);
                shapeImageView = itemView.findViewById(R.id.medi_shape);
                mediNameTextView = itemView.findViewById(R.id.medi_name);
                mediAmtTextView = itemView.findViewById(R.id.medi_amt);
                medimsgTextView = itemView.findViewById(R.id.alarm_msg);
                setBtn = itemView.findViewById(R.id.setBtn);
                deleteBtn = itemView.findViewById(R.id.deleteBtn);
            }
        }
    }
    private void addNewItem() {
        // 새로운 아이템 생성 및 리스트에 추가
        MedicineItem newItem = new MedicineItem("New Medicine", "url_new_medi_Image", "New Amount", "New Message", true);
        medicineList.add(newItem);

        // 어댑터에 변경 사항 알림
        adapter.notifyDataSetChanged();

        // 아이템이 추가된 후에 noneDrugLayout을 숨김
        LinearLayout noneDrugLayout = findViewById(R.id.none_drug);
        noneDrugLayout.setVisibility(View.GONE);
    }

    void postAlarm() {
        System.out.println(medicine +"확인");
        List day = new ArrayList<>();

        postAlarm postAlarm = new postAlarm(medicine, day, hour, minute);
        retrofitAPI.postAlarm(divId, postAlarm).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("postAlarm 성공 " + response.body());
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <6> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("<6> 실패 " + call + "\n티는 " + t);
            }
        });
    }

    void getAlarm() {
        retrofitAPI.getAlarm(divId).enqueue(new Callback<getAlarm>() {
            @Override
            public void onResponse(Call<getAlarm> call, Response<getAlarm> response) {
                if(response.isSuccessful()) {
                    getAlarm data = response.body();

                    String name = data.getName();
                    List days = data.getDays();
                    int hour = data.getHour(); //24시간제로 저장됨
                    int minute = data.getMinute();

                    System.out.println("수정하기 눌렀을 때 저장될 데이터 테스트\n" + name +"\n" + days + "\n" +
                            hour + "시 " + minute + "분");

                    //알림 설정 페이지에 받아와진 데이터 표시

                    modifyAlarm();
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <7> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<getAlarm> call, Throwable t) {
                System.out.println("<7> 실패 " + call + "\n티는 " + t);
            }
        });
    }

    void modifyAlarm() {
        List day = new ArrayList<>();

        putAlarm putAlarm = new putAlarm(medicine, day, hour, minute);

        retrofitAPI.putAlarm(divId, putAlarm).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("putAlarm 성공 " + response.body());
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <8> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("<8> 실패 " + call + "\n티는 " + t);
            }
        });
    }
}

