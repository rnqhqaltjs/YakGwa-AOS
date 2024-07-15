package com.prography.yakgwa.ui.createPromise

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.DialogExitModalBinding
import com.prography.yakgwa.util.base.BaseDialogFragment
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