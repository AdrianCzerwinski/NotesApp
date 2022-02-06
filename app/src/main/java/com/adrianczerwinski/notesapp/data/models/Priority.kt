package com.adrianczerwinski.notesapp.data.models

import androidx.compose.ui.graphics.Color
import com.adrianczerwinski.notesapp.ui.theme.HighPriorityColor
import com.adrianczerwinski.notesapp.ui.theme.LowPriorityColor
import com.adrianczerwinski.notesapp.ui.theme.MediumPriorityColor
import com.adrianczerwinski.notesapp.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)

}