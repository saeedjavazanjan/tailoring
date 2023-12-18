package com.saeeed.devejump.project.tailoring.presentation.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.presentation.ui.upload.SelectedImagesPager
import com.saeeed.devejump.project.tailoring.presentation.ui.upload.TitleAndDescription
import com.saeeed.devejump.project.tailoring.presentation.ui.upload.UploadPostViewModel
import com.saeeed.devejump.project.tailoring.presentation.ui.upload.createImageFile
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.Objects

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductEditDialog(
    showDialog: (Boolean) -> Unit,
    requestPermission:()->Unit
    )

{

    val expanded = remember {
        listOf(
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false),
        ) }


    Dialog(
        onDismissRequest = { showDialog(false) },

        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        ),



    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column {
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
                        Column {


                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(id = R.string.name_and_detail_of_product),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    if (expanded[0].value)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,

                                    contentDescription = null
                                )
                            }

                            if (expanded[0].value) {
                                ProductNameAndDescription()
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
                        Column {


                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(id = R.string.product_images),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    if (expanded[1].value)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }

                            if (expanded[1].value) {

                                ProductImages()
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
                        Column {


                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(id = R.string.product_type),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    if (expanded[2].value)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }

                            if (expanded[2].value) {

                                ProductType()
                            }
                        }
                    }

                }
                }

            }
        }
    }


@Composable
fun ProductType(){
    val selectedFilePath = remember { mutableStateOf<Uri?>(Uri.EMPTY) }
    val fileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()) {
        selectedFilePath.value=it
    }

    val radioOptions = listOf(
        stringResource(id = R.string.physical_product),
        stringResource(id = R.string.digital_product)
        )
    val selectedOption = remember { mutableStateOf(radioOptions[0]) }
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        radioOptions.forEach { productType ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { selectedOption.value = productType }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (productType == selectedOption.value),
                            onClick = { selectedOption.value = productType }

                )
                Text(
                    text = productType,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        if(selectedOption.value.equals(stringResource(id = R.string.digital_product))){
            Text(
                text = stringResource(id = R.string.digital_condition),
                color = Color.LightGray,
               style= MaterialTheme.typography.bodySmall

            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                onClick = {
                    fileLauncher.launch("*/*")

                }) {
                Text(
                    text = stringResource(id = R.string.select_file),
                    color = Color.White
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_folder_zip_24),
                    contentDescription =null,
                    tint = Color.White
                )


            }
        }

    }

}


@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProductImages(){
    var selectedImages= remember { mutableStateListOf<Uri?>(Uri.EMPTY) }
    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)) {
        selectedImages.apply {
            clear()
            addAll(it)
        }
    }

    Row (
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)
    ){
        Card(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp),
            border = BorderStroke(2.dp, color = Color.DarkGray),
            onClick = {
                imageLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painterResource(id = R.drawable.baseline_photo_24), contentDescription = null)
                Text(text = stringResource(id = R.string.choose_photo))
            }
            
        }
        LazyRow{
            itemsIndexed(
                items = selectedImages
            ){index, selectedImage ->
                GlideImage(
                    model = selectedImage,
                    contentDescription = "",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(5.dp)
                        .clip(RectangleShape),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
   
    
}

@Composable
fun ProductNameAndDescription(){
    var name= remember {
        mutableStateOf<String>("")
    }
    var description= remember {
        mutableStateOf<String>("")
    }
    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .fillMaxWidth(0.9f),
            shape = MaterialTheme.shapes.medium,
            value = name.value!!,
            label = {
                Text(text = stringResource(id = R.string.name_of_product), color = Color.Gray)
            },
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                name.value = it
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
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .fillMaxWidth(0.9f),
            shape = MaterialTheme.shapes.medium,
            value = description.value!!,
            label = {
                Text(text = stringResource(id = R.string.detail_of_product), color = Color.Gray)
            },
            maxLines = 4,
            onValueChange = {
                description.value = it
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



@OptIn(ExperimentalFoundationApi::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun ProductImageTitleAndDescription(
    requestCameraPermission:()->Unit
){

    val typeOfPost= remember {
        mutableStateOf("")
    }
    var selectedImages= remember { mutableStateListOf<Uri?>(Uri.EMPTY) }
    val selectedVideoUri = remember { mutableStateOf<Uri?>(Uri.EMPTY) }

    val pagerState = rememberPagerState(pageCount = {
        selectedImages.size
    })
    var title= remember {
        mutableStateOf<String>("")
    }
    var description= remember {
        mutableStateOf<String>("")
    }



    val context = LocalContext.current

    val file = context.createImageFile()

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri = remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }
 
    val imageCropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            capturedImageUri.value = result.uriContent
            typeOfPost.value="cameraPhoto"
            // Got image data. Use it according to your need
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            val cropOptions = CropImageContractOptions(uri, CropImageOptions())
            imageCropLauncher.launch(cropOptions)


        }



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {

                        when(typeOfPost.value){
                            "video"->{
                                if(selectedVideoUri.value!=null){
                                    VideoPlayer(
                                        videoUrl = selectedVideoUri.value.toString(),
                                        context = context
                                    )}
                            }
                            "photo"->{
                                SelectedImagesPager(
                                    pagerState = pagerState ,
                                    selectedImages = selectedImages
                                )
                            }
                            "cameraPhoto"->{
                                GlideImage(
                                    model = capturedImageUri.value,
                                    loading = placeholder(R.drawable.empty_plate),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RectangleShape),
                                    contentScale = ContentScale.Crop,
                                )
                            }

                            else->{
                                Image(
                                    painter = painterResource(id = R.drawable.empty_plate),
                                    contentDescription =null )
                            }
                        }

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.LightGray),
                            shape = RoundedCornerShape(5.dp),

                            onClick = {
                               

                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = R.string.choose_photo_from_gallery),
                                color = Color.DarkGray
                            )


                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.LightGray),
                            shape = RoundedCornerShape(5.dp),

                            onClick = {
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                     requestCameraPermission()
                                }
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = R.string.take_photo_with_camera),
                                color = Color.DarkGray
                            )


                        }
                    }

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp, start = 20.dp, top = 10.dp, bottom = 10.dp),
                        value = title.value!!,
                        label = {
                            Text(text = stringResource(id = R.string.post_title), color = Color.Gray)
                        },
                        singleLine = true,
                        onValueChange = {
                            title.value = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Default,
                        ),

                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor= Color.White,
                            textColor = Color.DarkGray,
                            placeholderColor = Color.White,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp, start = 20.dp, top = 10.dp, bottom = 10.dp),
                        value = description.value!!,
                        label = {
                            Text(text = stringResource(id = R.string.post_description),color = Color.Gray)
                        },
                        maxLines = 5,
                        onValueChange = {
                            description.value = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Default,
                        ),

                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor= Color.White,
                            textColor = Color.DarkGray,
                            placeholderColor = Color.White,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            }





