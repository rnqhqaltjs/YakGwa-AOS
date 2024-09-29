package com.yomo.yakgwa.ui.createPromise

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.DialogExitModalBinding
import com.yomo.yakgwa.util.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExitDialogFragment : BaseDialogFragment<DialogExitModalBinding>(R.layout.dialog_exit_modal) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners()
    }

    private fun addListeners() {
        binding.btnCreateContinue.setOnClickListener {
            dismiss()
        }
        
        binding.btnExitScreen.setOnClickListener {
            findNavController().navigate(R.id.action_global_home)
        }
    }
}