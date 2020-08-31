package com.sreejithsreejayan.qrreaderandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRGenerateActivity extends AppCompatActivity {

    ImageView qrView;
    EditText qrInput;
    Button generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_genereate);

        initView();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qrInput.length()!=0){
                    String text=qrInput.toString();
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qrView.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(QRGenerateActivity.this,"Enter some text",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void initView() {
        qrView=findViewById(R.id.QRView);
        qrInput=findViewById(R.id.editTxt);
        generate=findViewById(R.id.btnGenerate);
    }
}