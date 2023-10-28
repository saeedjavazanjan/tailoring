package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen

@Composable
fun BestsRowsOfHome(
    onNavigateToDescriptionScreen:(String)-> Unit,
    loading: Boolean,
    bestOfMonthMethods: MutableState<List<SewMethod>>,
    bestOfWeekMethods: MutableState<List<SewMethod>>,
    bestOfDayMethods: MutableState<List<SewMethod>>,
    errors:MutableList<Boolean>


) {

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

    }
    if (!errors[0]){
        Row (
            modifier= Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextButton(

                onClick = {
                    val route = Screen.MoreOfBests.route + "/${"month"}"
                    onNavigateToDescriptionScreen(route)
                }
            ){
                Text(
                    text = stringResource(id = R.string.more),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Blue,
                )

            }


            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.best_of_month),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
            )


        }
        BestsRow(
            loading = loading ,
            sewMethods = bestOfMonthMethods.value.take(3) ,
            onNavigateToDescriptionScreen = onNavigateToDescriptionScreen,


            )
        Spacer(modifier = Modifier.size(50.dp))

    }
   if(!errors[1]){
       Row (
           modifier= Modifier
               .fillMaxWidth()
               .padding(10.dp),
           verticalAlignment = Alignment.CenterVertically
       ){
           TextButton(

               onClick = {
                   val route = Screen.MoreOfBests.route + "/${"week"}"
                   onNavigateToDescriptionScreen(route)
               }
           ){
               Text(
                   text = stringResource(id = R.string.more),
                   style = MaterialTheme.typography.bodySmall,
                   color = Color.Blue,
               )

           }


           Spacer(modifier = Modifier.weight(1f))

           Text(
               text = stringResource(id = R.string.best_of_week),
               style = MaterialTheme.typography.bodyLarge,
               color = Color.DarkGray,
           )


       }

       BestsRow(
           loading = loading ,
           sewMethods = bestOfWeekMethods.value.take(3) ,
           onNavigateToDescriptionScreen = onNavigateToDescriptionScreen,
       )
       Spacer(modifier = Modifier.size(50.dp))
   }
   if (!errors[2]){
       Row (
           modifier= Modifier
               .fillMaxWidth()
               .padding(10.dp),
           verticalAlignment = Alignment.CenterVertically
       ){
           TextButton(

               onClick = {
                   val route = Screen.MoreOfBests.route + "/${"day"}"
                   onNavigateToDescriptionScreen(route)
               }
           ){
               Text(
                   text = stringResource(id = R.string.more),
                   style = MaterialTheme.typography.bodySmall,
                   color = Color.Blue,
               )

           }

           Spacer(modifier = Modifier.weight(1f))
           Text(
               text = stringResource(id = R.string.best_of_day),
               style = MaterialTheme.typography.bodyLarge,
               color = Color.DarkGray,
           )


       }
       BestsRow(
           loading = loading ,
           sewMethods = bestOfDayMethods.value.take(3) ,
           onNavigateToDescriptionScreen = onNavigateToDescriptionScreen,


           )
   }

}