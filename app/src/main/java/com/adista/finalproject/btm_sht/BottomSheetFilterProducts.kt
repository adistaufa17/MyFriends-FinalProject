package com.adista.finalproject.btm_sht

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.zip.Inflater

class BottomSheetFilterProducts (
    private val onSave: (filter: String) -> Unit
) : BottomSheetDialogFragment(){

    private var _binding: BottomSheetFilterProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFilterProducts.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSimpan.setOnClickListener {
            val selectedFilter = when (binding.filter.checkedRadioButtonId) {
                binding.filterTops.id -> "tops"
                binding.filterVehicle.id -> "vehicle"
                binding.filterLaptops.id -> "laptops"
                binding.filterMotorcycle.id -> "motorcycle"
                else -> ""
            }

            onSave(selectedFilter)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}