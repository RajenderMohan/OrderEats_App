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
import com.rajender.ordereats.data.CartRepository
import com.rajender.ordereats.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val DELAY_TITLE = 0L
    private val DELAY_RECYCLER_VIEW_CONTAINER = 150L
    private val DELAY_PROCEED_BUTTON = 300L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slideInFromTop = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_top)
        val fadeInRecyclerContainer = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)
        val slideInFromBottomButton = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_bottom_button)
        val clickScale = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)

        binding.textView17.visibility = View.INVISIBLE
        binding.cartRecyclerView.visibility = View.INVISIBLE
        binding.proceedButton.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            binding.textView17.visibility = View.VISIBLE
            binding.textView17.startAnimation(slideInFromTop)
        }, DELAY_TITLE)

        handler.postDelayed({
            binding.cartRecyclerView.visibility = View.VISIBLE
            binding.cartRecyclerView.startAnimation(fadeInRecyclerContainer)
            setupRecyclerViewWithAnimations()
        }, DELAY_RECYCLER_VIEW_CONTAINER)

        handler.postDelayed({
            binding.proceedButton.visibility = View.VISIBLE
            binding.proceedButton.startAnimation(slideInFromBottomButton)
        }, DELAY_PROCEED_BUTTON)

        binding.proceedButton.setOnClickListener {
            it.startAnimation(clickScale)
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerViewWithAnimations() {
        val adapter = CartAdapter(
            CartRepository.cartItemNames,
            CartRepository.cartItemPrices,
            CartRepository.cartItemImages
        )
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        val layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_from_bottom)
        binding.cartRecyclerView.layoutAnimation = layoutAnimationController
    }
}