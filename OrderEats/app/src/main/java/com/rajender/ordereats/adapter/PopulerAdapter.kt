package com.rajender.ordereats.adapter

import android.view.ViewGroup

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.rajender.ordereats.databinding.PopulerItemBinding

class PopulerAdapter(private val items:List<String>, private val price: List<String>, private val image:List<Int>) : RecyclerView.Adapter<PopulerAdapter.PopulerViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopulerAdapter.PopulerViewHolder {
       return PopulerViewHolder(PopulerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopulerAdapter.PopulerViewHolder, position: Int) {
        val item = items[position]
        val price = price[position]
        val images = image[position]
        holder.bind(item, price, images )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PopulerViewHolder (private val binding : PopulerItemBinding): RecyclerView.ViewHolder(binding.root){
        private val imagesView = binding.imageView6
        fun bind(item: String, price: String, images: Int) {
            binding.foodNamePopuler.text = item
            binding.PricePopuler.text = price.toString()
            imagesView.setImageResource(images)
        }

    }
}