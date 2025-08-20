package com.rajender.ordereats.Fragment // Your actual package

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.PopulerAdapter
import com.rajender.ordereats.databinding.FragmentHomeBinding

// Remember to import your actual MenuBottomSheetFragment
// For example: import com.rajender.ordereats.Fragment.MenuBottomSheetFragment

class HomeFragment : Fragment() {
    // Replaced _binding with lateinit var binding
    private lateinit var binding: FragmentHomeBinding

    private val DELAY_IMAGE_SLIDER = 0L
    private val DELAY_POPULAR_TITLE = 200L
    private val DELAY_VIEW_MENU_BUTTON = 200L
    private val DELAY_RECYCLER_VIEW_CONTAINER = 400L // Renamed for clarity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Load Animations ---
        val scaleFadeInCard =
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale_fade_in_card)
        val slideInFromLeft =
            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left_text)
        val slideInFromRight =
            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_right_button)
        // Animation for the RecyclerView container itself
        val fadeInRecyclerContainer =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)
        val clickScale = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)

        // --- Prepare Views for Animation ---
        val viewsToAnimate = listOf(
            binding.cardView3,
            binding.textView15,
            binding.viewAllMenu,
            binding.PopulerRecyclerView // The container
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Staggered Entrance Animations ---
        handler.postDelayed({
            binding.cardView3.visibility = View.VISIBLE
            binding.cardView3.startAnimation(scaleFadeInCard)
        }, DELAY_IMAGE_SLIDER)

        handler.postDelayed({
            binding.textView15.visibility = View.VISIBLE
            binding.textView15.startAnimation(slideInFromLeft)
        }, DELAY_POPULAR_TITLE)

        handler.postDelayed({
            binding.viewAllMenu.visibility = View.VISIBLE
            binding.viewAllMenu.startAnimation(slideInFromRight)
        }, DELAY_VIEW_MENU_BUTTON)

        // Animate RecyclerView container, then its items via LayoutAnimation in setupPopularRecyclerView
        handler.postDelayed({
            binding.PopulerRecyclerView.visibility = View.VISIBLE
            binding.PopulerRecyclerView.startAnimation(fadeInRecyclerContainer)
            setupPopularRecyclerView() // This will now apply item animations
        }, DELAY_RECYCLER_VIEW_CONTAINER)

        // --- Setup Views and Click Listeners ---
        setupImageSlider()

        binding.viewAllMenu.setOnClickListener {
            it.startAnimation(clickScale)
            // Make sure MenuBottomSheetFragment is correctly defined, named (PascalCase), and imported
            // Example of correct usage:
            val bottomSheetDialog = MenuBottomSheetFragment.newInstance()
            bottomSheetDialog.show(parentFragmentManager, MenuBottomSheetFragment.TAG)

            // Current placeholder - this will likely cause an "Unresolved reference" error
            // if menuBottomSheetFragment is not defined as such or not imported.
//             val bottomSheetDialog = menuBottomSheetFragment()
//             bottomSheetDialog.show(parentFragmentManager, "TestMenuSheet")
            Toast.makeText(requireContext(), "View Menu Clicked", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun menuBottomSheetFragment(): Any {
        val todo = TODO("Not yet implemented")
    }

    private fun setupImageSlider() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner_burger, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.chole_kulche, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.dosa, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.momo, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.noodles, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.paneer, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner_pizza, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.momos_2, ScaleTypes.FIT))


        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                // TODO("Not yet implemented for double click")
            }

            override fun onItemSelected(position: Int) {
                val itemMessage = "Selected Image ${position + 1}"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupPopularRecyclerView() {
        val foodName = listOf(
            "Cheese Burger",
            "Veggie Pizza",
            "Chicken Noodles",
            "Paneer Tikka",
            "Masala Dosa",
            "Chole Bhature",
            "Spring Rolls",
            "Veg Sandwich",
            "Momo Platter"
        )
        val price = listOf("₹30", "₹120", "₹100", "₹99", "₹79", "₹58", "₹69", "₹50", "₹110")
        val populerFoodImages = listOf(
            R.drawable.burger,
            R.drawable.banner_pizza,
            R.drawable.noddles,
            R.drawable.paneer_tikka, // Replace with your actual drawables
            R.drawable.dosas,
            R.drawable.chole_kulche,
            R.drawable.momo,
            R.drawable.logo_app,
            R.drawable.momos_2
        )

        val context = requireContext() // Context is still needed for LayoutManager
        val adapter = PopulerAdapter(foodName, price, populerFoodImages, context)
        binding.PopulerRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.PopulerRecyclerView.adapter = adapter

        // --- ADD THIS FOR RECYCLERVIEW ITEM ANIMATIONS ---
        val layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(
                context, // Using local context variable
                R.anim.layout_animation_from_bottom
            ) // Or your chosen layout animation
        binding.PopulerRecyclerView.layoutAnimation = layoutAnimationController
        // --- END OF ADDITION ---
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // No need to explicitly nullify 'binding' when using lateinit.
        // It will be re-initialized in onCreateView if the view is recreated.
    }

    // The placeholder class for menuBottomSheetFragment should be removed if it's
    // defined in its own file and properly imported.
    // class menuBottomSheetFragment : com.google.android.material.bottomsheet.BottomSheetDialogFragment() { ... }
}
