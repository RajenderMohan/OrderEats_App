package com.rajender.adminordereats.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.R
import com.rajender.adminordereats.databinding.PendingOrderItemBinding

class PendingOrderAdapter(
    private val customerNames: ArrayList<String>,
    private val quantities: ArrayList<String>,
    private val foodImages: ArrayList<Int>,
    private val context: Context
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    private val acceptedStatus = ArrayList(List(customerNames.size) { false })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return customerNames.size
    }

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                if (position >= 0 && position < customerNames.size) {
                    orderCustomerName.text = customerNames[position]
                    orderQuantity.text = quantities[position]
                    orderFoodImage.setImageResource(foodImages[position])
                    root.visibility = View.VISIBLE
                } else {
                    Log.e("PendingOrderAdapter", "Invalid position ($position). Hiding item.")
                    root.visibility = View.GONE
                    return@apply
                }

                val isAccepted = acceptedStatus[position]

                if (isAccepted) {
                    acceptButton.setImageResource(R.drawable.ic_dispatch)
                    declineButton.visibility = View.GONE
                } else {
                    acceptButton.setImageResource(R.drawable.ic_accept)
                    declineButton.visibility = View.VISIBLE
                }

                acceptButton.setOnClickListener {
                    handleAcceptOrDispatch(adapterPosition)
                }

                declineButton.setOnClickListener {
                    handleDecline(adapterPosition)
                }
            }
        }

        private fun handleAcceptOrDispatch(position: Int) {
            if (position == RecyclerView.NO_POSITION || position >= acceptedStatus.size) {
                Log.w("PendingOrderAdapter", "Accept/Dispatch clicked on an invalid position: $position")
                return
            }

            if (!acceptedStatus[position]) {
                acceptedStatus[position] = true
                binding.acceptButton.setImageResource(R.drawable.ic_dispatch)
                binding.declineButton.visibility = View.GONE
                showToast("Order Accepted for: ${customerNames[position]}")
            } else {
                removeItem(position, "Order Dispatched for: ${customerNames[position]}")
            }
        }

        private fun handleDecline(position: Int) {
            if (position == RecyclerView.NO_POSITION || position >= customerNames.size) {
                Log.w("PendingOrderAdapter", "Decline clicked on an invalid position: $position")
                return
            }
            removeItem(position, "Order Declined for: ${customerNames[position]}")
        }

        private fun removeItem(position: Int, toastMessage: String) {
            if (position >= 0 && position < customerNames.size) {
                customerNames.removeAt(position)
                quantities.removeAt(position)
                foodImages.removeAt(position)
                acceptedStatus.removeAt(position)

                notifyItemRemoved(position)
                notifyItemRangeChanged(position, customerNames.size)

                showToast(toastMessage)
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
