package com.rajender.adminordereats.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.databinding.PendingOrderItemBinding
// import kotlin.contracts.contract // This import seems unused, can be removed

class PendingOrderAdapter(
    // It's a common Kotlin convention to use camelCase for property names
    private val customerNames: ArrayList<String>,
    private val quantities: ArrayList<String>,
    private val foodImages: ArrayList<Int>,
    private val context: Context
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    // getItemCount should reflect the size of your primary data list
    override fun getItemCount(): Int = customerNames.size

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // isAccepted state should ideally be managed per item,
        // or the adapter should be notified of changes to rebind correctly if items are recycled.
        // For simplicity in this example, we'll keep it as is, but be aware of ViewHolder recycling.
        private var isAccepted = false

        fun bind(position: Int) {
            binding.apply {
                // Check if position is valid to prevent IndexOutOfBoundsException
                // This is important if items are removed asynchronously or if lists get out of sync
                if (position < customerNames.size && position < quantities.size && position < foodImages.size) {
                    orderCustomerName.text = customerNames[position]
                    orderQuantity.text = quantities[position]
                    orderFoodImage.setImageResource(foodImages[position])
                } else {
                    // Handle the case where the position is out of bounds,
                    // e.g., by hiding the view or showing an error state.
                    // This can happen if notifyDataSetChanged() is called without proper list updates.
                    // For now, let's log an error or clear the views
                    root.visibility = android.view.View.GONE // Hide the item if data is inconsistent
                    return@apply // Exit apply block
                }


                orderedAcceptButton.apply {
                    if (!isAccepted) {
                        text = "Accept" // Consistent naming for button state
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        val currentPosition = adapterPosition
                        // Ensure the position is still valid before proceeding
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            if (!isAccepted) {
                                text = "Dispatch"
                                isAccepted = true
                                showToast("Order Accepted")
                            } else {
                                // --- THIS IS THE CORRECTED PART ---
                                if (currentPosition < customerNames.size) {
                                    customerNames.removeAt(currentPosition)
                                    quantities.removeAt(currentPosition)
                                    foodImages.removeAt(currentPosition)
                                    notifyItemRemoved(currentPosition)
                                    showToast("Order Dispatched")
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}

