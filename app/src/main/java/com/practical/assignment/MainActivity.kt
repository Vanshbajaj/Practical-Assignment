package com.practical.assignment


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.practical.assignment.ui.theme.practicalAssignmentTheme
import com.practical.presentation.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        (application as RickAndMorty).appComponent.inject(this)
        setContent {
            practicalAssignmentTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    appNavGraph(navController, viewModelFactory)
                }

            }
        }
    }
}



