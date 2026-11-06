package com.topicos.finanapp.presentation.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.topicos.finanapp.presentation.common.theme.FinanExpense
import com.topicos.finanapp.presentation.common.theme.FinanNavy

@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    confirmButtonText: String = "Eliminar",
    dismissButtonText: String = "Cancelar",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = FinanExpense)
            ) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = FinanNavy)
            ) {
                Text(text = dismissButtonText)
            }
        },
        containerColor = Color.White
    )
}
