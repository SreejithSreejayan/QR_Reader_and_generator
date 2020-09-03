package com.sreejithsreejayan.qrreaderandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    String intentData;
    Button another;
    TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();

        Intent result=getIntent();
        intentData=result.getExtras().getString("resultString");
        resultView.setText(intentData);



    }

    private void initView() {
        another=findViewById(R.id.btnAnotherScan);
        resultView=findViewById(R.id.resultTextView);
        another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanActivity = new Intent(ResultActivity.this, ScanActivity.class);
                startActivity(scanActivity);
            }

        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }
}