package com.sreejithsreejayan.qrreaderandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button generateBtn,scanBtn,historyBtn;
    boolean exitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        generateBtn =findViewById(R.id.btnGen);
        scanBtn= findViewById(R.id.btnScn);
        historyBtn= findViewById(R.id.btnHistory);
        generateBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGen:
                startActivity(new Intent(MainActivity.this, QRGenerateActivity.class));
                break;
            case R.id.btnScn:
                startActivity(new Intent(MainActivity.this,ScanActivity.class));
                break;
            case R.id.btnHistory:
                startActivity(new Intent(MainActivity.this,HistoryActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (exitPressedOnce){
            finish();
        }
        this.exitPressedOnce=true;
        Toast.makeText(MainActivity.this,"press again to exit",Toast.LENGTH_SHORT).show();

        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            public void run() {
                exitPressedOnce=false;
            }
        };
        worker.schedule(runnable, 2, TimeUnit.SECONDS);
    }
}