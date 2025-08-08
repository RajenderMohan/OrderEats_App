package com.rajender.ordereats.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.MenuAdapter
import com.rajender.ordereats.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
   private lateinit var binding: FragmentSearchBinding
   private lateinit var adapter: MenuAdapter
   private val originalMenuFoodName = listOf("Burger","Sandwich","Pizza","Momos", "Burger","Sandwich","Pizza")
    private val originalMenuItemPrice = listOf("$5","$6","$7","$88","$5","$6","$77")
    private val originalMenuImages = listOf(R.drawable.burger, R.drawable.logo_app1, R.drawable.logo_app, R.drawable.logo_app1, R.drawable.burger, R.drawable.logo_app, R.drawable.logo_app1,)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val filteredMenuFoodName = mutableListOf<String>()
    private val filteredMenuItemPrice = mutableListOf<String>()
    private val filteredMenuImages = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        adapter = MenuAdapter(filteredMenuFoodName, filteredMenuItemPrice, filteredMenuImages, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        // Setup for Search view
        setupSearchView()
        // show all menus Items
        showAllMenu()

        return binding.root
    }

    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImages.clear()

        filteredMenuFoodName.addAll(originalMenuFoodName)
        filteredMenuItemPrice.addAll(originalMenuItemPrice)
        filteredMenuImages.addAll(originalMenuImages)

        adapter.notifyDataSetChanged()

    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextSubmit(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }

        })
    }

    private fun filterMenuItems(query: String) {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImages.clear()

        originalMenuFoodName.forEachIndexed{index, foodName ->
        if (foodName.contains(query, ignoreCase = true)){
            filteredMenuFoodName.add(foodName)
            filteredMenuItemPrice.add(originalMenuItemPrice[index])
            filteredMenuImages.add(originalMenuImages[index])

        }
    }
        adapter.notifyDataSetChanged()
}
    companion object {

    }
}
