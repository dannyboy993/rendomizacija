package com.example.randomteamskotlin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomteamskotlin.ui.theme.RandomTeamsKotlinTheme
import kotlin.random.Random

@Composable
fun SecondScreen() {
    val context = LocalContext.current
    val players: MutableList<Player> = getPlayersFromSharedPreferences(context) as MutableList<Player>
    //val players = mutableListOf<Player>(Player("Dajanj", 2), Player("Dajanj", 2), Player("Dajanj", 2),Player("Dajanj", 2),Player("Dajanj", 2),Player("Dajanj", 2),Player("Dajanj", 2),Player("Dajanj", 2))
    players.sortByDescending { it.position }
    val team1 = mutableListOf<String>()
    val team2 = mutableListOf<String>()

    val startColor = Color(0xFF2196F3) // Plava boja
    val endColor = Color(0xFFF44336) // Crvena boja

    for (i in 0 until players.size step 2) {
        val random = Random.nextInt(2)
        if (i == players.size - 1) {
            if (random == 0) {
                team1.add(players[i].name)
            } else {
                team2.add(players[i].name)
            }
            break
        }
        if (random == 0) {
            team1.add(players[i].name)
            team2.add(players[i + 1].name)
        } else {
            team2.add(players[i].name)
            team1.add(players[i + 1].name)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.verticalGradient(colors = listOf(startColor, endColor)))) {
        Team(team1, 1)
        Team(team2, 2)
    }
}

@Composable
fun Team (team: List<String>, teamNumber:Int) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val halfScreenHeight = with(LocalDensity.current) { screenHeight / 2 }
    Column(modifier = Modifier
        .height(halfScreenHeight)
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(text = "Tim $teamNumber:", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Column(modifier = Modifier.fillMaxSize() // workaround for custom border
            .padding(vertical = 10.dp, horizontal = 25.dp)
            .border(3.dp, SolidColor(Color.White), shape = RoundedCornerShape(40.dp))) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ){
                items(team) {item ->
                    TeamItem(teamItem = item)
                }
            }
        }
    }
}

@Composable
fun TeamItem(teamItem: String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center
        ) {
        Text(text = teamItem, fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun PreviewScreenContent() {
    SecondScreen()
}