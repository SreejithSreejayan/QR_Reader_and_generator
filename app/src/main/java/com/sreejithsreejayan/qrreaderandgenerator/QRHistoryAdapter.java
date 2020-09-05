package com.sreejithsreejayan.qrreaderandgenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QRHistoryAdapter extends RecyclerView.Adapter<QRHistoryAdapter.ViewHolder> {

    private ArrayList<QRHistory> mHistories;

//    public HistoryAdapter(List<QRHistory> histories) {
//        mHistories = histories;
//    }

    public QRHistoryAdapter(ArrayList<QRHistory> histories) {
        mHistories = histories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView QRData,Date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            QRData= (TextView) itemView.findViewById(R.id.QRScanHistoryData);
            Date= (TextView) itemView.findViewById(R.id.QRScanHistoryDate);
        }
    }

    @NonNull
    @Override
    public QRHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View ScanHistory = inflater.inflate(R.layout.item_history,parent,false);
        ViewHolder viewHolder= new ViewHolder(ScanHistory);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QRHistoryAdapter.ViewHolder holder, int position) {
        QRHistory qrHistory=mHistories.get(position);

        TextView textView=holder.QRData;
        textView.setText(qrHistory.getQREncodedData());
        TextView dateView=holder.QRData;
        dateView.setText(String.format("%d",qrHistory.getDate()));
    }

    @Override
    public int getItemCount() {
        return mHistories.size();
    }
}
