package com.example.saveit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val action = intent.action
        val type = intent.type

        if(Intent.ACTION_SEND == action && type != null){
            if("text/plain" == type){
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                if(sharedText != null){
                    Toast.makeText(this, sharedText, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}