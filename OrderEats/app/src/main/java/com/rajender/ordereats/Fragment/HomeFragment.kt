package com.rajender.ordereats.Fragment // Your actual package

// *** IMPORTANT: Make sure this import is correct for YOUR project structure ***
// If MenuBottomSheetFragment is in the same 'Fragment' package:
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

// If MenuBottomSheetFragment is in the root 'com.rajender.ordereats' package:
// import com.rajender.ordereats.MenuBottomSheetFragment
// If it's in a subpackage like 'bottomsheet':
// import com.rajender.ordereats.bottomsheet.MenuBottomSheetFragment


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    // Using lateinit var for binding, so no need for nullable _binding and separate getter

    // Animation Delays
    private val DELAY_IMAGE_SLIDER = 0L
    private val DELAY_POPULAR_TITLE = 200L
    private val DELAY_VIEW_MENU_BUTTON = 200L
    private val DELAY_RECYCLER_VIEW_CONTAINER = 400L

    companion object {
        private const val TAG = "HomeFragment" // For logging
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView called")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")

        // --- Load Animations ---
        val scaleFadeInCard = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_fade_in_card)
        val slideInFromLeftText = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left_text)
        val slideInFromRightButton = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_right_button)
        val fadeInRecyclerContainer = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)
        val clickScale = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)

        // --- Prepare Views for Animation ---
        binding.cardView3.visibility = View.INVISIBLE
        binding.textView15.visibility = View.INVISIBLE
        binding.viewAllMenu.visibility = View.INVISIBLE
        binding.PopulerRecyclerView.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Staggered Entrance Animations ---
        handler.postDelayed({
            binding.cardView3.visibility = View.VISIBLE
            binding.cardView3.startAnimation(scaleFadeInCard)
        }, DELAY_IMAGE_SLIDER)

        handler.postDelayed({
            binding.textView15.visibility = View.VISIBLE
            binding.textView15.startAnimation(slideInFromLeftText)
        }, DELAY_POPULAR_TITLE)

        handler.postDelayed({
            binding.viewAllMenu.visibility = View.VISIBLE
            binding.viewAllMenu.startAnimation(slideInFromRightButton)
        }, DELAY_VIEW_MENU_BUTTON)

        handler.postDelayed({
            binding.PopulerRecyclerView.visibility = View.VISIBLE
            binding.PopulerRecyclerView.startAnimation(fadeInRecyclerContainer)
            setupPopularRecyclerView()
        }, DELAY_RECYCLER_VIEW_CONTAINER)

        // --- Setup Views and Click Listeners ---
        setupImageSlider()

        // --- Corrected Click Listener for viewAllMenu ---
        binding.viewAllMenu.setOnClickListener { viewClicked ->
            Log.d(TAG, "viewAllMenu clicked")
            viewClicked.startAnimation(clickScale) // Apply click animation

            // --- Show the MenuBottomSheetFragment ---
            // Ensure MenuBottomSheetFragment.kt exists and is correctly defined
            // as a BottomSheetDialogFragment.

            // Option 1: Simple instantiation if no arguments are needed
            // val menuBottomSheet = MenuBottomSheetFragment()

            // Option 2: Using a newInstance pattern (recommended if you might add arguments later)
            // This assumes your MenuBottomSheetFragment has a companion object with newInstance()
            try {
                val menuBottomSheet = MenuBottomSheetFragment.newInstance() // Make sure newInstance() exists
                // The TAG used here should ideally be a public constant in MenuBottomSheetFragment
                menuBottomSheet.show(parentFragmentManager, MenuBottomSheetFragment.TAG)
                Log.d(TAG, "MenuBottomSheetFragment shown successfully.")
            } catch (e: Exception) {
                Log.e(TAG, "Error showing MenuBottomSheetFragment: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: Could not open menu.", Toast.LENGTH_SHORT).show()
            }

            // The old Toast is now replaced by the actual BottomSheet display logic.
            // Toast.makeText(requireContext(), "View Menu Clicked - Implement BottomSheet", Toast.LENGTH_LONG).show()
        }
    }

    // The placeholder menuBottomSheetFragment() function is correctly commented out / removed.
    // Ensure it's fully removed if it was a placeholder.

    private fun setupImageSlider() {
        Log.d(TAG, "setupImageSlider called")
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner_burger, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.chole_kulche, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.dosa, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.momo, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.noodles, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.paneer, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner_pizza, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.momos_2, ScaleTypes.FIT))

        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                val itemMessage = "Selected Image ${position + 1}"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "ImageSlider item selected: $position")
            }
            // The doubleClick method was correctly removed as it's not in the interface.
        })
    }

    private fun setupPopularRecyclerView() {
        Log.d(TAG, "setupPopularRecyclerView called")
        val foodName = listOf(
            "Cheese Burger", "Veggie Pizza", "Noodles", "Paneer Tikka", "Masala Dosa",
            "Chole Bhature", "Makta Kulfi", "Veg Sandwich", "Momo Platter", "Ice Cream",
            "Kachori", "Desi Jalebi", "Mojito", "Manchurian", "Desi Aalu Paratha"
        )
        val price = listOf(
            "₹30", "₹120", "₹100", "₹99", "₹79", "₹58", "₹150", "₹50", "₹110",
            "₹99", "₹30", "₹50", "₹50", "₹70", "₹40"
        )
        val populerFoodImages = listOf(
            R.drawable.burger, R.drawable.banner_pizza, R.drawable.noddles,
            R.drawable.paneer_tikka, R.drawable.dosas, R.drawable.chole_kulche,
            R.drawable.matka_kulfi, R.drawable.sandwich, R.drawable.momos_2,
            R.drawable.ice_cream, R.drawable.kachori, R.drawable.jalebi,
            R.drawable.mojito, R.drawable.manchurian, R.drawable.paratha
        )

        val adapter = PopulerAdapter(foodName, price, populerFoodImages, requireContext())
        binding.PopulerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopulerRecyclerView.adapter = adapter

        val layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_from_bottom)
        binding.PopulerRecyclerView.layoutAnimation = layoutAnimationController
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")
        // No need to explicitly nullify 'binding' when using 'private lateinit var binding'
        // and returning binding.root directly from onCreateView.
        // The binding is automatically cleared by the Fragment's view lifecycle.
        // If you were using the pattern:
        // private var _binding: FragmentHomeBinding? = null
        // private val binding get() = _binding!!
        // Then you would do: _binding = null here.
    }
}

