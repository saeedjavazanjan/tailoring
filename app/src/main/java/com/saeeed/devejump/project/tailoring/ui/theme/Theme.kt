package com.saeeed.devejump.project.tailoring.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.saeeed.devejump.project.tailoring.components.GenericDialog
import com.saeeed.devejump.project.tailoring.components.GenericDialogInfo
import com.saeeed.devejump.project.tailoring.components.NegativeAction
import com.saeeed.devejump.project.tailoring.components.PositiveAction
import com.saeeed.devejump.project.tailoring.presentation.components.CircularIndeterminateProgressBar
import com.saeeed.devejump.project.tailoring.presentation.components.ConnectivityMonitor
import java.util.Queue

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi

@Composable
fun AppTheme(
    darkTheme: Boolean,
    isNetworkAvailable: Boolean,
    displayProgressBar: Boolean,
    dialogQueue: Queue<GenericDialogInfo>? = null,
    scaffoldState:ScaffoldState,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = if (!darkTheme) Color.White else Color.Black)
        ){

                Column{
                    ConnectivityMonitor(isNetworkAvailable = isNetworkAvailable)
                    content()
                }

            CircularIndeterminateProgressBar(isDisplayed = displayProgressBar)

            ProcessDialogQueue(
                dialogQueue = dialogQueue,
            )

        }
    }
}



@Composable
fun ProcessDialogQueue(
    dialogQueue: Queue<GenericDialogInfo>?,
) {
    dialogQueue?.peek()?.let { dialogInfo ->
        GenericDialog(
            onDismiss = dialogInfo.onDismiss,
            title = dialogInfo.title,
            description = dialogInfo.description,
            positiveAction = dialogInfo.positiveAction,
            negativeAction = dialogInfo.negativeAction
        )
    }
}
/*@Composable
fun showSnackBar(
    snackBarInfo: CustomSnackBarInfo?,
    snackbarHostState: SnackbarHostState
){
    snackBarInfo?.let {
        DefaultSnackbar(
            snackbarHostState =snackbarHostState,
            onDismiss = snackBarInfo.onDismiss
            )
    }
}*/

