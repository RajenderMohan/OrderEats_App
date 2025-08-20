package com.rajender.adminordereats.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.databinding.ItemItemBinding

class AddItemAdapter(
    private val menuItemName: MutableList<String>,
    private val menuItemPrice: MutableList<String>,
    private val menuItemImage: MutableList<Int>
) : RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder>() {

    private val itemQuantities: MutableList<Int> = MutableList(menuItemName.size) { 1 }
    private lateinit var context: Context // Variable to hold context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        context = parent.context // Get context from parent
        val binding = ItemItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItemName.size

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            if (position < itemQuantities.size) {
                val quantity = itemQuantities[position]
                binding.quantityTextView.text = quantity.toString()
            } else {
                binding.quantityTextView.text = "1"
            }

            binding.apply {
                // Ensure position is valid for all lists before accessing
                if (position < menuItemName.size && position < menuItemPrice.size && position < menuItemImage.size) {
                    foodNameTextView.text = menuItemName[position]
                    priceTextView.text = menuItemPrice[position]
                    foodImageView.setImageResource(menuItemImage[position])
                } else {
                    // Handle case where item might have been removed rapidly
                    // Clear fields or show placeholder
                    foodNameTextView.text = "N/A"
                    priceTextView.text = "N/A"
                    // foodImageView.setImageResource(R.drawable.placeholder_image) // Example
                }


                minusButton.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        decreaseQuantity(adapterPosition)
                    }
                }
                plusButton.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        increaseQuantity(adapterPosition)
                    }
                }
                deleteButton.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        // Pass the item name to deleteItem for the Toast message
                        val itemName = if (adapterPosition < menuItemName.size) menuItemName[adapterPosition] else "Item"
                        deleteItem(adapterPosition, itemName)
                    }
                }
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (position < itemQuantities.size && itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.quantityTextView.text = itemQuantities[position].toString()
                // You can add a Toast here if you want feedback for every quantity change
                // Toast.makeText(context, "Quantity decreased", Toast.LENGTH_SHORT).show()
            } else if (position < itemQuantities.size && itemQuantities[position] == 1) {
                Toast.makeText(context, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (position < itemQuantities.size && itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.quantityTextView.text = itemQuantities[position].toString()
                // You can add a Toast here if you want feedback for every quantity change
                // Toast.makeText(context, "Quantity increased", Toast.LENGTH_SHORT).show()
            } else if (position < itemQuantities.size && itemQuantities[position] >= 10) {
                Toast.makeText(context, "Quantity cannot be more than 10", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteItem(position: Int, itemName: String) { // Added itemName parameter
        if (position < menuItemName.size) {
            menuItemName.removeAt(position)
            menuItemPrice.removeAt(position)
            menuItemImage.removeAt(position)
            itemQuantities.removeAt(position)

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuItemName.size - position)

            Toast.makeText(context, "$itemName deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun addItem(name: String, price: String, imageResId: Int, quantity: Int = 1) {
        menuItemName.add(name)
        menuItemPrice.add(price)
        menuItemImage.add(imageResId)
        itemQuantities.add(quantity)
        notifyItemInserted(menuItemName.size - 1)
        Toast.makeText(context, "$name added", Toast.LENGTH_SHORT).show()
    }

    fun submitList(names: List<String>, prices: List<String>, images: List<Int>) {
        menuItemName.clear()
        menuItemPrice.clear()
        menuItemImage.clear()
        itemQuantities.clear()

        menuItemName.addAll(names)
        menuItemPrice.addAll(prices)
        menuItemImage.addAll(images)
        itemQuantities.addAll(MutableList(names.size) { 1 })

        notifyDataSetChanged()
        // Toast.makeText(context, "List updated", Toast.LENGTH_SHORT).show() // Optional
    }
}
