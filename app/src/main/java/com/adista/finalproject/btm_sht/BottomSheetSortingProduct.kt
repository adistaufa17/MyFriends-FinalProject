package com.adista.finalproject.btm_sht

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSortingProduct (
    private val onSave: (sortBy: String, order: String) -> Unit
) : BottomSheetDialogFragment(){

    private var _binding: BottomSheetSortingProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetSortingProductBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSimpan.setOnClickListener {
            val selectedSort = when (binding.sort.checkedRadionButtonId) {
                binding.sortTitle.id -> "titie"
                binding.sortDesc.id -> "description"
                else -> ""
            }

            val selectedOrder = when (binding.order.checkedRadionButtonId) {
                binding.orderAsc.id -> "asc"
                binding.orderDsc.id -> "dsc"
                else -> ""
            }
        }
    }
}