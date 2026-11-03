package com.gio.sabor_android.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gio.sabor_android.R
import com.gio.sabor_android.ui.theme.AvocadoGreen
import com.gio.sabor_android.ui.theme.NarangaOrange
import com.gio.sabor_android.ui.theme.SaborWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AvocadoGreen),
        contentAlignment = Alignment.Center
    ) {
        // Styled circular white badge container that ensures your logo pops regardless of background
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(SaborWhite),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.mipmap.sabor_logo_asset),
                contentDescription = "Sabor Logo",
                modifier = Modifier.size(120.dp)
            )
        }
    }
}