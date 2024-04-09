//Rochana Godigamuwa 20221116
//Start Date 28.03.2024
//End Date 07.03.2024

package com.example.cw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cw1.ui.theme.CW1Theme
import java.util.Locale


class AdvancedLevel : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CW1Theme {

                // Parse JSON and get the list of countries
                val countries = parseCountriesJson(LocalContext.current)

                val (randomCountries, setRandomCountries) = remember {
                    mutableStateOf(
                        getRandThreeCountries(countries)
                    )
                }
                val guesses = remember { mutableStateListOf("", "", "") }

                val (attempts, setAttempts) = remember { mutableStateOf(0) }
                val (score, setScore) = remember { mutableStateOf(0) }
                val correctGuesses = remember { mutableStateListOf(false, false, false) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Name the Flag")
                            }
                        )
                    },

                    ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 60.dp)
                    )
                    {
                        // Display Score
                        Text(
                            text = "Score: $score",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = "Attempts: $attempts",
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        randomCountries.forEachIndexed { index, country ->
                            val flagDrawable = resources.getIdentifier(
                                country.first.lowercase(Locale.getDefault()),
                                "drawable",
                                packageName
                            )

                            // Display flag image
                            //Text(text = country.toString())
                            Image(
                                painter = painterResource(id = flagDrawable),
                                contentDescription = "Flag of ${country.second}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .size(120.dp) // Increase image size
                            )
                            TextField(
                                value = guesses[index],
                                onValueChange = { if (!correctGuesses[index]) guesses[index] = it },
                                enabled = !correctGuesses[index]
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                        }

                        Button(
                            onClick = {
                                if (attempts < 3) {
                                    var correctCount = 0
                                    guesses.forEachIndexed { index, guess ->
                                        if (guess.equals(
                                                randomCountries[index].second,
                                                ignoreCase = true
                                            )
                                        ) {
                                            correctGuesses[index] = true
                                            correctCount++
                                        }
                                    }
                                    setScore(score + correctCount)

                                    if (correctGuesses.all { it }) {
                                        // All correct, reset for next round
                                        setRandomCountries(getRandThreeCountries(countries))
                                        guesses.replaceAll { "" }
                                        correctGuesses.replaceAll { false }
                                        setAttempts(0)
                                    } else {
                                        setAttempts(attempts + 1)
                                    }
                                }
                                if (attempts >= 3 || correctGuesses.all { it }) {
                                    // Show correct answers or reset for next if all correct
                                    setRandomCountries(getRandThreeCountries(countries))
                                    guesses.replaceAll { "" }
                                    correctGuesses.replaceAll { false }
                                    setAttempts(0)
                                }
                            },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(if (correctGuesses.all { it } || attempts >= 3) "Next" else "Submit")
                        }

                    }
                }
            }
        }
    }
}

fun getRandThreeCountries(countries: List<Pair<String, String>>): List<Pair<String, String>> {
    // Ensure the list has at least 3 countries to avoid an infinite loop
    if (countries.size < 3) {
        throw IllegalArgumentException("The countries list must contain at least 3 countries.")
    }

    // Shuffle the list and take the first three elements to ensure randomness and uniqueness
    return countries.shuffled().take(3)
}