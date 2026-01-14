package com.example.toolpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.toolpanel.ui.ToolPanelScreen
import com.example.toolpanel.ui.theme.ToolPanelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToolPanelTheme {
                ToolPanelScreen()
            }
        }
    }
}
