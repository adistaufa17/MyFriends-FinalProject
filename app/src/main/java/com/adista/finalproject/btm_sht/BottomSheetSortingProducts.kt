package com.adista.finalproject.btm_sht

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adista.finalproject.databinding.BottomSheetSortingProductsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSortingProducts (
    private val onSave: (sortBy: String, order: String) -> Unit
) : BottomSheetDialogFragment(){

    private var _binding: BottomSheetSortingProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSortingProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSimpan.setOnClickListener {
            val selectedSort = when (binding.sort.checkedRadioButtonId) {
                binding.sortTitle.id -> "title"
                binding.sortDesc.id -> "description"
                else -> ""
            }

            val selectedOrder = when (binding.order.checkedRadioButtonId) {
                binding.orderAsc.id -> "asc"
                binding.orderDesc.id -> "desc"
                else -> ""
            }
            onSave(selectedSort, selectedOrder)
            dismiss()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}