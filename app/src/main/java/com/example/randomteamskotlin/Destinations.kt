package com.example.randomteamskotlin

interface Destinations {
    val route: String
}

object MainScreen : Destinations {
    override val route = "MainScreen"
}

object SecondScreen : Destinations {
    override val route = "SecondScreen"
}