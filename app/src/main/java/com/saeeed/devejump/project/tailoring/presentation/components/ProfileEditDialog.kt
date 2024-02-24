package com.saeeed.devejump.project.tailoring.presentation.components

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.utils.TAG
import androidx.compose.material3.Card as Card1

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileEditDialog(
    showDialog: (Boolean) -> Unit,
    userData: UserData?,
    applyChanges:(
        imageUri:Uri,
        userName:String,
            bio:String
            )-> Unit

) {

    val context= LocalContext.current

    val imageUri = remember {
        mutableStateOf<Uri?>(userData!!.avatar.toUri())
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri.value = uri
    }
    val userName= remember {
        mutableStateOf(userData!!.userName)
    }
    val userBio= remember {
        mutableStateOf(userData!!.bio)
    }

    val expanded = remember {
        listOf(
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false),
            ) }

    Dialog(onDismissRequest = { showDialog(false) }) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {

                Column {

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(id = R.string.profile_edit_title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable(onClick = {
                                expanded[0].value = !expanded[0].value
                            })
                    ) {
                        Column(

                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = stringResource(id = R.string.profile_edit_user_name),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (expanded[0].value) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .fillMaxWidth(0.9f),
                                    shape = MaterialTheme.shapes.medium,
                                    value =userName.value!!,
                                    maxLines = 1,
                                    singleLine = true
                                    ,
                                    onValueChange ={
                                        userName.value=it
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Default,
                                    ),
                                     colors = TextFieldDefaults.textFieldColors(

                                         textColor = Color.DarkGray,
                                         placeholderColor = Color.White,
                                         focusedIndicatorColor = Color.Transparent,
                                         unfocusedIndicatorColor = Color.Transparent,
                                         disabledIndicatorColor = Color.Transparent
                                 )
                                )
                            }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable(onClick = {
                                expanded[1].value = !expanded[1].value
                            })
                    ) {
                        Column(

                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = stringResource(id = R.string.profile_edit_avatar),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (expanded[1].value) {

                                ConstraintLayout(
                                modifier = Modifier.padding(8.dp)
                                ) {
                                    val (avatarHolder,selectImageButton) = createRefs()
                                    if(imageUri.value ==null){
                                        GlideImage(
                                            model = userData!!.avatar,
                                            loading = placeholder(R.drawable.empty_plate),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .height(50.dp)
                                                .width(50.dp)
                                                .fillMaxWidth(0.4f)
                                                .clip(CircleShape)
                                                .constrainAs(avatarHolder) {
                                                    start.linkTo(parent.start)
                                                    top.linkTo(parent.top)
                                                },
                                            contentScale = ContentScale.Crop,
                                        )
                                    }else{
                                        GlideImage(
                                            model = imageUri.value,
                                            loading = placeholder(R.drawable.empty_plate),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .height(50.dp)
                                                .width(50.dp)
                                                .fillMaxWidth(0.4f)
                                                .clip(CircleShape)
                                                .constrainAs(avatarHolder) {
                                                    start.linkTo(parent.start)
                                                    top.linkTo(parent.top)
                                                },
                                            contentScale = ContentScale.Crop,
                                        )
                                    }

                                    Button(
                                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                                        shape= RoundedCornerShape(5.dp) ,
                                        modifier = Modifier
                                            .padding(15.dp)
                                            .fillMaxWidth(0.8f)
                                            .constrainAs(selectImageButton) {
                                                start.linkTo(avatarHolder.end)
                                                top.linkTo(avatarHolder.top)
                                                bottom.linkTo(avatarHolder.bottom)
                                                end.linkTo(parent.end)

                                            },
                                        onClick = {
                                            launcher.launch("image/*")

                                        }
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(5.dp),
                                            text = stringResource(id = R.string.profile_edit_choose_photo),
                                            color = Color.DarkGray
                                        )
                                        Icon(Icons.Default.AccountCircle, contentDescription = null)

                                    }
                                }
                            }
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable(onClick = {
                                expanded[2].value = !expanded[2].value
                            })
                    ) {
                        Column(

                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = stringResource(id = R.string.profile_edit_bio),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (expanded[2].value) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .fillMaxWidth(0.9f),
                                    shape = MaterialTheme.shapes.medium,
                                    value =userBio.value!!,

                                            maxLines = 4,
                                    onValueChange ={
                                        userBio.value=it
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Default,
                                    ),

                                    colors = TextFieldDefaults.textFieldColors(

                                        textColor = Color.DarkGray,
                                        placeholderColor = Color.White,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    )
                                    )

                            }
                        }


                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                        shape= RoundedCornerShape(5.dp) ,
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                        onClick = {



                            applyChanges(
                                if(userData!!.avatar=="")
                                    getResourceUri(context.resources,R.drawable.empty_plate)
                                else imageUri.value!! ,
                                userName.value!!,
                                userBio.value!!
                            )

                               showDialog(false)




                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = stringResource(id = R.string.profile_edit_save),
                            color = Color.DarkGray
                        )
                        Icon(Icons.Default.Done, contentDescription = null)

                    }
                }

            }

        }
    }

}

 fun getResourceUri(resources: Resources, resourceID: Int): Uri {
    return Uri.parse(
        "android.resource://" + resources.getResourcePackageName(resourceID) + "/" +
                resources.getResourceTypeName(resourceID) + '/'
                + resources.getResourceEntryName(resourceID)
    )
}
