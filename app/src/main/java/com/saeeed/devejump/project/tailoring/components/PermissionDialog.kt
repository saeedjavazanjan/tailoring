package com.saeeed.devejump.project.tailoring.components
import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        AlertDialog(
            onDismissRequest = onDismiss,
            buttons = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider()
                    Text(
                        text = if (isPermanentlyDeclined) {
                            "اعطای دسترسی"
                        } else {
                            "فهمیدم"
                        },
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isPermanentlyDeclined) {
                                    onGoToAppSettingsClick()
                                    onDismiss()
                                } else {
                                    onOkClick()
                                }
                            }
                            .padding(16.dp)
                    )
                }
            },
            title = {
                Text(text = "نیاز به دسترسی")
            },
            text = {
                Text(
                    text = permissionTextProvider.getDescription(
                        isPermanentlyDeclined = isPermanentlyDeclined
                    )
                )
            },
            modifier = modifier
        )
    }
}
interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "ظاهرا شما با دسترسی برنامه به دوربین موافقت نکرده اید لازم است از بخش تنظیمات دسترسی را اعطا نمایید."
        } else {
           "لطفا با دسترسی برنامه به دوربین موافقت کنید "
        }
    }
}

class StoragePermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "ظاهرا شما با دسترسی برنامه به حافظه داخلی موافقت نکرده اید لازم است از بخش تنظیمات دسترسی را اعطا نمایید."
        } else {
            "لطفا با دسترسی برنامه به حافظه داخلی موافقت کنید "
        }
    }
}


