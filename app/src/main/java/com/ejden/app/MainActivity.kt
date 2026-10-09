package com.ejden.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ejden.app.ui.EjdenRoot
import com.ejden.app.ui.theme.EjdenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            EjdenTheme {
                EjdenRoot()
            }
        }
    }
}
