package com.example.android.politicalpreparedness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // onCreate() method is called when activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {

        // Call super class onCreate to complete the creation of activity like the view hierarchy
        super.onCreate(savedInstanceState)

        // Set the activity content to @res/layout/activity_main.xml
        setContentView(R.layout.activity_main)

    }

}
