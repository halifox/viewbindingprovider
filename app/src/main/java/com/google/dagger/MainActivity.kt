package com.google.dagger

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.dagger.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var nsdManager: NsdManager

    @Inject
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nsdManager.discoverServices("_video_sync_c._tcp.", NsdManager.PROTOCOL_DNS_SD, object : NsdManager.DiscoveryListener {
            override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {

            }

            override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {

            }

            override fun onDiscoveryStarted(serviceType: String?) {

            }

            override fun onDiscoveryStopped(serviceType: String?) {

            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo?) {

            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo?) {

            }
        })
    }
}