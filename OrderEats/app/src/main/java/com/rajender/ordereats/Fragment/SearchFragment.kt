package com.rajender.ordereats.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils // <-- Import this
// Corrected SearchView import
import androidx.appcompat.widget.SearchView // <-- Use this for androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.MenuAdapter
import com.rajender.ordereats.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    // Using _binding pattern for safer view access
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter // Keep this if your MenuAdapter is designed this way

    // Original data - consider making these immutable if they don't change after init
    private val originalMenuFoodName = listOf("Burger", "Sandwich", "Pizza", "Momos", "Burger", "Sandwich", "Pizza")
    private val originalMenuItemPrice = listOf("$5", "$6", "$7", "$88", "$5", "$6", "$77")
    private val originalMenuImages = listOf(
        R.drawable.burger, R.drawable.logo_app1, R.drawable.logo_app,
        R.drawable.logo_app1, R.drawable.burger, R.drawable.logo_app, R.drawable.logo_app1
    )

    // Filtered data for the adapter
    private val filteredMenuFoodName = mutableListOf<String>()
    private val filteredMenuItemPrice = mutableListOf<String>()
    private val filteredMenuImages = mutableListOf<Int>()

    // Animation Delays
    private val DELAY_TITLE = 0L
    private val DELAY_SEARCH_VIEW = 100L
    private val DELAY_MENU_SUBTITLE = 200L
    private val DELAY_RECYCLER_VIEW_CONTAINER = 300L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Return type should be non-nullable View
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        // Adapter and RecyclerView setup moved to onViewCreated for animations
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Initialize Adapter ---
        // Initialize adapter with empty lists first, it will be populated by showAllMenu or filterMenuItems
        adapter = MenuAdapter(filteredMenuFoodName, filteredMenuItemPrice, filteredMenuImages, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        // --- Load Animations ---
        val slideInFromTop = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_top)
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in) // For "Menu" subtitle
        val fadeInRecyclerContainer = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)

        // --- Prepare Views for Animation (set initial visibility to INVISIBLE) ---
        binding.textView16.visibility = View.INVISIBLE       // "Search" Title
        binding.searchView.visibility = View.INVISIBLE       // SearchView
        binding.textView18.visibility = View.INVISIBLE       // "Menu" Subtitle
        binding.menuRecyclerView.visibility = View.INVISIBLE // RecyclerView Container

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Staggered Entrance Animations ---
        handler.postDelayed({
            binding.textView16.visibility = View.VISIBLE
            binding.textView16.startAnimation(slideInFromTop)
        }, DELAY_TITLE)

        handler.postDelayed({
            binding.searchView.visibility = View.VISIBLE
            binding.searchView.startAnimation(slideInFromTop) // Reusing slideInFromTop
        }, DELAY_SEARCH_VIEW)

        handler.postDelayed({
            binding.textView18.visibility = View.VISIBLE
            binding.textView18.startAnimation(fadeIn)
        }, DELAY_MENU_SUBTITLE)

        handler.postDelayed({
            binding.menuRecyclerView.visibility = View.VISIBLE
            binding.menuRecyclerView.startAnimation(fadeInRecyclerContainer)
            // Apply LayoutAnimation for item entrance to RecyclerView
            val layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_from_bottom)
            binding.menuRecyclerView.layoutAnimation = layoutAnimationController

            // Now that container is visible and item animation is set, load initial data
            showAllMenu()
        }, DELAY_RECYCLER_VIEW_CONTAINER)

        // --- Setup SearchView Listener ---
        setupSearchView()
    }


    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImages.clear()

        filteredMenuFoodName.addAll(originalMenuFoodName)
        filteredMenuItemPrice.addAll(originalMenuItemPrice)
        filteredMenuImages.addAll(originalMenuImages)

        // The RecyclerView's LayoutAnimation will trigger when adapter.notifyDataSetChanged()
        // is called and new items are laid out for the first time or after a significant change.
        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        // Correcting the SearchView listener type cast
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMenuItems(query ?: "") // Handle null query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMenuItems(newText ?: "") // Handle null query
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImages.clear()

        originalMenuFoodName.forEachIndexed { index, foodName ->
            if (foodName.contains(query, ignoreCase = true)) {
                filteredMenuFoodName.add(foodName)
                filteredMenuItemPrice.add(originalMenuItemPrice[index])
                filteredMenuImages.add(originalMenuImages[index])
            }
        }
        // This will also trigger item animations if items are added/removed/changed
        // and the LayoutAnimationController is set.
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Important to avoid memory leaks
    }

    // companion object can be removed if not used
    // companion object {
    // }
}

