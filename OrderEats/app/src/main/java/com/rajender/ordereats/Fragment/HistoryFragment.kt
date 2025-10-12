package com.rajender.ordereats.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.BuyAgainAdapter
import com.rajender.ordereats.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    // Using _binding pattern for safer view access
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var buyAgainAdapter: BuyAgainAdapter // Keep this
    // Data for the RecyclerView (can be kept as is for this example)
    private val buyAgainFoodName = arrayListOf(
        "Cheese Burger", "Veggie Pizza", "Noodles", "Paneer Tikka",
        "Masala Dosa", "Chole Bhature", "Makta Kulfi", "Veg Sandwich",
        "Momo Platter", "Ice Cream", "Kachori", "Desi Jalebi", "Mojito",
        "Manchurian", "Desi Aalu Paratha")
    private val buyAgainFoodPrice = arrayListOf("₹30", "₹120", "₹100", "₹99",
        "₹79", "₹58", "₹150", "₹50", "₹110","₹99","₹30","₹50","₹50","₹70","₹40")
    private val buyAgainFoodImage = arrayListOf(
        R.drawable.burger,
        R.drawable.banner_pizza,
        R.drawable.noddles,
        R.drawable.paneer_tikka, // Replace with your actual drawables
        R.drawable.dosas,
        R.drawable.chole_kulche,
        R.drawable.matka_kulfi,
        R.drawable.sandwich,
        R.drawable.momos_2,
        R.drawable.ice_cream,
        R.drawable.kachori,
        R.drawable.jalebi,
        R.drawable.mojito,
        R.drawable.manchurian,
        R.drawable.paratha
    )

    // Dummy data for the static card view (if it's dynamic, fetch this too)
    // You'll need to decide what to show in the top card or if it's also from history
    private val staticBuyAgainFoodName = "Last Order Item" // Example
    private val staticBuyAgainFoodPrice = "₹35" // Example
    private val staticBuyAgainFoodImage = R.drawable.noodles // Example, ensure this drawable exists

    // Animation Delays
    private val DELAY_TITLE1 = 0L
    private val DELAY_MAIN_CARD = 150L
    private val DELAY_TITLE2 = 300L
    private val DELAY_RECYCLER_VIEW_CONTAINER = 450L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Return type should be non-nullable View
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        // RecyclerView setup and other view manipulations moved to onViewCreated
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Load Animations ---
        val slideInFromLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left_text)
        val scaleFadeInCard = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_fade_in_card)
        val fadeInRecyclerContainer = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)
        val clickScale = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale) // For receivedButton

        // --- Prepare Views for Animation (set initial visibility to INVISIBLE) ---
        binding.textView19.visibility = View.INVISIBLE         // "Recent Buy" Title 1
        binding.cardView.visibility = View.INVISIBLE           // Main Buy Again CardView
        binding.textView22.visibility = View.INVISIBLE         // "Recent buy" Title 2
        binding.BuyAgainRecyclerView.visibility = View.INVISIBLE // RecyclerView Container

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Staggered Entrance Animations ---
        handler.postDelayed({
            binding.textView19.visibility = View.VISIBLE
            binding.textView19.startAnimation(slideInFromLeft)
        }, DELAY_TITLE1)

        handler.postDelayed({
            binding.cardView.visibility = View.VISIBLE
            binding.cardView.startAnimation(scaleFadeInCard)
            populateStaticCardView() // Populate the static card's content
        }, DELAY_MAIN_CARD)

        handler.postDelayed({
            binding.textView22.visibility = View.VISIBLE
            binding.textView22.startAnimation(slideInFromLeft) // Reusing slideInFromLeft
        }, DELAY_TITLE2)

        handler.postDelayed({
            binding.BuyAgainRecyclerView.visibility = View.VISIBLE
            binding.BuyAgainRecyclerView.startAnimation(fadeInRecyclerContainer)
            // Setup adapter and RecyclerView item animations AFTER container is visible
            setupRecyclerViewWithAnimations()
        }, DELAY_RECYCLER_VIEW_CONTAINER)

        // --- Setup Click Listener for Received Button ---
        binding.receivedButton.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            // Add actual logic for what "Received" means
            android.widget.Toast.makeText(requireContext(), "Order marked as Received!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateStaticCardView() {
        // Sets content for the CardView with id 'cardView' in your XML
        binding.buyAgainFoodName.text = staticBuyAgainFoodName // This ID is inside cardView
        binding.buyAgainFoodPrice.text = staticBuyAgainFoodPrice // This ID is inside cardView
        binding.buyAgainFoodImage.setImageResource(staticBuyAgainFoodImage) // This ID is inside cardView
    }

    private fun setupRecyclerViewWithAnimations() {
        // Your existing adapter setup logic
        buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImage)
        binding.BuyAgainRecyclerView.adapter = buyAgainAdapter
        binding.BuyAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Apply LayoutAnimation for item entrance
        val layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_from_bottom)
        binding.BuyAgainRecyclerView.layoutAnimation = layoutAnimationController
        // Since data is already available, adapter.notifyDataSetChanged() is not strictly needed here
        // for the initial animation, as the items will animate on first layout.
        // However, if data were loaded asynchronously, you'd call it after data is ready.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Important to avoid memory leaks
    }

    // companion object can be removed if not used
    // companion object {
    // }
}
