package com.example.attendancesystem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddNewAttendance extends AppCompatActivity
{
    ImageView back;
    EditText  title, email, total;
    Button    submit;
    RadioButton finish, start;

    String Tit;
    String Att;
    String usID;
    String Stat;
    String em;
    String nm;

    FirebaseAuth firebaseAuth;

    private FirebaseUser mUser;
    private DatabaseReference reference;
    private String uID;

    String tempName  = "";
    String attendance = "0";
    String status = "On-Going";
    int incrementElection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_attendance);

        firebaseAuth = FirebaseAuth.getInstance();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uID = mUser.getUid();

        start  = (RadioButton) findViewById(R.id.ongoin);
        finish = (RadioButton) findViewById(R.id.conc);
        title  = (EditText)  findViewById(R.id.title_for_attendance);
        email = (EditText)  findViewById(R.id.email);
        total = (EditText)  findViewById(R.id.total);
        submit = (Button)    findViewById(R.id.submitbtn);

        start.setVisibility(View.GONE);
        finish.setVisibility(View.GONE);

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish.setChecked(false);
                status = "On-Going";
            }
        });
        finish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                start.setChecked(false);
                status = "Concluded";
            }
        });

        Bundle intent = getIntent().getExtras();
        if(intent != null)
        {
            Tit = intent.getString("Tit");
            em = intent.getString("em");
            Att = intent.getString("tot");
            usID = intent.getString("uID");
            Stat = intent.getString("Stat");

            title.setText(Tit);
            email.setText(em);
            total.setText(Att);
            status = Stat;
            start.setVisibility(View.VISIBLE);
            finish.setVisibility(View.VISIBLE);

            submit.setText("Update Changes");
        }

        setValueElectionIncrement();

        back   = (ImageView) findViewById(R.id.go_back);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AddNewAttendance.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean check = false;

                if(title.getText().toString().trim().isEmpty())
                {
                    check = true;
                    title.setError("Please Enter a Title");
                }
                if(email.getText().toString().trim().isEmpty())
                {
                    check = true;
                    email.setError("Please Enter Email...");
                }
                if(total.getText().toString().trim().isEmpty())
                {
                    check = true;
                    total.setError("Please Enter Strength...");
                }

                if(check == false)
                {
                    String Title  = title.getText().toString().trim();
                    String ema = email.getText().toString().trim();
                    String userID = uID;
                    String tot = total.getText().toString().trim();

                    submitData(Title, ema, tot, attendance, userID);
                }
            }
        });
    }

    private void setValueElectionIncrement()
    {
        reference.child(uID).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Userdata userProfile = snapshot.getValue(Userdata.class);

                if(userProfile != null)
                {
                    tempName = userProfile.fullName;

                    if(tempName.substring(tempName.length() - 1).matches("[0-9]") == false)
                    {
                        String nameCopy = tempName + "_" + incrementElection;
                        incrementElection = Integer.parseInt(nameCopy.substring(nameCopy.length() - 1 ));
                    }
                    else
                    {
                        incrementElection = Integer.parseInt(tempName.substring(tempName.length() - 1 ));
                    }
                    incrementElection++;

                    tempName = tempName + "_" + incrementElection;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(AddNewAttendance.this, "Error: Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void submitData(String title, String email, String total,String attendance,
                            String userID)
    {
            setValueElectionIncrement();
        String host = tempName;
        Electiondata electiondata = new Electiondata(title, email, total, attendance, userID, status, host);

        FirebaseDatabase.getInstance().getReference("AttendanceData").child(host).child("title")
                .setValue(title);
//        FirebaseDatabase.getInstance().getReference("AttendanceData").child(host).child("name")
//                .setValue(name);
        FirebaseDatabase.getInstance().getReference("AttendanceData").child(host).child("email")
                .setValue(email);
        FirebaseDatabase.getInstance().getReference("AttendanceData").child(host).child("total")
                .setValue(total);
        FirebaseDatabase.getInstance().getReference("AttendanceData").child(host).child("attendance")
                .setValue(attendance);
        FirebaseDatabase.getInstance().getReference("AttendanceData").child(host).child("userID")
                .setValue(userID);
        FirebaseDatabase.getInstance().getReference("AttendanceData").child(host).child("status")
                .setValue(status);
        FirebaseDatabase.getInstance().getReference("AttendanceData").child(host).child("host")
                .setValue(host);
    }
}