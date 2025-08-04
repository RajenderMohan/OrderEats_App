package com.rajender.ordereats.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.PopulerAdapter
import com.rajender.ordereats.databinding.FragmentHomeBinding

@Suppress("UNREACHABLE_CODE")
class HomeFragment : Fragment() {
    private  lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container,false)
        // Inflate the layout for this fragment

        binding.viewAllMenu.setOnClickListener{
            val bottomSheetDialog = menuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.burger, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.logo_app, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.logo_app1, ScaleTypes.FIT))


        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object  : ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMassage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMassage,Toast.LENGTH_SHORT).show()
            }
        })
        val foodName = listOf("Burger","Sandwich","Pizza","Momos", "Burger","Sandwich","Pizza")
        val Price = listOf("$5","$6","$7","$88","$5","$6","$77")
        val populerFoodImages =
            listOf(R.drawable.burger, R.drawable.logo_app1, R.drawable.logo_app, R.drawable.logo_app1, R.drawable.burger, R.drawable.logo_app, R.drawable.logo_app1,)
        val adapter = PopulerAdapter(foodName, Price, populerFoodImages)
        binding.PopulerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopulerRecyclerView.adapter = adapter

    }

    companion object{

    }
}