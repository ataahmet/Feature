package com.github.app.ui.main.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.app.ui.base.ComposeBaseActivity
import com.github.app.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start
import kotlin.math.roundToInt

@AndroidEntryPoint
class SplashActivity : ComposeBaseActivity<SplashViewModel>() {
    private val generateVM: SplashViewModel by viewModels()

    override fun provideViewModel() = generateVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun Content() {
        var offsetX by remember { mutableFloatStateOf(0f) }
        var containerWidth by remember { mutableFloatStateOf(0f) }
        val buttonSizePx = 64.dp
        var completed by remember { mutableStateOf(false) }

        val fraction =
            if (containerWidth > 0f) {
                (offsetX / (containerWidth - 64f * 3)).coerceIn(0f, 1f)
            } else {
                0f
            }

        val startColor = Color(0xFFFEAC00) // yellow_sea
        val endColor = Color(0xFF3700B3) // primary_dark
        val buttonColor by animateColorAsState(
            targetValue =
                if (fraction >= 1f) {
                    endColor
                } else {
                    startColor.copy(
                        red = startColor.red + (endColor.red - startColor.red) * fraction,
                        green = startColor.green + (endColor.green - startColor.green) * fraction,
                        blue = startColor.blue + (endColor.blue - startColor.blue) * fraction,
                    )
                },
            animationSpec = tween(durationMillis = 100),
            label = "buttonColor",
        )

        LaunchedEffect(completed) {
            if (completed) gotoMainActivity()
        }

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .onGloballyPositioned { containerWidth = it.size.width.toFloat() },
        ) {
            Box(
                modifier =
                    Modifier
                        .offset { IntOffset(offsetX.roundToInt(), 0) }
                        .size(buttonSizePx)
                        .background(buttonColor)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (fraction >= 1f) {
                                        completed = true
                                    } else {
                                        offsetX = 0f
                                    }
                                },
                            ) { _, dragAmount ->
                                offsetX = (offsetX + dragAmount).coerceAtLeast(0f)
                                if (fraction >= 1f) completed = true
                            }
                        },
            )
        }
    }

    private fun gotoMainActivity() {
        start<LoginActivity>()
        finish()
    }
}
