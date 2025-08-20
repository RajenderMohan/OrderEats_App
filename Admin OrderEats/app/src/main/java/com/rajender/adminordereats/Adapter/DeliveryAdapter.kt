package com.rajender.adminordereats.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rajender.adminordereats.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val customerNames: ArrayList<String>,
    private val moneyStatus: ArrayList<String>,
    private val context: Context // Context can be useful
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    // Color map using corrected spellings as keys
    private val colorMap = mapOf(
        "Received" to "#4CAF50",     // Green for received
        "Not Received" to "#E20F0F", // Red for not received
        "Pending" to "#FFC107"      // Yellow for pending
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding =
            DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        // Check bounds to be safe, though lists should be the same size
        if (position < customerNames.size && position < moneyStatus.size) {
            holder.bind(customerNames[position], moneyStatus[position])
        }
    }

    override fun getItemCount(): Int {
        // Ideally, ensure both lists are always the same size before passing to the adapter.
        // This minOf is a safeguard.
        return minOf(customerNames.size, moneyStatus.size)
    }

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customerNameText: String, moneyStatusText: String) {
            binding.apply {
                // Assuming your delivery_item.xml has:
                // TextView with id "customerName"
                // TextView with id "statusMoney"
                // View (e.g., ImageView or simple View) with id "statusColor"

                customerName.text = customerNameText
                statusMoney.text = moneyStatusText

                val colorValueFromMap = colorMap[moneyStatusText] // Use the passed moneyStatusText

                val finalColorInt: Int = if (colorValueFromMap != null) {
                    try {
                        Color.parseColor(colorValueFromMap)
                    } catch (e: IllegalArgumentException) {
                        Log.e("DeliveryAdapter", "Invalid color string in map: $colorValueFromMap for status: $moneyStatusText", e)
                        Color.DKGRAY // A different default for parsing errors
                    }
                } else {
                    Log.w("DeliveryAdapter", "Status string '$moneyStatusText' not found in colorMap. Defaulting color.")
                    Color.BLACK // Default if status string is not in map
                }

                statusMoney.setTextColor(finalColorInt)
                statusColor.backgroundTintList = android.content.res.ColorStateList.valueOf(finalColorInt)
            }
        }
    }
}
