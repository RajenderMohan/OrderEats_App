package com.rajender.adminordereats.Adapter

import android.graphics.Color
import android.util.Log // Added for logging errors
import android.view.LayoutInflater
// import android.view.View // This import was unused, removed for cleanliness
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val customerNames: ArrayList<String>,
    private val moneyStatus: ArrayList<String>
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding =
            DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                statusMonay.text = moneyStatus[position]

                val colorMap = mapOf(
                    "Recevied" to "#4CAF50",
                    "Not Recevied" to "#E20F0F",
                    "Pending" to "#FFC107"
                )

                // Get the value from the map (which can be a String or null)
                val colorValueFromMap = colorMap[moneyStatus[position]]

                var finalColorInt: Int
                if (colorValueFromMap != null) {
                    // If we got a string from the map, parse it
                    try {
                        finalColorInt = Color.parseColor(colorValueFromMap)
                    } catch (e: IllegalArgumentException) {
                        // Handle cases where the string in the map is not a valid color
                        Log.e("DeliveryAdapter", "Invalid color string in map: $colorValueFromMap", e)
                        finalColorInt = Color.BLACK // Default to black on error
                    }
                } else {
                    // If not found in map, use Color.BLACK
                    finalColorInt = Color.BLACK
                }

                // Apply the color
                statusMonay.setTextColor(finalColorInt) // Assuming you also want to set text color
                statusColor.backgroundTintList = android.content.res.ColorStateList.valueOf(finalColorInt)
            }
        }
    }
}
