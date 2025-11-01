package com.rajender.adminordereats.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.ItemDetailsActivity
import com.rajender.adminordereats.databinding.ItemItemBinding
import com.rajender.adminordereats.model.AllMenu

class MenuItemAdapter(
    private val context: Context,
    private val menuItems: ArrayList<AllMenu>
) : RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuItemViewHolder(private val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)
                }
            }
        }

        fun bind(item: AllMenu) {
            binding.apply {
                foodNameTextView.text = item.foodName
                priceTextView.text = item.foodPrice
                item.foodImage?.let { foodImageView.setImageURI(it) }
            }
        }

        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]
            val intent = Intent(context, ItemDetailsActivity::class.java).apply {
                putExtra("menuItem", menuItem)
            }
            context.startActivity(intent)
        }
    }
}
