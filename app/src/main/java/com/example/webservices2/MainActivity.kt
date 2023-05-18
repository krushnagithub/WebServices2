package com.example.webservices2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {
    private lateinit var fragments:UsersFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

    }
    private fun initView(){
        fragments=supportFragmentManager.findFragmentById(R.id.fragments)as UsersFragment
    }

}