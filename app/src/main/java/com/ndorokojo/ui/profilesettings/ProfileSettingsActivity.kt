package com.ndorokojo.ui.profilesettings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.ndorokojo.R
import com.ndorokojo.data.remote.ApiConfig
import com.ndorokojo.databinding.ActivityProfileSettingsBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.auth.AuthActivity
import com.ndorokojo.ui.changepassword.ChangePasswordActivity
import com.ndorokojo.ui.updateprofile.UpdateProfileActivity
import com.ndorokojo.ui.updateprofile.UpdateProfileViewModel
import com.ndorokojo.ui.updateprofile.UpdateProfileViewModelFactory
import com.ndorokojo.utils.Constants
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import com.ndorokojo.utils.dataStore

class ProfileSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileSettingsBinding
//    private val accessToken by lazy { intent.getStringExtra(Constants.USER_ACCESS_TOKEN) }

    private val profileSettingsViewModel by viewModels<ProfileSettingsViewModel> {
        ProfileSettingsViewModelFactory.getInstance(
            Injection.provideApiService(this),
            Injection.provideUserPreferences(this)
        )
    }

    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inflater: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this@ProfileSettingsActivity)
            .setView(inflater.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(true)
        loadingDialog = loadingAlert.create()

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            btnChangeProfile.setOnClickListener {
                val iUpdateProfile =
                    Intent(this@ProfileSettingsActivity, UpdateProfileActivity::class.java)
//                iUpdateProfile.putExtra(Constants.USER_ACCESS_TOKEN, accessToken)
                startActivity(iUpdateProfile)
            }

            btnLogout.setOnClickListener {
                profileSettingsViewModel.logout().observe(this@ProfileSettingsActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            loadingDialog.show()
                        }

                        is Result.Success -> {
                            profileSettingsViewModel.clearPreferences()
                            loadingDialog.dismiss()
                            val builder = AlertDialog.Builder(this@ProfileSettingsActivity)
                            builder.setCancelable(false)

                            with(builder)
                            {
                                setMessage("Sukses Logout Akun!")
                                setPositiveButton("OK") { _, _ ->
                                    val iAuth = Intent(
                                        this@ProfileSettingsActivity,
                                        AuthActivity::class.java
                                    )
                                    finishAffinity()
                                    startActivity(iAuth)
                                }
                                show()
                            }
                        }

                        is Result.Error -> {
                            loadingDialog.dismiss()
                            val builder = AlertDialog.Builder(this@ProfileSettingsActivity)
                            builder.setCancelable(false)

                            with(builder)
                            {
                                setMessage("Gagal Logout Akun!")
                                setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                show()
                            }

                        }
                    }
                }

            }
        }
    }
}