package com.gio.sabor_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gio.sabor_android.ui.navigation.NavGraph
import com.gio.sabor_android.ui.theme.Sabor_AndroidTheme
import com.gio.sabor_android.ui.theme.Charcoal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sabor_AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Charcoal
                ) {
                    val navController = rememberNavController()

                    // Call your centralized NavGraph which handles Splash, Login, Register, and MainDashboard
                    NavGraph(navController = navController)
                }
            }
        }
    }
}