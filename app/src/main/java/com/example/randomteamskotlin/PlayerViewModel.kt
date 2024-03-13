package com.example.randomteamskotlin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PlayerViewModel: ViewModel() {
    val players: MutableState<List<Player>> = mutableStateOf(emptyList())
}

