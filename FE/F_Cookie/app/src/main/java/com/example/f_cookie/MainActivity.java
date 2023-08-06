package com.example.f_cookie;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCvsAkYUkIlKG7c4yiSjsKe9MHRrtjNE6M";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private View detailLayout;
    private TextView mImageDetails;
    private ImageView mMainImage;

    private static TextView reverseTxt, againTxt, descriptTxt;
    private static Button photoBtn;

    private Button getInfoBtn;
    private Button getManagBtn;
    private static int picErrCount = 0;
    private static String divId = "";
    private static String temp = "";
    private static boolean vb = false;
    private static boolean next = false;

    public static Context context;

    public String token = "";
    public static String medicine = "";

    public static String subName, shapeUrl, description, dosage, storage;
    public static String[] efficacy;
    public static String[] information;
    //efficacy, information 처리

    LoginRequest loginRequest;

    public static Gson gson = new GsonBuilder().setLenient().create();

    public static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://43.202.15.83:8080/fortunecookie/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    public static RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

//        Gson gson = new GsonBuilder().setLenient().create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://43.202.15.83:8080/fortunecookie/")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
//
////        Post post = new Post();
//
//        HashMap<String, String> input = new HashMap<>();

        context = this;

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        divId = getDivId(MainActivity.this);
        System.out.println("디바이스 아이디 " + divId);
        //디바이스 아이디 전달

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        saveToken(token);
                    }
                });

        getInfoBtn = findViewById(R.id.infoBtn);
        getManagBtn = findViewById(R.id.manageBtn);

        getInfoBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("사진을 촬영해주세요").setPositiveButton("카메라", (dialog, which) -> startCamera());
            builder.create().show();
        });

        getManagBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ManageActivity.class);
            intent.putExtra("MainToDivId", divId);
            startActivity(intent);
        });

        detailLayout = findViewById(R.id.detailLayout);
        mImageDetails = findViewById(R.id.image_details);
        mMainImage = findViewById(R.id.main_image);
        reverseTxt = findViewById(R.id.reverseTxt);
        againTxt = findViewById(R.id.againTxt);
        descriptTxt = findViewById(R.id.descriptTxt);
        photoBtn = findViewById(R.id.photoBtn);

        photoBtn.setOnClickListener(view -> {
            startCamera();
            againTxt.setVisibility(View.GONE);
            reverseTxt.setVisibility(View.GONE);
            photoBtn.setVisibility(View.GONE);
        });
    }

    public void saveToken(String tk) {
        token = tk;

        loginRequest = new LoginRequest(divId, token);
        System.out.println("확인" + loginRequest.device + "토큰" + loginRequest.fcmToken);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        initMyApi initMyApi = RetrofitClient.getRetrofitInterface();

        initMyApi.getLoginResponse(loginRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("Test Post 성공 " + response.body());
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <1> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("실패 " + call + "\n티는 " + t);
            }
        });
    }

    public void DetailPage() {
        Intent intent = new Intent(this, MeDetailActivity.class);

        intent.putExtra("divId", divId);

        intent.putExtra("subName", subName);
        intent.putExtra("shapeUrl", shapeUrl);
        intent.putExtra("description", description);
        intent.putExtra("dosage", dosage);
        intent.putExtra("storage", storage);
        System.out.println("보내는 거 확인 " + efficacy);
        intent.putExtra("efficacy", efficacy);
        intent.putExtra("information", information);

        startActivity(intent);
    }

    public static String getDivId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_MEDIA_IMAGES)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }

        Vibrate();
        if (vb == true) {
            Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(VibrationEffect.createOneShot(1000, 100));
            vb = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (com.example.f_cookie.PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (com.example.f_cookie.PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                callCloudVision(bitmap);
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, "Something is wrong with that image. Pick a different one please", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, "Something is wrong with that image. Pick a different one please", Toast.LENGTH_LONG).show();
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = com.example.f_cookie.PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature textDetection = new Feature();
                textDetection.setType("TEXT_DETECTION");
                textDetection.setMaxResults(10);
                add(textDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<MainActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(MainActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            MainActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                TextView imageDetail = activity.findViewById(R.id.image_details);
                imageDetail.setText(result);
            }

            if (picErrCount == 1) {
                descriptTxt.setVisibility(View.INVISIBLE);
                Vibrate();
                reverseTxt.setVisibility(View.VISIBLE);
                photoBtn.setVisibility(View.VISIBLE);
            }
            if (picErrCount >= 2) {
                descriptTxt.setVisibility(View.INVISIBLE);
                Vibrate();
                againTxt.setVisibility(View.VISIBLE);
                photoBtn.setVisibility(View.VISIBLE);

                picErrCount = 0;
            }
        }
    }

    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        detailLayout.setVisibility(View.VISIBLE);
        mImageDetails.setText("Uploading image. Please wait.");

        // 인식 중 UI 여기!
        descriptTxt.setVisibility(View.VISIBLE);
        photoBtn.setVisibility(View.VISIBLE);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("I found these things:\n\n");

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();

        int repeatCount = 0;
        String store = "";

        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.append(String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription()));
                //System.out.println("가져온 것 테스트: " + label.getDescription());
                message.append("\n");

                repeatCount++;

                store = label.getDescription();

                if (store.equals("골드") || store.equals("플러스") || store.equals("큐") || store.equals("Q") || store.equals("티")) {
                    if (temp.equals("훼스탈")) {
                        System.out.println(temp + store);
                        medicine = temp+store+"정";
                        BackEndAndDetail();
                        break;
                    }
                    if (temp.equals("판피린")) {
                        System.out.println(temp + store);
                        if((temp+store).equals("판피린티")) {
                            medicine = "판피린티정";
                        }
                        if((temp+store).equals("판피린큐") || (temp+store).equals("판피린Q")) {
                            medicine = "판피린건조시럽";
                        }
                        BackEndAndDetail();
                        break;
                    }
                }

                if (NameExtract(label.getDescription().toString()) == true){
                    System.out.println(temp + store);
//                    medicine = temp + store;
                    if ((temp+store).equals("타이레놀")) {
                        medicine = "타이레놀8시간이알서방정";
                    }
                    if ((temp+store).equals("케토톱")) {
                        medicine = "케토톱플라스타";
                    }
                    if ((temp+store).equals("인사돌")) {
                        medicine = "인사돌정";
                    }
                    if ((temp+store).equals("임팩타민")) {
                        medicine = "임팩타민정";
                    }
                    if ((temp+store).equals("활명수")) {
                        medicine = "활명수";
                    }
                    if ((temp+store).equals("이모튼")) {
                        medicine = "이모튼캡슐";
                    }
                    if ((temp+store).equals("판콜")) {
                        medicine = "판콜에이내복액";
                    }
                    if ((temp+store).equals("베아제")) {
                        medicine = "닥터베아제정";
                    }

                    BackEndAndDetail();
                    break;
                }
            }

            //불일치 경우
            if (repeatCount == labels.size()) {
                picError();
            }

        } else {
            picError();
            message.append("nothing");
        }

        return message.toString();
    }

    private static boolean NameExtract(String name){
        String[] medicine = {"타이레놀", "케토톱", "인사돌", "임팩타민", "활명수", "판피린", "이모튼",
                "판콜", "베아제"};

        boolean correct = false;

        for (int i = 0; i < medicine.length; i++) {
            if (name.equals(medicine[i])) {
                if (name.equals("훼스탈")) {
                    temp = "훼스탈";
                }
                if (name.equals("판피린")) {
                    temp = "판피린";
                }
                else {
                    correct = true;
                }
                break;
            }
        }
        return correct;
    }

    private static int picError() {
        picErrCount++;
        System.out.println("에러카운트 " + picErrCount);
        return picErrCount;
    }

    public static void Vibrate() {
        if (picErrCount >= 1) {
            vb = true;
        }
    }

