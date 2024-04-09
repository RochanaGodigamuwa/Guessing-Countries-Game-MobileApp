//Rochana Godigamuwa 20221116
//Start Date 28.03.2024
//End Date 04.03.2024

package com.example.cw1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cw1.ui.theme.CW1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CW1Theme {
                //Text(text = "Rochana's Game")
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Button(
                        onClick = {
                            Intent(applicationContext, GuessTheCountry::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(16.dp)
                    ) {
                        Text(text = "Guess the Country",
                            fontSize = 30.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            Intent(applicationContext, GuessHints::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(16.dp)
                    ) {
                        Text(text = "Guess Hints",
                            fontSize = 30.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            Intent(applicationContext, GuessTheFlag::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(16.dp)

                    ) {
                        Text(text = "Guess the Flag",
                            fontSize = 30.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            Intent(applicationContext, AdvancedLevel::class.java).also {
                                startActivity(it)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(16.dp)
                    ) {
                        Text(text = "Advanced Level",
                            fontSize = 30.sp)
                    }
                }
            }
        }
    }
}

