package com.myapp.spotzify


import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feature.player.ui.screen.PlayerScreen

import com.feature.search.ui.screen.SearchScrren
import com.feature.search.ui.viewmodel.SearchViewmodel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.myapp.spotzify.Mapper.toPlayer
import com.myapp.spotzify.ui.theme.SpotzifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @SuppressLint("InlinedApi")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotzifyTheme {
                val permissionState =
                    rememberMultiplePermissionsState(
                        listOf(
                            Manifest.permission.POST_NOTIFICATIONS,

                            )

                    )

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    val searchViewmodel: SearchViewmodel = hiltViewModel()



                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "search") {
                        composable("search") {
                            SearchScrren(viewModel = searchViewmodel, onSongClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "song",
                                    it.toPlayer()
                                )

                                navController.navigate("player")
                            })
                        }
                        composable("player") {
                            val song =
                                navController.previousBackStackEntry?.savedStateHandle?.get<com.feature.player.ui.model.Data>(
                                    "song"
                                )
                            if (song != null) {
                                PlayerScreen(
                                    song = song,

                                )
                            }
                        }
                    }
                }
            }
        }
    }


}