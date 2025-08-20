package com.rajender.adminordereats.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.databinding.PendingOrderItemBinding

class PendingOrderAdapter(
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
        // Here, if 'isAccepted' was part of your data model, you would pass
        // the status for the item at 'position' to the holder.bind() method
        // or set it on the holder directly before calling bind.
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        // This assumes all lists are of the same size.
        // For safety, you could use minOf(customerNames.size, quantities.size, foodImages.size)
        // but it's better to ensure data integrity before it reaches the adapter.
        return customerNames.size
    }

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // IMPORTANT: 'isAccepted' is local to this ViewHolder instance.
        // This can lead to incorrect UI states when ViewHolders are recycled if the
        // state isn't reset based on the new item's data in onBindViewHolder/bind.
        // For robust state management, this boolean should reflect a property of the data item.
        private var isAccepted = false

        fun bind(position: Int) {
            // Reset isAccepted state if it were based on recycled ViewHolder.
            // However, with the current model, this line has limited effect unless you explicitly
            // manage and reset it in onBindViewHolder before calling bind.
            // If 'isAccepted' came from the data item, this local 'isAccepted' would be set from that.
            // For now, we rely on its default 'false' for new/recycled ViewHolders unless
            // the same ViewHolder instance happens to be reused for an item previously accepted by it.

            binding.apply {
                // Defensive check for list bounds
                if (position >= 0 && position < customerNames.size &&
                    position < quantities.size && position < foodImages.size
                ) {
                    orderCustomerName.text = customerNames[position]
                    orderQuantity.text = quantities[position]
                    orderFoodImage.setImageResource(foodImages[position])
                    root.visibility = View.VISIBLE // Ensure item is visible if previously hidden
                } else {
                    // Data is inconsistent or position is invalid
                    Log.e(
                        "PendingOrderAdapter",
                        "Invalid position ($position) or data list size mismatch. Hiding item."
                    )
                    root.visibility = View.GONE // Hide the item
                    return@apply // Exit apply block
                }

                // --- Button State and Click Listener ---

                // If 'isAccepted' state was part of your data model for the item at 'position',
                // you would set the local 'this.isAccepted' based on that data item here.
                // e.g., this.isAccepted = dataModel.orders[position].isAccepted

                if (!this@PendingOrderViewHolder.isAccepted) { // Explicitly refer to the ViewHolder's isAccepted
                    orderedAcceptButton.text = "Accept"
                } else {
                    orderedAcceptButton.text = "Dispatch"
                }

                orderedAcceptButton.setOnClickListener {
                    val currentAdapterPosition = adapterPosition
                    if (currentAdapterPosition != RecyclerView.NO_POSITION) {
                        // Further check if the position is still valid for the lists
                        if (currentAdapterPosition < customerNames.size) {
                            if (!this@PendingOrderViewHolder.isAccepted) {
                                // Action: Accept
                                this@PendingOrderViewHolder.isAccepted = true // Update local state
                                orderedAcceptButton.text = "Dispatch" // Update button text immediately

                                // TODO: If 'isAccepted' was part of your data model, update it here:
                                //  dataModel.orders[currentAdapterPosition].isAccepted = true
                                //  And then potentially call notifyItemChanged(currentAdapterPosition)
                                //  if other UI elements depend on this change beyond the button text.

                                showToast("Order Accepted for: ${customerNames[currentAdapterPosition]}")
                            } else {
                                // Action: Dispatch (and remove)
                                val name = customerNames.removeAt(currentAdapterPosition)
                                quantities.removeAt(currentAdapterPosition)
                                foodImages.removeAt(currentAdapterPosition)

                                notifyItemRemoved(currentAdapterPosition)
                                // Optional: If you want to ensure subsequent items re-bind positions correctly
                                // notifyItemRangeChanged(currentAdapterPosition, customerNames.size - currentAdapterPosition)

                                showToast("Order Dispatched for: $name")

                                // After removal, this ViewHolder might be recycled.
                                // The local 'isAccepted' state will be reset if it's rebound to a new item
                                // or if a new ViewHolder is created.
                            }
                        } else {
                            Log.w("PendingOrderAdapter", "Attempted action on a stale position: $currentAdapterPosition after list modification.")
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
