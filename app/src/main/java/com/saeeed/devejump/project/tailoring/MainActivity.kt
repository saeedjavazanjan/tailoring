package com.saeeed.devejump.project.tailoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saeeed.devejump.project.tailoring.ui.theme.TailoringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
    }


}
/*
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFF2F2F2))
            ) {
                Image(painter = painterResource(
                    id = R.drawable.ic_launcher_foreground),
                    contentDescription ="nothing",
                    modifier = Modifier
                        .height(300.dp)
                        .width(300.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {

                    Row (
                         modifier = Modifier
                             .fillMaxWidth(),
                             horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "name of item",
                            style = TextStyle(
                                fontSize =26.sp
                            )
                        )
                        Text(
                            text = "price of item",
                            style = TextStyle(
                                fontSize =17.sp,
                                color = Color(0xFF85bb65)
                            ),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }


                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "detail of item",
                        style = TextStyle(
                            fontSize =17.sp
                        )
                    )
                    Spacer(modifier = Modifier.padding(10.dp))

                    Button(
                        onClick = { */
/*TODO*//*
 },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "ORDER NOW")
                    }

                }
            }
        }
*/
