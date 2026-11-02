package com.ruangtunggu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ruangtunggu.ui.navigation.RuangTungguNavGraph
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.RuangTungguTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RuangTungguTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Starts at Splash which auto-navigates to Onboarding, Login, or Home
                    RuangTungguNavGraph(
                        navController = navController,
                        startDestination = Routes.Splash
                    )
                }
            }
        }
    }
}
