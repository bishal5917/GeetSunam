package com.example.geetsunam.features.presentation.home.change_password

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.geetsunam.databinding.FragmentChangePasswordBinding
import com.example.geetsunam.features.presentation.home.change_password.viewmodel.ChangePasswordEvent
import com.example.geetsunam.features.presentation.home.change_password.viewmodel.ChangePasswordState
import com.example.geetsunam.features.presentation.home.change_password.viewmodel.ChangePasswordViewModel
import com.example.geetsunam.features.presentation.login.LoginActivity
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.LocalController
import com.example.geetsunam.utils.Validation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    lateinit var binding: FragmentChangePasswordBinding

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        watchTextChange(binding.etCurrentPassword, 0)
        watchTextChange(binding.etNewPassword, 1)
        watchTextChange(binding.etRepeatNewPassword, 2)
        binding.btnChangePasswordSubmit.setOnClickListener {
            LocalController().unfocusKeyboard(requireActivity())
            if (splashViewModel.userFlow.value?.isGoogleLogin == true) {
                CustomToast.showToast(requireContext(), "Can't perform this action.")
            } else {
                changePasswordViewModel.onEvent(ChangePasswordEvent.CheckValidation)
            }
        }
        observePasswordChange()
        return binding.root
    }

    private fun watchTextChange(editText: EditText, index: Int) {
        editText.doOnTextChanged { text, start, before, count ->
            when (index) {
                0 -> {
                    val validationResult = Validation.validatePassword(text.toString())
                    binding.tvCurrentPasswordError.text = validationResult.message
                    changePasswordViewModel.onEvent(
                        ChangePasswordEvent.PasswordChanged(
                            validationResult, text.toString()
                        )
                    )
                }

                1 -> {
                    val validationResult = Validation.validatePassword(text.toString())
                    binding.tvNewPasswordError.text = validationResult.message
                    changePasswordViewModel.onEvent(
                        ChangePasswordEvent.NewPasswordChanged(
                            validationResult, text.toString()
                        )
                    )
                }

                2 -> {
                    val validationResult = Validation.confirmPassword(
                        binding.etNewPassword.text.toString(), text.toString()
                    )
                    binding.tvRepeatNewPasswordError.text = validationResult.message
                    changePasswordViewModel.onEvent(
                        ChangePasswordEvent.ConfirmNewPasswordChanged(
                            validationResult, text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun observePasswordChange() {
        val dialog = Dialog(requireContext())
        changePasswordViewModel.changePasswordState.observe(viewLifecycleOwner) { response ->
            if (response.status == ChangePasswordState.ChangePasswordStatus.FormValid) {
                changePasswordViewModel.onEvent(
                    ChangePasswordEvent.ChangePassword(splashViewModel.userFlow.value?.token.toString())
                )
            }
            if (response.status == ChangePasswordState.ChangePasswordStatus.FormInvalid) {
                CustomToast.showToast(requireContext(), "${response.message}")
            }
            if (response.status == ChangePasswordState.ChangePasswordStatus.LOADING) {
                //show loading dialog
                CustomDialog().showLoadingDialog(dialog)
            }
            if (response.status == ChangePasswordState.ChangePasswordStatus.SUCCESS) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(context = requireContext(), "${response.message}")
                val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(loginIntent)
                requireActivity().finish()
            }
            if (response.status == ChangePasswordState.ChangePasswordStatus.FAILED) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
        }
    }
}