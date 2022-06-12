package com.example.projectii.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectii.R;
import com.example.projectii.model.SessionModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class TabHistoryAdapter  extends FirestoreRecyclerAdapter<SessionModel, TabHistoryAdapter.myviewholder> {
    Context c;
    public TabHistoryAdapter(@NonNull FirestoreRecyclerOptions<SessionModel> options, Context context) {
        super(options);
        c = context;
    }

    @NonNull
    @Override
    public TabHistoryAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_tab_history, parent, false);
        return new TabHistoryAdapter.myviewholder(convertView);
    }


    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull SessionModel sessionModel) {
        holder.tv.setText(sessionModel.getStart_date());
        holder.tv1.setText(sessionModel.getEnd_date());
        holder.tv2.setText(sessionModel.getDuration());
        holder.tv3.setText(String.valueOf(sessionModel.getTotal_fees()));
        FirebaseFirestore.getInstance().collection("Learners").document(sessionModel.getLearnerID())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                holder.tv4.setText(task.getResult().getString("fullName"));
                holder.tv5.setText(task.getResult().getString("address"));
                holder.tv6.setText(task.getResult().getString("email"));
                holder.tv7.setText(task.getResult().getString("phone"));
            }
        });
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView tv, tv1, tv2,tv3,tv4,tv5,tv6,tv7;
        Button btn;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.start_date);
            tv1 = itemView.findViewById(R.id.end_date);
            tv2 = itemView.findViewById(R.id.duration);
            tv3 = itemView.findViewById(R.id.totalfees);
            tv4 = itemView.findViewById(R.id.learner_fullName);
            tv5 = itemView.findViewById(R.id.learner_address);
            tv6 = itemView.findViewById(R.id.learner_email);
            tv7 = itemView.findViewById(R.id.learner_phone);
//            btn = itemView.findViewById(R.id.checkStatus);
        }
    }
}
