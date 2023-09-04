package com.example.attendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckPreviousAttendance extends AppCompatActivity
{
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyPreviousAttendanceAdapter myPreviousAttendanceAdapter;
    ArrayList<PreviousAttendanceData> list;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_previous_attendance);

        back = (ImageView) findViewById(R.id.go_back);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CheckPreviousAttendance.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("AttendanceData");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myPreviousAttendanceAdapter = new MyPreviousAttendanceAdapter(this,list);
        recyclerView.setAdapter(myPreviousAttendanceAdapter);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    PreviousAttendanceData previousAttendanceData = dataSnapshot.getValue(PreviousAttendanceData.class);
                    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uID = mUser.getUid();

                    if(previousAttendanceData.getUserID().equals(uID))
                    {
                        list.add(previousAttendanceData);
                    }
                }
                myPreviousAttendanceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}