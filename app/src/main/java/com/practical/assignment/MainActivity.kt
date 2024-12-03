package com.practical.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.practical.presentation.di.EpisodeDetailsViewModelFactory
import com.practical.presentation.ui.theme.PracticalAssignmentTheme
import com.practical.presentation.factory.ViewModelFactory
import dagger.assisted.AssistedFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var factory: EpisodeDetailsViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        (application as RickAndMorty).appComponent.inject(this)
        setContent {
            PracticalAssignmentTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavGraph(navController, viewModelFactory,factory)

                }

            }
        }
    }
}
