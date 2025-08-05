package com.rajender.ordereats.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.BuyAgainAdapter
import com.rajender.ordereats.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        setupRecyclerView()
        return binding.root
    }
    private fun setupRecyclerView() {
        val buyAgainFoodName = arrayListOf("Pizza", "Burger", "Pizza", "Burger", "Pizza", "Burger")
        val buyAgainFoodPrice = arrayListOf("$5", "$5", "$5", "$5", "$5", "$5")
        val buyAgainFoodImage = arrayListOf(R.drawable.pizzas, R.drawable.burger, R.drawable.pizzas, R.drawable.burger, R.drawable.pizzas, R.drawable.burger)
        buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImage)
        binding.BuyAgainRecyclerView.adapter = buyAgainAdapter
        binding.BuyAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    companion object {

    }
}