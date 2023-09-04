package com.example.attendancesystem;

public class Electiondata
{
    public String Title, email, Total, Attendance, userID, Status, host;

    public Electiondata(String title, String email, String total, String attendance, String userID, String status, String host)
    {
        this.Title = title;
        this.email = email;
        this.Total = total;
        this.Attendance = attendance;
        this.userID = userID;
        this.Status = status;
        this.host = host;
    }

    public void setAttendance(String attendance)
    {
        Attendance = attendance;
    }

}
