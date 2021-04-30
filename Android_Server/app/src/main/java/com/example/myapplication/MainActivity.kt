package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val server = RspServer(context = applicationContext)
        server.start()
        server.blockUntilShutdown()
    }

}