package com.example.attendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class Election_details extends AppCompatActivity
{
    ImageView goBack;
    TextView tit, name, roll, attend, abb;
    Button btn;

    String Tit, tot, n,at,hos;
    Integer getValue2, getValue1, absent;
    boolean voted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);

        tit    = (TextView) findViewById(R.id.title_attendance);
        name  = (TextView) findViewById(R.id.Name);
        abb  = (TextView) findViewById(R.id.abse);
        attend  = (TextView) findViewById(R.id.pre);
        btn   = (Button) findViewById(R.id.votesBtn);

        Bundle intent = getIntent().getExtras();
        if(intent != null)
        {
            Tit = intent.getString("sendTitle");
            n = intent.getString("sendName");
            tot = intent.getString("sendTotal");
            at = intent.getString("sendAttendance");
            hos = intent.getString("sendHost");

            int present = Integer.parseInt(at);
            int total = Integer.parseInt(tot);
            absent = total - present;

            tit.setText(Tit);
            name.setText(hos);
            abb.setText("Absent: " + absent.toString());
            attend.setText("Present: " + at);
            btn.setText("Mark Attendance");
        }

        getValue1 = Integer.parseInt(at);
        getValue2 = Integer.parseInt(tot);

        goBack = (ImageView) findViewById(R.id.go_back);
        goBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Election_details.this, CurrentElections.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(voted == true)
                {
                    Toast.makeText(Election_details.this, "Sorry, can't have multiple Attendance...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    voted = true;
                    getValue1 = getValue1 + 1;

                    if(absent <= 0)
                    {
                        Toast.makeText(Election_details.this, "Can't Mark Attendance", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        attend.setText("Present: " + getValue1.toString());
                        abb.setText("Absent: " + absent);

                        String temp = hos;

                        FirebaseDatabase.getInstance().getReference("AttendanceData")
                                .child(temp).child("attendance")
                                .setValue(getValue1.toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(Election_details.this,"Successfully Entered...",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(Election_details.this,"Error in Attendance...",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}