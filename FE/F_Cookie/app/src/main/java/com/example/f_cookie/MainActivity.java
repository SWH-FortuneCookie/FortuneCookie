package com.example.f_cookie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder
//                    .setMessage("Choose a picture")
//                    .setPositiveButton("Gallery", (dialog, which) -> startGalleryChooser())
//                    .setNegativeButton("Camera", (dialog, which) -> startCamera());
//            builder.create().show();
//        });

        context = this;

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        divId = getDivId(MainActivity.this);
        System.out.println("디바이스 아이디 " + divId);
        //디바이스 아이디 전달

        getInfoBtn = findViewById(R.id.infoBtn);
        getManagBtn = findViewById(R.id.manageBtn);

        getInfoBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("사진을 촬영해주세요").setPositiveButton("카메라", (dialog, which) -> startCamera())
                    .setNegativeButton("테스트용", (dialog, which) -> startGalleryChooser());
            builder.create().show();
        });

        getManagBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ManageActivity.class);
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

//        // 뒤로가기 버튼
//        ImageButton backButton = findViewById(R.id.backbtn);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

//        // 사용상 주의사항 보러가기 버튼
//        android.widget.Button drugWarningsButton = findViewById(R.id.drug_warningsbtn);
//        drugWarningsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(MainActivity.this, DrugWarningsActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // 앱 시작 시 자동으로 팝업 액티비티 띄우기
//        showPopupActivity();
    }

//    private void showPopupActivity() {
//        Intent intent = new Intent(MainActivity.this, PopupActivity.class);
//        startActivity(intent);
//
//        // 팝업 액티비티의 배경을 투명하게 설정
//        getWindow().setBackgroundDrawableResource(R.drawable.rounded_popup_background);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    void checkErr(){
//        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//        vib.vibrate(VibrationEffect.createOneShot(1000, 100));
//
//        if (picErrCount == 1) {
//            reverseTxt.setVisibility(View.VISIBLE);
//            photoBtn.setVisibility(View.VISIBLE);
//        }
//        if (picErrCount == 2) {
//            againTxt.setVisibility(View.VISIBLE);
//            photoBtn.setVisibility(View.VISIBLE);
//        }
//
//        photoBtn.setOnClickListener(view -> {
//            startCamera();
//            againTxt.setVisibility(View.GONE);
//            reverseTxt.setVisibility(View.GONE);
//            photoBtn.setVisibility(View.GONE);
//        });
//    }

    public void DetailPage() {
        Intent intent = new Intent(this, MeDetailActivity.class);
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
                        //백엔드로 훼스탈골드 전달 & -> 의약품 상세 페이지
                        BackEndAndDetail();
                        break;
                    }
                    if (temp.equals("판피린")) {
                        System.out.println(temp + store);
                        //백엔드로 훼스탈골드 전달 & -> 의약품 상세 페이지
                        BackEndAndDetail();
                        break;
                    }
                }

                if (NameExtract(label.getDescription().toString()) == true){
                    System.out.println(temp + store);
                    //백엔드로 이름 전달 & -> 의약품 상세 페이지
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

    public static void BackEndAndDetail() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                MainActivity md = new MainActivity();
//                md.getClass();
//                md.DetailPage();
                ((MainActivity)MainActivity.context).DetailPage();
            }
        }, 0);
    }

}