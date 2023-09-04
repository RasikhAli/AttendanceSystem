package com.example.attendancesystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyDelAnyAdapter extends RecyclerView.Adapter<MyDelAnyAdapter.MyDelAnyViewHolder>
{
    Context context;
    ArrayList<PreviousAttendanceData> list;

    public MyDelAnyAdapter(Context context, ArrayList<PreviousAttendanceData> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyDelAnyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.del_any_attendance,parent,false);

        return new MyDelAnyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDelAnyViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        PreviousAttendanceData previousAttendanceData = list.get(position);
        holder.title.setText(previousAttendanceData.getTitle());
        holder.str.setText("Total Strength: " + previousAttendanceData.getTotal());
        holder.pre.setText("Present: " + previousAttendanceData.getAttendance());
        holder.status.setText("STATUS: " + previousAttendanceData.getStatus());

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviousAttendanceData previous = list.get(position);

                //Delete
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AttendanceData");
                String name = previous.getHost();

                ref.child(name).removeValue();
                list.remove(list.get(position));
                Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class MyDelAnyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView str;
        TextView pre;
        TextView status;
        Button   del;

        public MyDelAnyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title  = (TextView) itemView.findViewById(R.id.title_attendance);
            str = (TextView) itemView.findViewById(R.id.pres);
            pre = (TextView) itemView.findViewById(R.id.abse);
            status = (TextView) itemView.findViewById(R.id.status);
            del = (Button)   itemView.findViewById(R.id.delete);

        }
    }
}
