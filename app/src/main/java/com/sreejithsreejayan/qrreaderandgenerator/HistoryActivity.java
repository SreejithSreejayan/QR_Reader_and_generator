package com.sreejithsreejayan.qrreaderandgenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<QRHistory>histories;
    RecyclerView recyclerView;
    private DatabaseHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        histories=new ArrayList<>();

        recyclerView=findViewById(R.id.rvScanHistory);

        DBHelper = new DatabaseHelper(this);

        try {
            SQLiteDatabase database = DBHelper.getReadableDatabase();
            Cursor cursor = database.query("scannerhistory",null,null,null,null,null,null);

            if (cursor!=null){
                if (cursor.moveToFirst()){
                    for (int i=0;i<cursor.getCount();i++){
                        QRHistory s = new QRHistory();
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String qrEncodedData =cursor.getString(cursor.getColumnIndex("qr_encoded_data"));
                        String dateAndTime =cursor.getString(cursor.getColumnIndex("date_and_time"));
                        s.setId(id);
                        s.setQREncodedData(qrEncodedData);
                        s.setDate(dateAndTime);
                        histories.add(s);
                        cursor.moveToNext();
                    }
                    cursor.close();
                    database.close();
                }else {
                    cursor.close();
                    database.close();
                }
            }else {
                database.close();
                Toast.makeText(this,"there is no history",Toast.LENGTH_LONG).show();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        QRHistoryAdapter adapter = new QRHistoryAdapter(histories);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}