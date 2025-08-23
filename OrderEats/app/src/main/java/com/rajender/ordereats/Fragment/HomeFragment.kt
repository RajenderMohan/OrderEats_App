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

// Make sure to import your actual MenuBottomSheetFragment if it's in a different package
// For example:
// import com.rajender.ordereats.MenuBottomSheetFragment // If MenuBottomSheetFragment is in the root of your app package
// or
// import com.rajender.ordereats.bottomsheet.MenuBottomSheetFragment // If it's in a subpackage 'bottomsheet'

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    // Animation Delays
    private val DELAY_IMAGE_SLIDER = 0L // Start immediately
    private val DELAY_POPULAR_TITLE = 200L
    private val DELAY_VIEW_MENU_BUTTON = 200L // Can be same as title or slightly offset
    private val DELAY_RECYCLER_VIEW_CONTAINER = 400L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Load Animations ---
        val scaleFadeInCard = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_fade_in_card)
        val slideInFromLeftText = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left_text)
        val slideInFromRightButton = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_right_button)
        val fadeInRecyclerContainer = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)
        val clickScale = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)

        // --- Prepare Views for Animation ---
        // Setting visibility to INVISIBLE before starting animations is a good practice
        // to prevent a "flash" of unstyled content.
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
            // Setup RecyclerView after its container is visible and animated
            setupPopularRecyclerView()
        }, DELAY_RECYCLER_VIEW_CONTAINER)

        // --- Setup Views and Click Listeners ---
        setupImageSlider()

        binding.viewAllMenu.setOnClickListener { viewClicked ->
            viewClicked.startAnimation(clickScale)

            // Ensure MenuBottomSheetFragment is correctly named, imported, and has a newInstance() method and TAG constant.
            // Example of how MenuBottomSheetFragment might be defined:
            // class MenuBottomSheetFragment : BottomSheetDialogFragment() {
            //     companion object {
            //         const val TAG = "MenuBottomSheetFragment"
            //         fun newInstance(): MenuBottomSheetFragment {
            //             return MenuBottomSheetFragment()
            //         }
            //     }
            //     // ... other fragment code ...
            // }

            // Replace this with your actual BottomSheetFragment invocation
            // If MenuBottomSheetFragment is correctly set up as a BottomSheetDialogFragment:
            // val bottomSheetDialog = MenuBottomSheetFragment.newInstance()
            // bottomSheetDialog.show(parentFragmentManager, MenuBottomSheetFragment.TAG)

            // For now, to avoid a crash if MenuBottomSheetFragment is not ready,
            // let's just show the Toast. You need to implement/uncomment the above.
            Toast.makeText(requireContext(), "View Menu Clicked - Implement BottomSheet", Toast.LENGTH_LONG).show()

            // Remove the placeholder function if MenuBottomSheetFragment is properly defined elsewhere.
            // menuBottomSheetFragment() // This line would call your TODO function and crash.
        }
    }

    // This placeholder function should be removed.
    // The actual BottomSheetFragment logic should be in its own class.
    /*
    private fun menuBottomSheetFragment(): Any {
        val todo = TODO("Not yet implemented")
    }
    */

    private fun setupImageSlider() {
        val imageList = ArrayList<SlideModel>()
        // Add your images - ensure these drawables exist in your res/drawable folders
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
            // The 'doubleClick' method does not exist in the ItemClickListener interface
            // of the denzcoskun:ImageSlideshow:0.1.0 library.
            // Removing it will fix the "overrides nothing" error.
            /*
            override fun doubleClick(position: Int) {
                // This method is not part of the interface you are implementing.
            }
            */

            override fun onItemSelected(position: Int) {
                val itemMessage = "Selected Image ${position + 1}"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupPopularRecyclerView() {
        // Sample Data - Replace with your actual data source if needed
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
            R.drawable.burger, R.drawable.banner_pizza, R.drawable.noddles, // Ensure 'noddles' isn't a typo for 'noodles' drawable
            R.drawable.paneer_tikka, R.drawable.dosas, R.drawable.chole_kulche,
            R.drawable.matka_kulfi, R.drawable.sandwich, R.drawable.momos_2,
            R.drawable.ice_cream, R.drawable.kachori, R.drawable.jalebi,
            R.drawable.mojito, R.drawable.manchurian, R.drawable.paratha
        )

        // Ensure context is available. Using requireContext() is safe within onViewCreated lifecycle and after.
        val adapter = PopulerAdapter(foodName, price, populerFoodImages, requireContext())
        binding.PopulerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopulerRecyclerView.adapter = adapter

        // Apply layout animation for items appearing in the RecyclerView
        val layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_from_bottom)
        binding.PopulerRecyclerView.layoutAnimation = layoutAnimationController
        // If you want the animation to run every time data changes (and not just the first time),
        // you might need to call adapter.notifyDataSetChanged() followed by
        // binding.PopulerRecyclerView.scheduleLayoutAnimation()
        // However, for initial population, this is generally fine.
    }

    // No need to explicitly nullify 'binding' when using lateinit var
    // and Fragment View Binding pattern with `return binding.root`.
    // The binding will be valid between onCreateView and onDestroyView.
    // override fun onDestroyView() {
    //     super.onDestroyView()
    //     // _binding = null // This line is for the nullable var _binding pattern. Not needed for lateinit.
    // }
}

