package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.components.NegativeAction
import com.saeeed.devejump.project.tailoring.components.PositiveAction

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit?
) {

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                },
                action = {
                    data.performAction()?.let { actionLabel ->
                        TextButton(
                            onClick = {
                                onDismiss()
                            }
                        ) {

                        }
                    }
                }
            )
        },
        modifier = modifier
    )
}
data class SnackBarAction(
    val positiveBtnTxt: String,
    val onPositiveAction: () -> Unit,
)
class CustomSnackBarInfo
private constructor(builder:Builder) {

    val text: String
    val onDismiss: () -> Unit
  //  val action: SnackBarAction?

    init {
        if (builder.text == null) {
            throw Exception("SnackBar text cannot be null.")
        }
        if (builder.onDismiss == null) {
            throw Exception("SnackBar onDismiss function cannot be null.")
        }
        this.text = builder.text!!
        this.onDismiss = builder.onDismiss!!
     //   this.action=builder.action!!

    }

    class Builder {

        var text: String? = null
            private set

        var onDismiss: (() -> Unit)? = null
            private set

     /*   var action: SnackBarAction? = null
            private set*/

        fun text(text: String): Builder {
            this.text = text
            return this
        }

        fun onDismiss(onDismiss: () -> Unit): Builder {
            this.onDismiss = onDismiss
            return this
        }
     /*   fun action(
            action: SnackBarAction?,
        ): Builder {
            this.action = action
            return this
        }*/



        fun build() = CustomSnackBarInfo(this)
    }
}