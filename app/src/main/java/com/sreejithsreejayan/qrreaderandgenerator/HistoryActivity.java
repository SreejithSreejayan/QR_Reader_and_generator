package com.sreejithsreejayan.qrreaderandgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

public class HistoryActivity extends AppCompatActivity implements QRHistoryAdapter.onItemClickListener {

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

        QRHistoryAdapter adapter = new QRHistoryAdapter(histories,this);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.activity_history_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteHistory:
                deleteHistoryDB();
                break;
            default:
                Toast.makeText(HistoryActivity.this,"Someting went wrong",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void deleteHistoryDB() {


        new AlertDialog.Builder(HistoryActivity.this).setTitle("Delete all history").setMessage("Are you sure you want to delete all the scan history").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SQLiteDatabase database=DBHelper.getWritableDatabase();
                database.delete("scannerhistory",null,null);
                database.close();
                Toast.makeText(HistoryActivity.this,"Scan history successfully deleted",Toast.LENGTH_SHORT).show();
                finish();

            }
        }).show();


    }

    @Override
    public void onItemClicked(int position) {
        String qrData= histories.get(position).getQREncodedData();
        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra("resultString",qrData);
        intent.putExtra("isFromHistory",true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }
}