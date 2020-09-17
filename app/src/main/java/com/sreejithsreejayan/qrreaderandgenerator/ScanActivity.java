package com.sreejithsreejayan.qrreaderandgenerator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ScanActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    Vibrator vibe;
    private boolean isFlashOn=false;
    private DatabaseHelper databaseHelper;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date;
    String[] permission =new String[] {Manifest.permission.CAMERA};
    int[] grantResult=new int[]{PERMISSION_GRANTED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        initView();

        if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            onRequestPermissionsResult(REQUEST_CAMERA_PERMISSION,permission,grantResult);{
                if (grantResult[0]== PERMISSION_GRANTED){
                    recreate();
                }else {
                    Toast.makeText(ScanActivity.this,"Camera permission is needed to scan barcode",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }

    }

    private void initView() {
        surfaceView = findViewById(R.id.surfaceView);
        vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        databaseHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(600, 600)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                            Toast.makeText(ScanActivity.this,"Camera permission id needed for scanning barcode",Toast.LENGTH_LONG).show();
                        }
                        ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    if (intentData==""){
                        surfaceView.post(new Runnable() {
                            @Override
                            public void run() {
                                intentData = barcodes.valueAt(0).displayValue;
                                startResultActivity();
                            }
                        });
                    }
                }
            }
        });
    }

    private void startResultActivity() {
        vibe.vibrate(100);
        simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY",Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        date=simpleDateFormat.format(calendar.getTime());
        try{
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            ContentValues contentValues= new ContentValues();
            contentValues.put("qr_encoded_data", intentData);
            contentValues.put("date_and_time",date);
            database.insert("scannerhistory",null,contentValues);
            database.close();

        }catch (SQLException e){
            e.printStackTrace();
        }


        Intent result = new Intent(ScanActivity.this, ResultActivity.class);
        result.putExtra("resultString", intentData);
        result.putExtra("isFromHistory",false);
        startActivity(result);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_scan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.torchAction:
                try {
                    setFlash();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Toast.makeText(ScanActivity.this, "someting went wrong", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    public static Camera getCamera(CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }

                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return null;
    }

    public void setFlash() throws IOException {
        getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        cameraSource.start(surfaceView.getHolder());
        Camera _cam = getCamera(cameraSource);
        if (_cam != null) {
            if (isFlashOn){
                Camera.Parameters _pareMeters = _cam.getParameters();
                _pareMeters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                isFlashOn=!isFlashOn;
                _cam.setParameters(_pareMeters);
                _cam.startPreview();
            }else {
                Camera.Parameters _pareMeters = _cam.getParameters();
                _pareMeters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                isFlashOn=!isFlashOn;
                _cam.setParameters(_pareMeters);
                _cam.startPreview();
            }
        }

    }
}