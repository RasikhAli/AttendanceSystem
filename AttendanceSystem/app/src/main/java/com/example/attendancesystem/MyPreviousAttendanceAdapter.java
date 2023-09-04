package com.example.attendancesystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyPreviousAttendanceAdapter extends RecyclerView.Adapter<MyPreviousAttendanceAdapter.MyPreviousElectionViewHolder>
{
    Context context;
    ArrayList<PreviousAttendanceData> list;
    String pos;

    private FirebaseUser mUser;
    private DatabaseReference reference;
    private String usID;

    public MyPreviousAttendanceAdapter(Context context, ArrayList<PreviousAttendanceData> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyPreviousElectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.prev_attendance,parent,false);

        return new MyPreviousElectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPreviousElectionViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        PreviousAttendanceData previousAttendanceData = list.get(position);
        holder.title.setText(previousAttendanceData.getTitle());
        holder.strength.setText("Total Strength: " + previousAttendanceData.getTotal());
        holder.att.setText("Attendance: " + previousAttendanceData.getAttendance());
        holder.status.setText("STATUS: " + previousAttendanceData.getStatus());

        holder.options_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviousAttendanceData previous = list.get(position);
                String Tit = previous.getTitle();
                String em = previous.getEmail();
                String Tot = previous.getTotal();
                String Att = previous.getAttendance();
                String uID = previous.getUserID();
                String Stat = previous.getStatus();
                pos = Integer.toString(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String[] options = {"Update", "Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if(i == 0)
                        {
                            //Update
                            Intent intent = new Intent(context, AddNewAttendance.class);
                            intent.putExtra("Tit", Tit);
                            intent.putExtra("em", em);
                            intent.putExtra("tot", Tot);
                            intent.putExtra("att",  Att);
                            intent.putExtra("uID", uID);
                            intent.putExtra("Stat", Stat);
                            context.startActivity(intent);
                        }
                        if(i == 1)
                        {
                            //Delete

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AttendanceData");
                            String name = previous.getHost();

                            ref.child(name).removeValue();
                            list.remove(list.get(position));
                            Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.create().show();
            }
        });

        holder.linear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                CurrElectionData curr = list.get(position);
                Intent intent = new Intent(context, Election_details.class);
                intent.putExtra("sendTitle", previousAttendanceData.getTitle());
//                intent.putExtra("sendRoll", previousAttendanceData.getRoll());
                intent.putExtra("sendTotal", previousAttendanceData.getTotal());
                intent.putExtra("sendAttendance", previousAttendanceData.getAttendance());
                intent.putExtra("sendHost", previousAttendanceData.getHost());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class MyPreviousElectionViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView strength;
        TextView att;
        TextView status;
        Button   options_Btn;
        LinearLayout linear;

        public MyPreviousElectionViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title  = (TextView) itemView.findViewById(R.id.title_attendance);
            strength = (TextView) itemView.findViewById(R.id.pres);
            att = (TextView) itemView.findViewById(R.id.abse);
            status = (TextView) itemView.findViewById(R.id.status);
            options_Btn= (Button)   itemView.findViewById(R.id.options);
            linear = (LinearLayout) itemView.findViewById(R.id.linearlayout);

        }
    }
}
