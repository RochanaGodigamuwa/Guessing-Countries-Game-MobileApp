//Rochana Godigamuwa 20221116
//Start Date 28.03.2024
//End Date 05.03.2024

package com.example.cw1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cw1.ui.theme.CW1Theme
import org.json.JSONObject


class GuessTheCountry : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CW1Theme {
                //---------------------------------------------------------------

                // Parse JSON and get the list of countries
                val countries = parseCountriesJson(LocalContext.current)

                // Get a random country from the list
//                var randomCountry = getRandomCountry(countries)
                var randCountry by rememberSaveable {
                    mutableStateOf(getRandCountry(countries))
                }

                var (randCountryNo, randCountryName) = randCountry


//

                var guessedAns by rememberSaveable {
                    mutableStateOf(false)
                }

                var buttonState by rememberSaveable {
                    mutableStateOf(false)
                }

                //var isSelected by remember { mutableStateOf(false) }
                var dialogBox by remember { mutableStateOf(false) }

                if (dialogBox) {
                    MinimalDialog(
                        answerGiven = if (guessedAns) "CORRECT!" else "WRONG!",
                        answer = randCountryName,
                        answerColor = if (guessedAns) Color.Green else Color.Red,
                        onDismissRequest = { dialogBox = false })
                }

                val countryNamePicked = remember { mutableStateOf<String?>(null) }



                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Guess The Country")
                            }
                        )
                    },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    ) {


                        Log.d("test", randCountryName.toString())
                        //------------------------------------------
                        // Text(randCountryName.toString())


                        val context = LocalContext.current

                        val randFlagDrawable = context.resources.getIdentifier(
                            randCountryNo?.lowercase(),
                            "drawable",
                            context.packageName
                        )

                        //Text(randomFlagDrawable.toString())

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )

                        {
                            Image(
                                painter = painterResource(id = randFlagDrawable),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .aspectRatio(16f / 9f)
                            )
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                        ) {
                            items(countries) { (code, name) ->

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(2.dp)
                                        //.background(if (isSelected) Color.Blue else Color.White)
                                        .clickable {
                                            countryNamePicked.value = name
                                        },
                                    border = if (countryNamePicked.value == name) BorderStroke(
                                        2.dp,
                                        Color.Red
                                    ) else null,
                                ) {
                                    Text(
                                        text = (name),
                                        modifier = Modifier
                                            .padding(20.dp),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp)
                        )
                        {
                            Button(onClick = {
                                // Handle correct selection
                                if (countryNamePicked.value == randCountryName) {
                                    guessedAns = true
                                }

                                dialogBox = true
                                buttonState = !buttonState

                                //to reset values
                                if (!buttonState) {
                                    randCountry = getRandCountry(countries)
                                    dialogBox = false
                                    guessedAns = false
                                }


                            }) {
                                Text(if (buttonState) "Next" else "Submit")
                            }

                        }
                    }
                }
            }

        }
    }
}


// Sample Data Class to hold country information
data class Country(val code: String, val name: String)

// Function to parse JSON and return a list of countries
fun parseCountriesJson(context: Context): MutableList<Pair<String, String>> {
    val countriesList = mutableListOf<Pair<String, String>>()
    val jsonString = context.assets.open("countries.json").bufferedReader().use { it.readText() }
    val jsonObject = JSONObject(jsonString)
    val keys = jsonObject.keys()
    while (keys.hasNext()) {
        val key = keys.next()
        val value = jsonObject.getString(key)
        countriesList.add(Pair(key, value))
    }
    return countriesList
}

fun getRandCountry(countries: MutableList<Pair<String, String>>): Pair<String, String> {
     if (countries.isNotEmpty()) {
             return countries.random()
     }// or handle the case where the list is empty
    return TODO("List of countries empty")
}


@Composable
fun MinimalDialog(answerGiven: String, answer: String, answerColor: Color, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                //.height(160.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        )
        {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = answerGiven,
                    color = answerColor,
                    fontSize = 50.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = answer,
                    color = Color.Blue,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


