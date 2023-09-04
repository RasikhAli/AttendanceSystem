package com.example.attendancesystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrAdapter extends RecyclerView.Adapter<CurrAdapter.myViewHolder>
{
    Context context;
    ArrayList<CurrAttendanceData> list;

    public CurrAdapter(Context context, ArrayList<CurrAttendanceData> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.curr_attendance,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        CurrAttendanceData currElectionData = list.get(position);
        holder.title.setText(currElectionData.getTitle());
        holder.present.setText("Present : " + currElectionData.getAttendance());
        holder.absent.setText("Total Students : " + currElectionData.getTotal());

        holder.linear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                CurrElectionData curr = list.get(position);
                Intent intent = new Intent(context, Election_details.class);
                intent.putExtra("sendTitle", currElectionData.getTitle());
                intent.putExtra("sendName", currElectionData.getName());
                intent.putExtra("sendTotal", currElectionData.getTotal());
                intent.putExtra("sendAttendance", currElectionData.getAttendance());
                intent.putExtra("sendHost", currElectionData.getHost());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, present, absent;
        LinearLayout linear;

        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title= (TextView)  itemView.findViewById(R.id.title_attendance);
            present = (TextView)  itemView.findViewById(R.id.pres);
            absent = (TextView)  itemView.findViewById(R.id.abse);
            linear = (LinearLayout) itemView.findViewById(R.id.linear_id);
        }
    }
}

