package com.rajender.adminordereats.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.databinding.ItemItemBinding

class AddItemAdapter(private val MenuItemName : ArrayList<String>, private val MenuItemPrice : ArrayList<String>, private val MenuItemImage : ArrayList<Int>) : RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }



    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = MenuItemName.size

    inner class AddItemViewHolder (private val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
           binding.apply {
               foodNameTextView.text = MenuItemName[position]
               priceTextView.text = MenuItemPrice[position]
               foodImageView.setImageResource(MenuItemImage[position])
           }
        }

    }

}