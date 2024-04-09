//Rochana Godigamuwa 20221116
//Start Date 28.03.2024
//End Date 05.03.2024

package com.example.cw1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cw1.ui.theme.CW1Theme


class GuessHints : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CW1Theme {

                // Parse JSON and get the list of countries
                val countries = parseCountriesJson(LocalContext.current)

                // Get a random country from the list
                var randCountry by rememberSaveable {
                    mutableStateOf(getRandCountry(countries))
                }

                var (randCountryNo, randCountryName) = randCountry

                var dashes = remember { (mutableStateListOf<Char>()) }
                if (dashes.size == 0) repeat(randCountryName.length) {
                    dashes.add('-')
                }

                // Input text state
                var inputText by remember { mutableStateOf("") }

                var availableChances by remember {
                    mutableStateOf(3)
                }

                //to get answer is correct state
                var moveToNext by rememberSaveable {
                    mutableStateOf(false)
                }

                //to get answer is correct state
                var answer by rememberSaveable {
                    mutableStateOf(false)
                }

                var resetStates by rememberSaveable {
                    mutableStateOf(false)
                }

                var dialogBox by remember { mutableStateOf(false) }

                if (dialogBox) {
                    MinimalDialog(
                        answerGiven = if (answer) "CORRECT!" else "WRONG!",
                        answer = randCountryName,
                        answerColor = if (answer) Color.Green else Color.Red,
                        onDismissRequest = { dialogBox = false })
                }


                //-----------------------------------------------------------------

                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Guess Hints")
                            }
                        )
                    },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {

//                        Text(randCountryName?.name.toString())
//                        Text(randCountry?.code.toString())

                        Log.d("test", randCountryName.toString())
                        //------------------------------------------

                        //Correct country name displayed here
                        //Text(randCountryName.toString())


                        val context = LocalContext.current

                        val randomFlagDrawable = context.resources.getIdentifier(
                            randCountryNo?.lowercase(),
                            "drawable",
                            context.packageName
                        )

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = dashes.joinToString(" "),
                                fontSize = 20.sp,
                                //letterSpacing = 1.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }


                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )

                        {
                            Image(
                                painter = painterResource(id = randomFlagDrawable),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(300.dp)
                                    .border(BorderStroke(1.dp, Color.Black)),
                                contentScale = ContentScale.Fit
                            )
                        }

                        // Text input and submit button
                        // Text input and submit button
                        Row()
                        {
                            TextField(
                                modifier = Modifier
                                    .padding(5.dp),
                                value = inputText,
                                onValueChange = { inputText = it },
                                label = { Text("Enter a Letter and Submit") }
                            )
                            Log.d("test", availableChances.toString())
                            Button(onClick = {
                                //run submitGuess function
                                enteredGuess(
                                    randCountryName,
                                    dashes,
                                    inputText,
                                    availableChances = { availableChances = availableChances - 1 },
                                )

                                if (moveToNext) {
                                    randCountry = getRandCountry(countries)
                                    moveToNext = false
                                    availableChances = 3
                                    dashes.clear()
                                    repeat(randCountry.second.length) {
                                        dashes.add('-')
                                    }
                                }

                                //clear text field
                                inputText = ""

                                //to check full correct guess
                                if (dashes.all { it != '-' }) {
                                    answer = true
                                    moveToNext = true
                                    dialogBox = true
                                    resetStates = true
                                }

                                //when loose all guess
                                else if (availableChances == 0) {
                                    answer = false
                                    moveToNext = true
                                    dialogBox = true
                                }

//                                submitButtonState = !submitButtonState
//
//                                //to reset values
//                                if (!submitButtonState) {
//                                    randomCountry = getRandomCountry(countries)
//                                    showDialog = false
//                                    answer = false
//                                    dashes.clear()
//                                }

                                /*
                                if (changeToNext == true) {
                                    changeToNext = false
                                    guessCounter == 3
                                    dashes.clear()
                                }
                                */

                            })
                            {
                                Text(if (moveToNext) "Next" else "Submit")
                            }
                        }

                        Text(text = "Remaining guesses: ${availableChances.toString()}")

                        Log.d("test", answer.toString())

                    }
                }
            }

        }
    }
}


// Function to submit user's guess and update dashes
fun enteredGuess(
    countryName: String,
    dashes: MutableList<Char>,
    guess: String,
    availableChances: () -> Unit
) {
    //to check single character
    if (guess.length == 1) {
        val guessedChar = guess[0].lowercaseChar() // Convert to lowercase for case insensitivity
        var updatedDashes = false
        for (i in countryName.indices) {
            if (countryName[i].lowercaseChar() == guessedChar && dashes[i] == '-') {
                dashes[i] = countryName[i]
                updatedDashes = true

            }
        }
        if (!updatedDashes) {
            availableChances()//run guess counter and reduce guesses
        }
    }
}
