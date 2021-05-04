package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemConsoleBinding

class ConsoleAdapter: RecyclerView.Adapter<ConsoleAdapter.ConsoleViewHolder>() {
    private val consoleList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsoleViewHolder {
        val binding = ItemConsoleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsoleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return consoleList.size
    }

    override fun onBindViewHolder(holder: ConsoleViewHolder, position: Int) {
        holder.bindViewHolder(consoleList[position])
    }

    fun addLog(log: String) {
        consoleList.add(log)
        notifyDataSetChanged()
    }

    inner class ConsoleViewHolder(private val binding: ItemConsoleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bindViewHolder(log: String) {
            binding.tvLog.text = log
        }
    }
}