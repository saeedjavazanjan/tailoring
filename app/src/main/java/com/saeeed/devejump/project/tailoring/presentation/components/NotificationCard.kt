package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saeeed.devejump.project.tailoring.domain.model.Notification
import okhttp3.internal.wait
import java.nio.file.WatchEvent

@Composable
fun NotificationCard(
    notification: Notification
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                top = 6.dp,
                start = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth()
            .background(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),

    ) {
        Column(
            Modifier.background(Color.White)
        ) {
            Row {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text =notification.title,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(10.dp),
                    text =notification.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

            }
            Text(
                modifier = Modifier.padding(10.dp),
                text =notification.text,
                fontSize = 12.sp,
            )
            
        }
        
    }
}