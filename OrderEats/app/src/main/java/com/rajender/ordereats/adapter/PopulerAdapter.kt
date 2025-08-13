package com.rajender.ordereats.adapter

import android.content.Context
import android.content.Intent
import android.provider.MediaStore.Images
import android.view.ViewGroup

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.rajender.ordereats.DetailsActivity
import com.rajender.ordereats.databinding.PopulerItemBinding

class PopulerAdapter(private val items:List<String>, private val price: List<String>, private val image:List<Int>, private val requireContext: Context) : RecyclerView.Adapter<PopulerAdapter.PopulerViewHolder>() {


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

        holder.itemView.setOnClickListener {
            // setOnClickListner to open details
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName", item)
            intent.putExtra("MenuItemImage", images
            )
            requireContext.startActivity(intent)
        }
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