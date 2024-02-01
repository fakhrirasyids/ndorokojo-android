package com.ndorokojo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ndorokojo.R
import com.ndorokojo.data.remote.ApiConfig
import com.ndorokojo.databinding.ActivityAuthBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.auth.login.LoginFragment
import com.ndorokojo.ui.main.MainActivity
import com.ndorokojo.ui.splash.SplashActivity
import com.ndorokojo.ui.updateprofile.UpdateProfileActivity
import com.ndorokojo.utils.Constants
import com.ndorokojo.utils.UserPreferences
import com.ndorokojo.utils.dataStore

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    private val authViewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance(
            Injection.provideApiService(this),
            Injection.provideUserPreferences(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.getAccessToken().observe(this@AuthActivity) { accessToken ->
            if (accessToken != UserPreferences.preferenceDefaultValue) {
                authViewModel.getIsProfileComplete()
                    .observe(this@AuthActivity) { isProfileComplete ->
                        if (isProfileComplete) {
                            val iSplash = Intent(this@AuthActivity, SplashActivity::class.java)
//                            iSplash.putExtra(Constants.USER_ACCESS_TOKEN, accessToken)
                            finishAffinity()
                            startActivity(iSplash)
                        } else {
                            val iUpdateProfile =
                                Intent(this@AuthActivity, UpdateProfileActivity::class.java)
//                            iUpdateProfile.putExtra(Constants.USER_ACCESS_TOKEN, accessToken)
                            iUpdateProfile.putExtra(Constants.IS_FROM_AUTH, true)
                            finishAffinity()
                            startActivity(iUpdateProfile)
                        }
                    }
            } else {
                if (savedInstanceState == null) {
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.auth_container, LoginFragment())
                        .commit()
                }
            }
        }
    }
}