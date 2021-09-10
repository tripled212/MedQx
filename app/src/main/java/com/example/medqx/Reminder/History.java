package com.example.medqx.Reminder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.medqx.Locator.MedModel;
import com.example.medqx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class History extends RecyclerView.Adapter<History.ViewHolder> {

    List<MedModel> medModel;
    List<String> mHistory;
    Context ct;
    DatabaseReference postRef;
    String selectedPostId;
    FirebaseAuth mAuth;
    RecyclerView rvHistory;

    public History(List<MedModel> medModel, Context ct) {
        this.medModel = medModel;
        this.ct = ct;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_string, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull History.ViewHolder holder, int position) {

        postRef = FirebaseDatabase.getInstance().getReference("Storage");
        mAuth = FirebaseAuth.getInstance();
        mAuth.getInstance().getCurrentUser();
        String currentUserId = mAuth.getUid();

        final String postKey = postRef.getKey();

        //Toast.makeText(ct, ""+postedKy, Toast.LENGTH_SHORT).show();

        MedModel medModel1 = medModel.get(position);
        holder.tv_name.setText(medModel1.getUname());
        holder.tv_medInteraval.setText(medModel1.getInterval());
        holder.tv_medNum.setText(medModel1.getNum());
        holder.tv_medType.setText(medModel1.getMedType());
        holder.tv_time.setText(medModel1.getHrsMin());
    }

    @Override
    public int getItemCount() {
        return medModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_time, tv_medNum, tv_medType, tv_medInteraval;
        RecyclerView rvHistory;

        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_medNum = (TextView)view.findViewById(R.id.tv_medNum);
            tv_medType = (TextView)view.findViewById(R.id.tv_medType);
            tv_medInteraval = (TextView)view.findViewById(R.id.tv_medInteraval);
            rvHistory = (RecyclerView)view.findViewById(R.id.rvHistory);

        }
    }

}