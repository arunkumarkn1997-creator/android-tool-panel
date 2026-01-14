package com.example.toolpanel.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.zIndex
import androidx.compose.foundation.text.KeyboardActions

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import android.content.Context

// --- Data Models ---
@Serializable
data class ColorData(val name: String, val hex: String)

@Serializable
data class WarpColorData(
    val name: String,
    val hex: String,
    val wefts: List<ColorData>
)

@Serializable
data class ColorConfig(
    val warpColors: List<WarpColorData>
)

@Immutable
data class WeftColor(val name: String, val color: Color)

// --- Helper for Hex to Color ---
fun String.toColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color.Gray
    }
}

// --- JSON Loader ---
fun loadColorsFromAssets(context: Context): ColorConfig {
    return try {
        val jsonString = context.assets.open("colors.json").bufferedReader().use { it.readText() }
        Json.decodeFromString<ColorConfig>(jsonString)
    } catch (e: Exception) {
        ColorConfig(emptyList())
    }
}

// --- Premium Design Constants ---
val PremiumBg = Color(0xFFF5F7FA)
val PremiumSurface = Color(0xFFFFFFFF)
val PremiumTextPrimary = Color(0xFF1A1C1E)
val PremiumTextSecondary = Color(0xFF6C757D)
val BrandGreen = Color(0xFF2CCA56)

// --- Translation Helper ---
val Translations = mapOf(
    "Warp colour" to "வார் வண்ணங்கள்", // APPROX for Warp
    "Suitable Weft colours" to "பொருத்தமான ஊடு வண்ணங்கள்",
    "Total selected" to "மொத்தம்", // Shortened
    "Search..." to "தேடு...",
    "No colors found" to "வண்ணங்கள் இல்லை",
    "Green" to "பச்சை",
    "Light Green" to "இளம்பச்சை",
    "Blue" to "நீலம்",
    "Red" to "சிவப்பு",
    "Orange" to "ஆரஞ்சு",
    "Yellow" to "மஞ்சள்",
    "Purple" to "ஊதா",
    "Pink" to "இளஞ்சிவப்பு",
    "Brown" to "பழுப்பு",
    "Black" to "கருப்பு",
    "White" to "வெள்ளை",
    "Grey" to "சாம்பல்",
    "Cyan" to "சாயல்",
    "Ivory" to "தந்தம்",
    "Lemon" to "எலுமிச்சை",
    "Grey" to "சாம்பல்",
    "Lavendar" to "லாவெண்டர்",
    "Baba" to "பாபா",
    "L Anandha" to "இள ஆனந்தா",
    "D Anandha" to "கரும் ஆனந்தா",
    "Voilet" to "ஊதா",
    "Coffee" to "காபி",
    "Peacock" to "மயில்",
    "Sumathi" to "சுமதி",
    "Dark Grey" to "கருஞ்சாம்பல்",
    "Maroon" to "மெரூன்",
    "Majenta" to "மெஜந்தா",
    "Tussar" to "தசர்",
    "Rani" to "ராணி சிகப்பு",
    "Parrot green" to "கிளி பச்சை",
    "Gold" to "தங்கம்"
)

fun String.translate(isTamil: Boolean): String {
    if (!isTamil) return this
    return Translations[this] ?: this
}


