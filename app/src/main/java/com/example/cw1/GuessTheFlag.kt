//Rochana Godigamuwa 20221116
//Start Date 28.03.2024
//End Date 06.03.2024

package com.example.cw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cw1.ui.theme.CW1Theme
import java.util.Locale


class GuessTheFlag : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CW1Theme {
                // Assume CW1Theme is your Compose Theme

                // Parse JSON and get the list of countries
                val countries = parseCountriesJson(LocalContext.current)

                // Get three random countries from the list
                var randCountries by rememberSaveable {
                    mutableStateOf(
                        getRandThreeCountries(countries)
                    )
                }

                var correctCountry by remember {
                    mutableStateOf(randCountries.random())
                }
                var (correctCountryCode, correctCountryName) = correctCountry

                var dialogBox by remember { mutableStateOf(false) }
                var answerCorrect by remember { mutableStateOf(false) }

                var answer by rememberSaveable {
                    mutableStateOf(false)
                }

                var nextButtonState by rememberSaveable {
                    mutableStateOf(false)
                }

                if (dialogBox) {
                    MinimalDialog(
                        answer = if (answerCorrect) "CORRECT!" else "WRONG!",
                        answerGiven = correctCountryName,
                        answerColor = if (answerCorrect) Color.Green else Color.Red,
                        onDismissRequest = {
                            dialogBox = false
                            // Prepare for the next round
                            //randomCountries = getRandomThreeCountries(countries)
                            //correctCountry = randomCountries.random()
                        })
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Guess The Flag")
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
                    ) {


                        Row() {
                            Text(
                                correctCountryName,
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }


                        randCountries.forEach { country ->
                            val flagDrawableId = resources.getIdentifier(
                                country.first.lowercase(Locale.getDefault()),
                                "drawable",
                                packageName
                            )


                            Spacer(modifier = Modifier.height(55.dp))

                            //To display correct country name above each country
                            //Text(text = country.toString())


                            Image(
                                painter = painterResource(id = flagDrawableId),
                                contentDescription = "Flag of ${country.second}",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .aspectRatio(16f / 9f)
                                    .size(100.dp)
                                    .clickable {
                                        answerCorrect = country == correctCountry
                                        dialogBox = true

                                    }
                            )
                        }


                        Button(
                            onClick = {


                                nextButtonState = !nextButtonState


                                //to reset values
                                if (!nextButtonState) {
                                    randCountries = getRandThreeCountries(countries)
                                    correctCountry = randCountries.random()
                                    dialogBox = false
                                    answerCorrect = false
                                }


                            },
                            modifier = Modifier
                                .padding(top = 16.dp)
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    }
}

