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

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.zIndex
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource

@Composable
fun ToolPanelScreen() {
    val context = LocalContext.current
    val colorConfig = remember { loadColorsFromAssets(context) }
    val focusManager = LocalFocusManager.current
    
    // State for Language
    var isTamil by remember { mutableStateOf(false) }

    // State for selected Warp Data
    var selectedWarpData by remember(colorConfig) { 
        mutableStateOf(colorConfig.warpColors.firstOrNull()) 
    }
    
    val warpDropdownOptions = remember(colorConfig) {
        colorConfig.warpColors.map { WeftColor(it.name, it.hex.toColor()) }
    }

    val currentWeftList = remember(selectedWarpData) {
        selectedWarpData?.wefts?.map { WeftColor(it.name, it.hex.toColor()) } ?: emptyList()
    }
    
    val selectedCounts = remember(selectedWarpData) { mutableStateMapOf<Int, Int>() }

    // Root Container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBg)
            .clickable(
                onClick = { focusManager.clearFocus() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) 
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // 1. STICKY HEADER GROUP (zIndex 1 to stay on top)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    .background(PremiumSurface)
            ) {
                Sticky_Header_Warp(
                    selectedColor = selectedWarpData?.hex?.toColor() ?: Color.Gray,
                    selectedName = selectedWarpData?.name ?: "Select",
                    warpColors = warpDropdownOptions,
                    isTamil = isTamil,
                    onLanguageToggle = { isTamil = !isTamil },
                    onColorSelected = { _, name ->
                        selectedWarpData = colorConfig.warpColors.find { it.name == name }
                    }
                )
                
                Secondary_Header_Weft(isTamil = isTamil)
            }
            
            // 3. SCROLLABLE LIST
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 12.dp)
            ) {
                itemsIndexed(
                    items = currentWeftList,
                    key = { index, item -> "${selectedWarpData?.name}_${item.name}_$index" }
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
            
            // 4. FOOTER (zIndex 1 to stay on top of scroll)
            val totalCount by remember(selectedCounts) {
                derivedStateOf { selectedCounts.values.sum() }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PremiumBg)
                    .zIndex(1f)
            ) {
                Sticky_Footer_Total(
                    count = totalCount,
                    isTamil = isTamil
                )
            }
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
