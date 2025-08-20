package com.rajender.ordereats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rajender.ordereats.databinding.CartItemBinding

class CartAdapter(
    private val cartItems: MutableList<String>, // Renamed for clarity (Kotlin convention)
    private val cartItemPrices: MutableList<String>, // Renamed
    private val cartImages: MutableList<Int>      // Renamed
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // itemQuantities should also be a MutableList to allow removal
    private val itemQuantities: MutableList<Int> = MutableList(cartItems.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size // Use the size of the primary data list

    inner class CartViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            // Defensive check, though generally position should be valid
            if (position < 0 || position >= cartItems.size || position >= itemQuantities.size) {
                // Log an error or handle this gracefully, this indicates a state inconsistency
                // This might happen if notifications are not handled perfectly during rapid changes.
                return
            }

            binding.apply {
                val quantity = itemQuantities[position]
                cartFoodName.text = cartItems[position]
                cartItemPrice.text = cartItemPrices[position]
                cartImage.setImageResource(cartImages[position])
                cartItemQuantity.text = quantity.toString()

                minusButton.setOnClickListener {
                    // Use adapterPosition for actions triggered by clicks,
                    // as the layout position might be stale.
                    val currentPosition = adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        decreaseQuantity(currentPosition)
                    }
                }
                plusButton.setOnClickListener {
                    val currentPosition = adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        increaseQuantity(currentPosition)
                    }
                }
                deleteButton.setOnClickListener {
                    val currentPosition = adapterPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        deleteItem(currentPosition)
                        // Toast can stay here or be moved to deleteItem if you prefer
                    }
                }
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (position < 0 || position >= itemQuantities.size) return // Bounds check

            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.cartItemQuantity.text = itemQuantities[position].toString()
                // You might want to notify a listener here if total price needs to update
            } else {
                Toast.makeText(binding.root.context, "Minimum Quantity Reached", Toast.LENGTH_SHORT).show()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (position < 0 || position >= itemQuantities.size) return // Bounds check

            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.cartItemQuantity.text = itemQuantities[position].toString()
                // You might want to notify a listener here if total price needs to update
            } else {
                Toast.makeText(binding.root.context, "Maximum Quantity Reached", Toast.LENGTH_SHORT).show()
            }
        }

        private fun deleteItem(position: Int) {
            if (position < 0 || position >= cartItems.size) { // Check against the primary list size
                return // Invalid position, possibly due to rapid clicks or adapter state issues
            }

            cartItems.removeAt(position)
            cartItemPrices.removeAt(position)
            cartImages.removeAt(position)
            itemQuantities.removeAt(position) // **IMPORTANT: Remove from itemQuantities**

            notifyItemRemoved(position)
            // After removing an item, the items from 'position' to 'itemCount - 1'
            // might need to be rebound if their indices change relative to the data.
            // This ensures that any subsequent views are correctly bound to the new data.
            notifyItemRangeChanged(position, cartItems.size - position)

            Toast.makeText(binding.root.context, "Item Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}
