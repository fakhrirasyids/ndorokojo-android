package com.ndorokojo.ui.auth.login

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ndorokojo.R
import com.ndorokojo.data.remote.ApiConfig
import com.ndorokojo.databinding.FragmentLoginBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.auth.AuthViewModel
import com.ndorokojo.ui.auth.AuthViewModelFactory
import com.ndorokojo.ui.auth.register.RegisterFragment
import com.ndorokojo.ui.splash.SplashActivity
import com.ndorokojo.ui.updateprofile.UpdateProfileActivity
import com.ndorokojo.utils.Constants
import com.ndorokojo.utils.Constants.alertDialogMessage
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import com.ndorokojo.utils.dataStore

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val authViewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance(
            Injection.provideApiService(requireContext()),
            Injection.provideUserPreferences(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupPlayAnimation()
        observeAll()

        setListeners()

        return binding.root
    }

    private fun observeAll() {
        authViewModel.apply {
            isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

            responseMessage.observe(viewLifecycleOwner) { message ->
                val builder = AlertDialog.Builder(requireContext())
                builder.setCancelable(false)

                with(builder)
                {
                    setMessage(message)
                    setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }

            isLoginPasswordFilled.observe(viewLifecycleOwner) {
                binding.edPasswordLayout.error = null
            }

            isLoginUsernameFilled.observe(viewLifecycleOwner) {
                binding.edUsernameLayout.error = null
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            edUsername.addTextChangedListener {
                if (binding.edUsername.text.toString().isEmpty()) {
                    authViewModel.isLoginUsernameFilled.postValue(false)
                } else {
                    authViewModel.isLoginUsernameFilled.postValue(true)
                }
            }

            edPassword.addTextChangedListener {
                if (binding.edPassword.text.toString().isEmpty()) {
                    authViewModel.isLoginPasswordFilled.postValue(false)
                } else {
                    authViewModel.isLoginPasswordFilled.postValue(true)
                }
            }

            btnLogin.setOnClickListener {
                if (isValid()) {
                    Log.e("LoginFRRagment", "setListeners: SAtu")

                    authViewModel.loginUser(edUsername.text.toString(), edPassword.text.toString())
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    authViewModel.isLoading.postValue(true)
                                }

                                is Result.Success -> {
                                    authViewModel.isLoading.postValue(false)
                                    authViewModel.savePreferences(
                                        accessToken = result.data.accessToken.toString(),
                                        username = result.data.farmer?.username.toString(),
                                        fullname = result.data.farmer?.fullname.toString(),
                                        email = result.data.farmer?.email.toString(),
                                        code = result.data.farmer?.code.toString(),
                                        phone = result.data.farmer?.phone?.toString() ?: "",
                                        address = result.data.farmer?.address.toString() ?: "",
                                        occupation = result.data.farmer?.occupation.toString()
                                            ?: "",
                                        gender = result.data.farmer?.gender.toString() ?: "",
                                        age = result.data.farmer?.age.toString() ?: "",
                                        kelompokTernak = result.data.farmer?.kelompokTernak.toString()
                                            ?: "",
                                        provinceId = result.data.farmer?.provinceId.toString() ?: "",
                                        regencyId = result.data.farmer?.regencyId.toString() ?: "",
                                        districtId = result.data.farmer?.districtId.toString() ?: "",
                                        villageId = result.data.farmer?.villageId.toString() ?: "",
                                        isProfileComplete = result.data.farmer?.isProfileComplete!!
                                    )

                                    authViewModel.getIsProfileComplete()
                                        .observe(viewLifecycleOwner) { isProfileComplete ->
//                                            Log.e(
//                                                "LoginFRRagment",
//                                                "setListeners: $isProfileComplete",
//                                            )
                                            if (!isProfileComplete) {
                                                val iUpdateProfile =
                                                    Intent(
                                                        requireContext(),
                                                        UpdateProfileActivity::class.java
                                                    )
//                            iUpdateProfile.putExtra(Constants.USER_ACCESS_TOKEN, accessToken)
                                                iUpdateProfile.putExtra(
                                                    Constants.IS_FROM_AUTH,
                                                    true
                                                )
                                                requireActivity().finishAffinity()
                                                startActivity(iUpdateProfile)
                                            }
                                        }
                                }

                                is Result.Error -> {
                                    Log.e("LoginFRRagment", "setListeners: ${result.error}")

                                    authViewModel.isLoading.postValue(false)
                                    authViewModel.responseMessage.postValue(result.error)
                                    alertDialogMessage(requireContext(), result.error)
                                }
                            }
                        }
                }
            }

            btnRegister.setOnClickListener { changeToRegister() }
        }
    }

    private fun changeToRegister() {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.auth_container,
                RegisterFragment(),
                RegisterFragment::class.java.simpleName
            )
            commit()
        }
    }

    private fun isValid() = if (binding.edUsername.text.isNullOrEmpty()
    ) {
//        binding.edUsernameLayout.error = "Masukkan username dengan benar!"
        alertDialogMessage(requireContext(),"Masukkan username dengan benar!")
        false
    } else if (binding.edPassword.text.isNullOrEmpty()) {
//        binding.edPasswordLayout.error = "Masukkan password dengan benar!"
        alertDialogMessage(requireContext(),"Masukkan password dengan benar!")
        false
    } else {
        true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressbar.isVisible = isLoading
            btnLogin.isVisible = !isLoading
            edUsername.isEnabled = !isLoading
            edPassword.isEnabled = !isLoading
        }
    }

    @SuppressLint("Recycle")
    private fun setupPlayAnimation() {
        val logo = ObjectAnimator.ofFloat<View>(binding.ivLogo, View.TRANSLATION_X, -30f, 30f)
        logo.duration = 1500
        logo.repeatCount = ObjectAnimator.INFINITE
        logo.repeatMode = ObjectAnimator.REVERSE

        val title: Animator =
            ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(200)
        val username: Animator =
            ObjectAnimator.ofFloat(binding.edUsernameLayout, View.ALPHA, 1f).setDuration(200)
        val password: Animator =
            ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(200)
        val button: Animator =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(200)
        val layoutText: Animator =
            ObjectAnimator.ofFloat(binding.layoutText, View.ALPHA, 1f).setDuration(200)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(title, username, password, button, layoutText)
        animatorSet.startDelay = 200
        animatorSet.start()
    }
}