@Composable
fun Sticky_Header_Warp(
    selectedColor: Color,
    selectedName: String,
    warpColors: List<WeftColor>,
    isTamil: Boolean,
    onLanguageToggle: () -> Unit,
    onColorSelected: (Color, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Elevated App Bar style
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp),
        color = PremiumSurface.copy(alpha = 0.98f), 
        shadowElevation = 8.dp, 
        border = BorderStroke(1.dp, Color(0xFFE2E8F0).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Selection Pill. 
            // Natural flexible width (Removed widthIn constraints)
            Surface(
                shape = RoundedCornerShape(50), 
                color = PremiumBg,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { expanded = true }
            ) {
                Row(
                    modifier = Modifier.padding(6.dp, 6.dp, 16.dp, 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(selectedColor)
                            .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    val displayName = remember(selectedName, isTamil) { selectedName.translate(isTamil) }
                    Text(
                        text = displayName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PremiumTextPrimary,
                    )
                    
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select",
                        tint = PremiumTextSecondary,
                        modifier = Modifier.size(20.dp).padding(start = 4.dp)
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { 
                            expanded = false 
                            searchQuery = "" 
                        },
                        modifier = Modifier
                            .background(PremiumSurface)
                            .heightIn(max = 240.dp)
                            .width(220.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search...".translate(isTamil), fontSize = 14.sp) },
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        val filteredColors = warpColors.filter { 
                            it.name.translate(isTamil).contains(searchQuery, ignoreCase = true) 
                        }
                        if (filteredColors.isEmpty()) {
                             DropdownMenuItem(
                                text = { Text("No colors found".translate(isTamil), color = Color.Gray, fontSize = 14.sp) },
                                onClick = { }
                            )
                        }
                        filteredColors.forEach { item ->
                            DropdownMenuItem(
                                text = { 
                                    Text(item.name.translate(isTamil), fontWeight = FontWeight.Medium) 
                                },
                                onClick = {
                                    onColorSelected(item.color, item.name)
                                    expanded = false
                                    searchQuery = "" 
                                },
                                leadingIcon = {
                                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(item.color))
                                }
                            )
                        }
                    }
                }
            }
            
            Box(
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Warp colour".translate(isTamil),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PremiumTextPrimary,
                    textAlign = TextAlign.Center
                )
            }
            
            // Right: Toggle
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 36.dp) // Optimized for Tamil/EN
                    .clip(RoundedCornerShape(50))
                    .background(if (isTamil) BrandGreen else PremiumBg)
                    .border(1.dp, if (isTamil) BrandGreen else Color(0xFFE2E8F0), RoundedCornerShape(50))
                    .clickable { onLanguageToggle() },
                contentAlignment = Alignment.Center
            ) {
                 Text(
                    text = if (isTamil) "EN" else "த",
                    fontSize = 16.sp, // Increased for clarity
                    fontWeight = FontWeight.Bold,
                    color = if (isTamil) Color.White else PremiumTextSecondary,
                    lineHeight = 1.sp // Compact vertical alignment
                )
            }
        }
    }
}

@Composable
fun Secondary_Header_Weft(
    isTamil: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = PremiumBg, // Use background color to cover scrolling items
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp, 24.dp, 12.dp)
        ) {
            Text(
                text = "Suitable Weft colours".translate(isTamil),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = PremiumTextPrimary
            )
        }
    }
}

@Composable
fun Weft_Color_Row(
    color: Color,
    name: String,
    count: Int,
    isTamil: Boolean,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = PremiumSurface,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Swatch + Name (Take all available space)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f) // Key for static stepper
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(1.dp, Color.Black.copy(alpha = 0.05f), CircleShape)
                )
                val translatedName = remember(name, isTamil) { name.translate(isTamil) }
                Text(
                    text = translatedName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PremiumTextPrimary,
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))

            // Right: Stepper Pill (Fixed Size/Position)
            Surface(
                shape = RoundedCornerShape(50),
                color = PremiumBg,
                modifier = Modifier.height(36.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    // Plus (Left)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { onCountChange(count + 1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PremiumTextPrimary)
                    }
                    
                    // Count (Editable)
                    val focusManager = LocalFocusManager.current
                    BasicTextField(
                        value = count.toString(),
                        onValueChange = { newVal ->
                            val parsed = newVal.toIntOrNull() ?: 0
                            onCountChange(parsed)
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = PremiumTextPrimary,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .width(32.dp)
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                    
                    // Minus (Right)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { if (count > 0) onCountChange(count - 1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("-", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PremiumTextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun Sticky_Footer_Total(
    count: Int,
    isTamil: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp) // Adjusted padding
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50), // Full Pill
            color = PremiumTextPrimary, // Dark Footer
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp), // Reduced vertical padding for sleek look
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if(isTamil) "மொத்தம் :" else "Total Selected :",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.weight(1f) // Flexible Text
                )
                
                Spacer(modifier = Modifier.width(16.dp)) // Safety Gap

                Surface(
                    shape = RoundedCornerShape(50),
                    color = BrandGreen
                ) {
                    Text(
                        text = count.toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
