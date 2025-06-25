package com.albertrg.firebasecourse.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.albertrg.firebasecourse.ui.composables.SignBackground

@Composable
fun HomeScreen() {
    BackHandler(true) {}
    SignBackground()
}