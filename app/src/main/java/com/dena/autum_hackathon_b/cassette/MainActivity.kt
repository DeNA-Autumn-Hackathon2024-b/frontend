package com.dena.autum_hackathon_b.cassette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dena.autum_hackathon_b.cassette.feature.create.CreateScreenHost
import com.dena.autum_hackathon_b.cassette.feature.create.addsong.AddSongDialogScreenHost
import com.dena.autum_hackathon_b.cassette.feature.play.PlayScreenHost
import com.dena.autum_hackathon_b.cassette.ui.theme.CassetteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            CassetteTheme {
                NavHost(navController = navController, startDestination = "play") {
                    composable(route = "play") {
                        PlayScreenHost(
                            navigateToCreateScreen = {
                                navController.navigate("create")
                            }
                        )
                    }

                    composable(route = "create") { currentNavBackstackEntry ->
                        CreateScreenHost(
                            navController = navController,
                            navigateToAddSongDialog = {
                                navController.navigate("addsong")
                            },
                            navigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(route = "addsong") {
                        AddSongDialogScreenHost(
                            onSuccess = { cachedAudioFile, songText ->
                                navController.previousBackStackEntry?.run {
                                    savedStateHandle["cachedAudioFile"] = cachedAudioFile
                                    savedStateHandle["songText"] = songText
                                }
                                navController.popBackStack()
                            },
                            onCancel = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CassetteTheme {
        Greeting("Android")
    }
}