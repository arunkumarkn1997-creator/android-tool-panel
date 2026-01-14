package com.example.toolpanel.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.toolpanel.ui.components.*
import com.example.toolpanel.ui.theme.ToolPanelTheme

val WeftColorsList = listOf(
    WeftColor("Green", Color(0xFF2CCA56)),
    WeftColor("Light green", Color(0xFFD1E231)),
    WeftColor("Green", Color(0xFF228B22)),
    WeftColor("Green", Color(0xFF32CD32)),
    WeftColor("Yellow", Color(0xFFFFF000)),
    WeftColor("Blue", Color(0xFF0096FF)),
    WeftColor("Blue", Color(0xFF0047AB))
)

// 6 Warp Colors as requested
val WarpColorsList = listOf(
    WeftColor("Red", Color(0xFFFF0000)),
    WeftColor("Green", Color(0xFF2CCA56)),
    WeftColor("Blue", Color(0xFF0096FF)),
    WeftColor("Yellow", Color(0xFFFFF000)),
    WeftColor("Orange", Color(0xFFFF7F00)),
    WeftColor("Purple", Color(0xFF800080))
)

@Composable
fun ToolPanelScreen() {
    // State for Language
    var isTamil by remember { mutableStateOf(false) }

    // State for Warp Header
    var selectedWarpColor by remember { mutableStateOf(WarpColorsList[1].color) } // Default Green
    var selectedWarpName by remember { mutableStateOf(WarpColorsList[1].name) }
    
    // State for multi-selection quantities
    val selectedCounts = remember { mutableStateMapOf<Int, Int>() }

    // Root Container - Premium Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA)) // Premium Bg
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // 1. STICKY HEADER
            Sticky_Header_Warp(
                selectedColor = selectedWarpColor,
                selectedName = selectedWarpName,
                warpColors = WarpColorsList,
                isTamil = isTamil,
                onLanguageToggle = { isTamil = !isTamil },
                onColorSelected = { color, name ->
                    selectedWarpColor = color
                    selectedWarpName = name
                }
            )
            
            // 2. SECONDARY HEADER
            Secondary_Header_Weft(isTamil = isTamil)
            
            // 3. SCROLLABLE LIST (Weight 1f to fill space)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                itemsIndexed(
                    items = WeftColorsList,
                    key = { index, item -> "${item.name}_$index" } // Use stable keys for better performance
                ) { index, item ->
                    Weft_Color_Row(
                        color = item.color,
                        name = item.name,
                        count = selectedCounts[index] ?: 0,
                        isTamil = isTamil,
                        onCountChange = { newCount ->
                            if (newCount > 0) {
                                selectedCounts[index] = newCount
                            } else {
                                selectedCounts.remove(index)
                            }
                        }
                    )
                }
            }
            
            // 4. FOOTER
            val totalCount by remember {
                derivedStateOf { selectedCounts.values.sum() }
            }
            
            Sticky_Footer_Total(
                count = totalCount,
                isTamil = isTamil
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun ToolPanelScreenPreview() {
    ToolPanelTheme {
        ToolPanelScreen()
    }
}
