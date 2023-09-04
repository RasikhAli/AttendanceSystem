package com.example.attendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Del_Any extends AppCompatActivity
{
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyDelAnyAdapter myDelAnyAdapter;
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
                Intent intent = new Intent(Del_Any.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("AttendanceData");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myDelAnyAdapter = new MyDelAnyAdapter(this,list);
        recyclerView.setAdapter(myDelAnyAdapter);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    PreviousAttendanceData previousAttendanceData = dataSnapshot.getValue(PreviousAttendanceData.class);
                    list.add(previousAttendanceData);
                }
                myDelAnyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}