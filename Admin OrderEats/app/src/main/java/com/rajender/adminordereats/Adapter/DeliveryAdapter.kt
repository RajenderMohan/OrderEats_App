package com.rajender.adminordereats.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val customerNames: ArrayList<String>,
    private val paymentStatus: ArrayList<String>,
    private val deliveryStatus: ArrayList<String>,
    private val customerImages: ArrayList<Int>,
    private val context: Context
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    private val colorMap = mapOf(
        "Received" to "#4CAF50",     // Green
        "Not Received" to "#D32F2F", // Red
        "Pending" to "#FFC107",      // Yellow
        "Delivered" to "#4CAF50",    // Green
        "Out for Delivery" to "#2196F3" // Blue
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding =
            DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        if (position < customerNames.size) {
            holder.bind(
                customerNames[position],
                paymentStatus[position],
                deliveryStatus[position],
                customerImages[position]
            )
        } else {
            Log.e("DeliveryAdapter", "Invalid position: $position")
        }
    }

    override fun getItemCount(): Int {
        return customerNames.size
    }

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            customerNameText: String,
            paymentStatusText: String,
            deliveryStatusText: String,
            customerImageRes: Int
        ) {
            binding.apply {
                customerName.text = customerNameText
                paymentStatus.text = paymentStatusText
                deliveryStatus.text = deliveryStatusText
                customerImageView.setImageResource(customerImageRes)

                // Set text colors based on status
                paymentStatus.setTextColor(getColorForStatus(paymentStatusText))
                deliveryStatus.setTextColor(getColorForStatus(deliveryStatusText))

                // Set up the call button
                callButton.setOnClickListener {
                    // You would typically get the phone number from your data model
                    val phoneNumber = "1234567890" // Placeholder phone number
                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                    try {
                        context.startActivity(dialIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not open dialer", Toast.LENGTH_SHORT).show()
                        Log.e("DeliveryAdapter", "Failed to start dial intent", e)
                    }
                }
            }
        }

        private fun getColorForStatus(status: String): Int {
            val colorString = colorMap[status]
            return if (colorString != null) {
                try {
                    Color.parseColor(colorString)
                } catch (e: IllegalArgumentException) {
                    Log.e("DeliveryAdapter", "Invalid color string: $colorString", e)
                    Color.BLACK // Fallback color
                }
            } else {
                Color.BLACK // Default color for unknown status
            }
        }
    }
}
