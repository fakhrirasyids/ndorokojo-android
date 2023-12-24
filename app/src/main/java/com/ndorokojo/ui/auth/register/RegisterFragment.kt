package com.ndorokojo.ui.auth.register

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.ndorokojo.R
import com.ndorokojo.data.remote.ApiConfig
import com.ndorokojo.databinding.FragmentRegisterBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.auth.AuthViewModel
import com.ndorokojo.ui.auth.AuthViewModelFactory
import com.ndorokojo.ui.auth.login.LoginFragment
import com.ndorokojo.utils.Constants
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import com.ndorokojo.utils.dataStore

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

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
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
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

            isRegisterUsernameFilled.observe(viewLifecycleOwner) {
                binding.edUsernameLayout.error = null
            }

            isRegisterFullnameFilled.observe(viewLifecycleOwner) {
                binding.edNameLayout.error = null
            }

            isRegisterEmailFilled.observe(viewLifecycleOwner) {
                binding.edEmailLayout.error = null
            }

            isRegisterPasswordFilled.observe(viewLifecycleOwner) {
                binding.edPasswordLayout.error = null
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            edUsername.addTextChangedListener {
                if (binding.edUsername.text.toString().isEmpty()) {
                    authViewModel.isRegisterUsernameFilled.postValue(false)
                } else {
                    authViewModel.isRegisterUsernameFilled.postValue(true)
                }
            }

            edName.addTextChangedListener {
                if (binding.edName.text.toString().isEmpty()) {
                    authViewModel.isRegisterFullnameFilled.postValue(false)
                } else {
                    authViewModel.isRegisterFullnameFilled.postValue(true)
                }
            }

            edEmail.addTextChangedListener {
                if (binding.edEmail.text.toString().isEmpty()) {
                    authViewModel.isRegisterEmailFilled.postValue(false)
                } else {
                    authViewModel.isRegisterEmailFilled.postValue(true)
                }
            }


            edPassword.addTextChangedListener {
                if (binding.edPassword.text.toString().isEmpty()) {
                    authViewModel.isRegisterPasswordFilled.postValue(false)
                } else {
                    authViewModel.isRegisterPasswordFilled.postValue(true)
                }
            }


            btnRegister.setOnClickListener {
                if (isValid()) {
                    authViewModel.registerUser(
                        edUsername.text.toString(),
                        edName.text.toString(),
                        edEmail.text.toString(),
                        edPassword.text.toString()
                    )
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    authViewModel.isLoading.postValue(true)
                                }

                                is Result.Success -> {
                                    authViewModel.isLoading.postValue(false)

                                    val builder = AlertDialog.Builder(requireContext())
                                    builder.setCancelable(false)

                                    with(builder)
                                    {
                                        setMessage("Sukses register akun, silahkan Log In!")
                                        setPositiveButton("OK") { _, _ ->
                                            changeToLogin()
                                        }
                                        show()
                                    }
                                }

                                is Result.Error -> {
                                    authViewModel.isLoading.postValue(false)
                                    authViewModel.responseMessage.postValue(result.error)
                                }
                            }
                        }
                }
            }

            btnLogin.setOnClickListener { changeToLogin() }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                changeToLogin()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun changeToLogin() {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.auth_container,
                LoginFragment(),
                LoginFragment::class.java.simpleName
            )
            commit()
        }
    }

    private fun isValid() = if (binding.edUsername.text.isNullOrEmpty()) {
        Constants.alertDialogMessage(requireContext(), "Masukkan username dengan benar!")
//        binding.edUsernameLayout.error = "Masukkan username dengan benar!"
        false
    } else if (binding.edUsername.text.toString().any { it.isUpperCase() }) {
        Constants.alertDialogMessage(requireContext(), "Username tidak boleh mengandung huruf kapital!")
//        binding.edUsernameLayout.error = "Masukkan username dengan benar!"
        false
    } else if (binding.edUsername.text.toString().any { it.isWhitespace() }) {
        Constants.alertDialogMessage(requireContext(), "Username tidak boleh mengandung Spasi!")
//        binding.edUsernameLayout.error = "Masukkan username dengan benar!"
        false
    } else if (binding.edName.text.isNullOrEmpty()) {
        Constants.alertDialogMessage(requireContext(), "Masukkan nama dengan benar!")

//        binding.edNameLayout.error = "Masukkan nama dengan benar!"
        false
    } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.text.toString())
            .matches() || binding.edEmail.text.isNullOrEmpty()
    ) {
        Constants.alertDialogMessage(requireContext(), "Masukkan email dengan benar!")

//        binding.edEmailLayout.error = "Masukkan email dengan benar!"
        false

    } else if (binding.edPassword.text.isNullOrEmpty()) {
        Constants.alertDialogMessage(requireContext(), "Masukkan password dengan benar!")

//        binding.edPasswordLayout.error = "Masukkan password dengan benar!"
        false
    } else if (binding.edPassword.text.toString().length < 8) {
        Constants.alertDialogMessage(requireContext(), "Password harus memiliki minimal 8 huruf!")

//        binding.edPasswordLayout.error = "Password harus memiliki minimal 8 huruf!"
        false
    } else {
        true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressbar.isVisible = isLoading
            btnRegister.isVisible = !isLoading
            edUsername.isEnabled = !isLoading
            edName.isEnabled = !isLoading
            edEmail.isEnabled = !isLoading
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
        val fullname: Animator =
            ObjectAnimator.ofFloat(binding.edNameLayout, View.ALPHA, 1f).setDuration(200)
        val email: Animator =
            ObjectAnimator.ofFloat(binding.edEmailLayout, View.ALPHA, 1f).setDuration(200)
        val password: Animator =
            ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(200)
        val button: Animator =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(200)
        val layoutText: Animator =
            ObjectAnimator.ofFloat(binding.layoutText, View.ALPHA, 1f).setDuration(200)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(title, username, fullname, email, password, button, layoutText)
        animatorSet.startDelay = 200
        animatorSet.start()
    }
}