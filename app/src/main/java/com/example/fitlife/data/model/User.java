package com.example.fitlife.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fullName;
    public String username;
    public String password;

    // Getter for fullName
    public String getFullName() {
        return fullName;
    }
}
