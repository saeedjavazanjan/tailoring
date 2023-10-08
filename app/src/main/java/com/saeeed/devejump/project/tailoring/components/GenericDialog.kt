package com.saeeed.devejump.project.tailoring.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GenericDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    title: String,
    description: String? = null,
    positiveAction: PositiveAction?,
    negativeAction: NegativeAction?,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            if (description != null) {
                Text(text = description)
            }
        },
        confirmButton = {
            positiveAction?.let {
                Button(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = positiveAction.onPositiveAction,
                ) {
                    Text(text = positiveAction.positiveBtnTxt)
                }
            }
        },
        dismissButton = {
            negativeAction?.let {
                Button(
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors( containerColor =  MaterialTheme.colorScheme.onError),
                    onClick = it.onNegativeAction
                ) {
                    Text(text = negativeAction.negativeBtnTxt)
                }
            }
        }


    )
}

data class PositiveAction(
    val positiveBtnTxt: String,
    val onPositiveAction: () -> Unit,
)

data class NegativeAction(
    val negativeBtnTxt: String,
    val onNegativeAction: () -> Unit,
)