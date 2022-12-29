package com.example.android.politicalpreparedness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.android.politicalpreparedness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // onCreate() method is called when activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {

        // Call super class onCreate to complete the creation of activity like the view hierarchy
        super.onCreate(savedInstanceState)

        // Inflate Layout: @layout/activity_main.xml
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Activity
        binding.lifecycleOwner = this

    }

}