//    static boolean getInfo = false;

    public static void BackEndAndDetail() {
        retrofitAPI.getMedicine(medicine).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post data = response.body();

                    System.out.println(data.toString());

                    List<String> e_list = Arrays.asList(data.getEfficacy().toString().split(","));
                    System.out.println("길이 " + e_list.size());
                    String[] eArrays = e_list.toArray(new String[e_list.size()]);

                    System.out.println("확인확인 " + eArrays[4]);

                    List<String> i_list = Arrays.asList(data.getInformation().toString().split(","));
                    String[] iArrays = i_list.toArray(new String[i_list.size()]);

//                    System.out.println("확인 " + e_list.get(0));

                    saveInfo(data.getSubName(), data.getshapeUrl(), data.getDescription(),
                            data.getdosage(), data.getstorage(), eArrays, iArrays);

                    medicine = "";

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)MainActivity.context).DetailPage();
                        }
                    }, 0);
                }
                else {
                    try {
                        String body = response.errorBody().string();
                        Log.e(TAG, " <2> error - body : " + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                System.out.println("실패");
            }
        });
    }

    static void saveInfo(String name, String url, String des, String use, String store, String[] eff, String[] info) {
        subName = name;
        shapeUrl = url;
        description = des;
        dosage = use;
        storage = store;
        efficacy = eff;
        information = info;
//        System.out.println("어떻게" + efficacy);
        //efficacy, information 처리
    }
}