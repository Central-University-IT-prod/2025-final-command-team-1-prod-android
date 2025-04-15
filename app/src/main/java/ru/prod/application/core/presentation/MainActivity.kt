package ru.prod.application.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.prod.application.core.presentation.navigation.StackNavigator
import ru.prod.application.core.presentation.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(0,0))
        setContent {
            AppTheme {
                val viewModel: MainActivityViewModel = hiltViewModel()
                StackNavigator(authorized = viewModel.isAuthorized)
            }
        }
    }
}