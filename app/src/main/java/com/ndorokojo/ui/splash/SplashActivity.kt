package com.ndorokojo.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ndorokojo.databinding.ActivitySplashBinding
import com.ndorokojo.ui.main.MainActivity
import com.ndorokojo.ui.profilesettings.ProfileSettingsActivity
import com.ndorokojo.utils.Constants

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
//    private val accessToken by lazy { intent.getStringExtra(Constants.USER_ACCESS_TOKEN) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val iMain =
                Intent(this@SplashActivity, MainActivity::class.java)
//            iMain.putExtra(Constants.USER_ACCESS_TOKEN, accessToken)
            finishAffinity()
            startActivity(iMain)
        }, Constants.SPLASH_MAIN_TIMEOUT)
    }
}