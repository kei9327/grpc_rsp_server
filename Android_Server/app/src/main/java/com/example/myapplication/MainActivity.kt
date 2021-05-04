package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding

    var thread:Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.tvServerStatus.text = "offline"

        binding.btnStart.setOnClickListener {
            startServer()
        }

        binding.btnStop.setOnClickListener {
            stopServer()
        }

        binding.rcConsole.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                reverseLayout = true
                stackFromEnd = true
            }
            adapter = ConsoleAdapter()
        }
    }

    private fun startServer() {
        thread = ServerThread()
        thread?.start()
    }

    private fun stopServer() {
        thread?.interrupt()
        binding.tvServerStatus.text = "Offline1"
    }


    inner class ServerThread : Thread() {
        var server:RspServer? = null

        override fun run() {
            server = RspServer(context = applicationContext, listener = object : RspServer.RspServerListener {
                @SuppressLint("SetTextI18n")
                override fun serverInfo(ip: String, status: String) {
                    runOnUiThread {
                        binding.tvServerStatus.text = "$status,  $ip"
                    }
                }

                override fun serverLog(log: String) {
                    runOnUiThread {
                        (binding.rcConsole.adapter as ConsoleAdapter).addLog(log)
                    }
                }

            })
            server?.start()

            while (!currentThread().isInterrupted) {
                try {
                    // do something
                    server?.blockUntilShutdown()
                } catch (ex: InterruptedException) {
                    this.interrupt()
                }
            }
        }

        override fun interrupt() {
            server?.stop()
            super.interrupt()
        }
    }
}