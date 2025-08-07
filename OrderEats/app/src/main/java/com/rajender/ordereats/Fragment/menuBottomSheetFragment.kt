package com.rajender.ordereats.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.MenuAdapter
import com.rajender.ordereats.databinding.FragmentMenuBottomSheetBinding

class menuBottomSheetFragment : BottomSheetDialogFragment(){
    private lateinit var binding : FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.buttonBack.setOnClickListener{
            dismiss()
        }

        val menuFoodName = listOf("Burger","Sandwich","Pizza","Momos", "Burger","Sandwich","Pizza", "Burger","Sandwich","Pizza", "Burger",)
        val menuPrice = listOf("$5","$6","$7","$88","$5","$6","$77", "$5","$6", "$5","$6" )
        val menuImage = listOf(R.drawable.burger, R.drawable.logo_app1, R.drawable.logo_app, R.drawable.logo_app1, R.drawable.burger, R.drawable.logo_app, R.drawable.logo_app1, R.drawable.burger, R.drawable.logo_app1, R.drawable.logo_app, R.drawable.logo_app1)
        val adapter = MenuAdapter(ArrayList(menuFoodName), ArrayList(menuPrice), ArrayList(menuImage))
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }

}