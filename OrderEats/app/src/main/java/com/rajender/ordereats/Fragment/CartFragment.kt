package com.rajender.ordereats.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.ordereats.PayOutActivity
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.CartAdapter
import com.rajender.ordereats.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    // Use nullable binding and initialize in onCreateView, clear in onDestroyView
    private var _binding: FragmentCartBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!! // !! is safe here due to lifecycle management

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Return type changed to non-nullable View
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        val cartFoodName = listOf("Burger", "Pizza", "Pasta", "Salad", "Fries", "Sandwich", "Sushi", "Tacos") // Added more items for scroll testing
        val cartItemPrice = listOf("$5", "$6", "$7", "$8", "$3", "$7", "$10", "$6") // Added corresponding prices
        val cartImage = listOf(
            R.drawable.burger, R.drawable.logo_app1, R.drawable.logo_app, R.drawable.burger,
            R.drawable.burger, // Replace with actual drawable names if you have more
            R.drawable.logo_app1,
            R.drawable.logo_app,
            R.drawable.burger
        )
        val adapter = CartAdapter(
            ArrayList(cartFoodName), // No need to explicitly type ArrayList<String>
            ArrayList(cartItemPrice),
            ArrayList(cartImage)
        )
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        // You can add click listener for your proceed button here if needed
        binding.proceedButton.setOnClickListener {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Important to avoid memory leaks
    }

    companion object {

//                }
//            }
    }
}
