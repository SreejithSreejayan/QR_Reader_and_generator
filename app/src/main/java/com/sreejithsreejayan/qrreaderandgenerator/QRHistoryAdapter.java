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
    private onItemClickListener mOnItemClickListener;


    public QRHistoryAdapter(ArrayList<QRHistory> histories,onItemClickListener onItemClickListener) {
        mOnItemClickListener=onItemClickListener;
        mHistories = histories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView QRData,Date;
        onItemClickListener onItemClickListener;


        public ViewHolder(@NonNull View itemView,onItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);

            this.onItemClickListener=onItemClickListener;
            QRData= (TextView) itemView.findViewById(R.id.QRScanHistoryData);
            Date= (TextView) itemView.findViewById(R.id.QRScanHistoryDate);
        }


        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public QRHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View ScanHistory = inflater.inflate(R.layout.item_history,parent,false);
        return new ViewHolder(ScanHistory,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QRHistoryAdapter.ViewHolder holder, int position) {
        QRHistory qrHistory=mHistories.get(position);

        TextView textView=holder.QRData;
        textView.setText(qrHistory.getQREncodedData());
        TextView dateView=holder.Date;
        dateView.setText(qrHistory.getDate());
    }

    @Override
    public int getItemCount() {
        return mHistories.size();
    }

    public interface onItemClickListener{
        void onItemClicked(int position);
    }
}
