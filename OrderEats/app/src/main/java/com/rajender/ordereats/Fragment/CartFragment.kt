package com.rajender.ordereats.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.ordereats.PayOutActivity
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.CartAdapter
import com.rajender.ordereats.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    // Replace _binding and binding getter with lateinit var
    private lateinit var binding: FragmentCartBinding

    // Animation Delays
    private val DELAY_TITLE = 0L
    private val DELAY_RECYCLER_VIEW_CONTAINER = 150L // Adjusted delay
    private val DELAY_PROCEED_BUTTON = 300L       // Adjusted delay

    // Data for the adapter (can be moved to a ViewModel or data source later)
    private val cartFoodName = listOf("Burger", "Pizza", "Pasta", "Salad", "Fries", "Sandwich", "Sushi", "Tacos")
    private val cartItemPrice = listOf("$5", "$6", "$7", "$8", "$3", "$7", "$10", "$6")
    private val cartImage = listOf(
        R.drawable.burger, R.drawable.logo_app1, R.drawable.logo_app, R.drawable.burger,
        R.drawable.burger, R.drawable.logo_app1, R.drawable.logo_app, R.drawable.burger
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding here
        binding = FragmentCartBinding.inflate(inflater, container, false)
        // Note: Adapter setup and click listeners are better handled in onViewCreated
        // when the view hierarchy is fully available.
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Load Animations ---
        val slideInFromTop = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_top)
        val fadeInRecyclerContainer = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)
        val slideInFromBottomButton = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_bottom_button)
        val clickScale = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)

        // --- Prepare Views for Animation (set initial visibility to INVISIBLE) ---
        // Access views directly via the 'binding' property
        binding.textView17.visibility = View.INVISIBLE       // "Cart" Title
        binding.cartRecyclerView.visibility = View.INVISIBLE // RecyclerView Container
        binding.proceedButton.visibility = View.INVISIBLE    // Proceed Button

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Staggered Entrance Animations ---
        handler.postDelayed({
            binding.textView17.visibility = View.VISIBLE
            binding.textView17.startAnimation(slideInFromTop)
        }, DELAY_TITLE)

        handler.postDelayed({
            binding.cartRecyclerView.visibility = View.VISIBLE
            binding.cartRecyclerView.startAnimation(fadeInRecyclerContainer)
            // Setup adapter and RecyclerView item animations AFTER container is visible
            setupRecyclerViewWithAnimations()
        }, DELAY_RECYCLER_VIEW_CONTAINER)

        handler.postDelayed({
            binding.proceedButton.visibility = View.VISIBLE
            binding.proceedButton.startAnimation(slideInFromBottomButton)
        }, DELAY_PROCEED_BUTTON)

        // --- Setup Click Listener for Proceed Button ---
        binding.proceedButton.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerViewWithAnimations() {
        val adapter = CartAdapter(
            ArrayList(cartFoodName),
            ArrayList(cartItemPrice),
            ArrayList(cartImage)
        )
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        // Apply LayoutAnimation for item entrance
        val layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_from_bottom)
        binding.cartRecyclerView.layoutAnimation = layoutAnimationController
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // No need to set binding to null with lateinit var.
        // The binding object will be eligible for garbage collection when the fragment's view is destroyed.
        // However, be mindful not to access `binding` after `onDestroyView()` and before
        // the next `onCreateView()` if the fragment instance itself is reused.
    }

    // companion object can be removed if not used
    // companion object {
    // }
}
