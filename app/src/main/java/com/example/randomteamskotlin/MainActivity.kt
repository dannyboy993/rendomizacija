package com.example.randomteamskotlin

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.randomteamskotlin.ui.theme.RandomTeamsKotlinTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomTeamsKotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainScreen.route ) {
        composable(MainScreen.route) {
            MainScreen(navController)
        }
        composable(SecondScreen.route) {
            SecondScreen()
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val players = remember {mutableStateListOf<Player>().apply {
        addAll(getPlayersFromSharedPreferences(context))
    }}

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
        ) {
        UpperPanel(players)
        Divider(color = Color.LightGray, thickness = 2.dp)
        MiddlePannel(players)
        Divider(color = Color.LightGray, thickness = 2.dp)
        LowerPannel(navController, context, players)
    }
}

@Composable
fun UpperPanel (players: MutableList<Player>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 30.dp)
    ) {
            Text(text = "Unesi igrače: ", fontSize = 18.sp, modifier = Modifier.padding(top = 10.dp))
            PlayerInputForm(players)
        }
            
    }

@Composable
fun MiddlePannel(players: MutableList<Player>){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.75f)
        .padding(horizontal = 30.dp)
    ) {

        itemsIndexed(players) {index, player ->
            PlayerItem(player= player, onDelete = {players.removeAt(index)})
        }
    }
}

@Composable
fun PlayerItem(player: Player, onDelete:(Player) -> Unit){
    Column {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = player.name, fontSize = 18.sp, modifier = Modifier.width(110.dp))
            Text(text = player.position.toString(), fontSize = 20.sp)
            IconButton(onClick = {
                onDelete(player)
            }) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    tint = Color(0xFFF44336)
                )
            }
        }
        Divider(color = Color.LightGray, thickness = 0.2.dp)
    }
}

@Composable
fun PlayerInputForm(players: MutableList<Player>) {
    val mcontext = LocalContext.current
    var playerName by remember { mutableStateOf("") }
    val focusRequester = remember {FocusRequester()}

    OutlinedTextField(
        value = playerName,
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
            //.padding(vertical = 10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done),
        onValueChange = { playerName = it },
        label = { Text("Ime") }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        var position by remember { mutableStateOf("")}

        OutlinedTextField(
            value = position,
            modifier = Modifier.fillMaxWidth(0.4f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = { position = it },
            label = { Text("Pozicija") }
        )
        Button(
                colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ),
            onClick = {
            if (playerName.isNotBlank() and position.isNotBlank() and position.all { char -> char.isDigit() }) {
                players.add(Player(playerName, position.toInt()))
                Toast.makeText(mcontext, playerName + " uspješno dodat.", Toast.LENGTH_SHORT).show()
                position = ""
                playerName = ""
                focusRequester.requestFocus()
            } else {
                Toast.makeText(mcontext, "Igrač mora imati ime i poziciju" , Toast.LENGTH_SHORT).show()
            }
        },
            modifier = Modifier
                .height(48.dp)
                .padding(top = 4.dp)
            ) {
            Text(text = "dodaj igrača".uppercase())
        }
    }
}

@Composable
fun LowerPannel(navController: NavHostController, context: Context, players: MutableList<Player>) {
    var showDialog by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        //.fillMaxHeight(0.5f)
        .padding(35.dp, 0.dp, 35.dp, 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                onClick = {
                savePlayersToSharedPreferences(context, players)
                navController.navigate(SecondScreen.route)
            },
                modifier = Modifier.height(48.dp)
                ) {
                Text(text = "potvrdi".uppercase())
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF44336),
                contentColor = Color.White
            ),
                onClick = {
                showDialog = showDialog.not()
            },
                modifier = Modifier
                    .height(48.dp)
                ) {
                Text(text = "izbriši sve".uppercase())
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Jeste li sigurni da želite da izbrišete sve igrače?") },
            text = { Text("Ova radnja ne može biti poništena") },
            confirmButton = {
                TextButton(onClick = {
                    players.clear()
                    showDialog = false
                }) {
                    Text("Izbriši sve".uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Poništi".uppercase())
                }
            },
        )
    }
}

fun savePlayersToSharedPreferences(context: Context, players: List<Player>) {
    val sharedPreferences = context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val playersJson = gson.toJson(players)
    editor.putString("players", playersJson)
    editor.apply()
}

fun getPlayersFromSharedPreferences(context: Context): List<Player> {
    val sharedPreferences = context.getSharedPreferences("player_prefs", Context.MODE_PRIVATE)
    val playersJson = sharedPreferences.getString("players", null)
    val gson = Gson()
    val type = object : TypeToken<List<Player>>() {}.type
    return gson.fromJson(playersJson, type) ?: emptyList()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomTeamsKotlinTheme {
        Navigation()
    }
}