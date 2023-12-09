package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen

@Composable
fun ReportAlertDialog(
    ok: () -> Unit,
    cancle : () -> Unit
) {


    AlertDialog(
            onDismissRequest = {
            },
            // below line is use to display title of our dialog
            // box and we are setting text color to white.
            title = {
                Text(
                    text = stringResource(R.string.report_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(text = stringResource(R.string.report_text), fontSize = 16.sp)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        ok()
                    }) {
                    Text(
                        stringResource(R.string.report),
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(color = Color.Black)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        cancle()
                    }) {
                    Text(
                        stringResource(R.string.cancel),
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(color = Color.Black)
                    )
                }
            },
        )

}