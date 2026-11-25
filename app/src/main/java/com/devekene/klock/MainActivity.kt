package com.devekene.klock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devekene.klock.ui.MainScreen
import com.devekene.klock.ui.splash.SplashScreen
import com.devekene.klock.ui.theme.KlockTheme
import com.devekene.klock.viewmodel.KlockViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KlockTheme {
                var showSplash by remember { mutableStateOf(true) }
                val viewModel: KlockViewModel = viewModel()

                Crossfade(
                    targetState = showSplash,
                    animationSpec = tween(durationMillis = 350),
                    label = "splash_to_main"
                ) { isSplash ->
                    if (isSplash) {
                        SplashScreen(onTimeout = { showSplash = false })
                    } else {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
