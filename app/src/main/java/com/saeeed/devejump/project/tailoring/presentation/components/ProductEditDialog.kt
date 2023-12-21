package com.saeeed.devejump.project.tailoring.presentation.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.media3.common.Timeline.Window
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.presentation.ui.upload.SelectedImagesPager
import com.saeeed.devejump.project.tailoring.presentation.ui.upload.createImageFile
import java.util.Objects

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductEditDialog(
    showDialog: (Boolean) -> Unit,
    requestPermission:()->Unit,
    zipSelectedFile:(List<Uri?>)->Unit,
    setProduct:(Product)->Unit
    )

{

    val context= LocalContext.current
    val expanded = remember {
        listOf(
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false)
        ) }


    var mas= remember {
        mutableStateOf<String>("")
    }
    var price= remember {
        mutableStateOf<String>("")
    }
    var supply= remember {
        mutableStateOf<String>("")
    }
    var unit= remember {
        mutableStateOf<String>("")
    }

    var selectedImages= remember { mutableStateListOf<Uri?>(Uri.EMPTY) }

    var name= remember {
        mutableStateOf<String>("")
    }
    var description= remember {
        mutableStateOf<String>("")
    }
    val selectedTypeOfProduct = remember { mutableStateOf("محصول فیزیکی") }



    Dialog(
        onDismissRequest = { showDialog(false) },

        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
                    decorFitsSystemWindows=false
        ),




    ) {

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(10.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 10.dp,

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight()
                        .padding(top = 20.dp)

                ) {
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
                                ProductNameAndDescription(
                                    name=name,
                                    description=description
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

                                ProductImages(
                                    selectedImages=selectedImages
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

                                ProductType(
                                    zipSelectedFile = {
                                        zipSelectedFile(it)
                                    },
                                   selectedTypeOfProduct = selectedTypeOfProduct,
                                    requestPermission={
                                        requestPermission()
                                    }
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
                                expanded[3].value = !expanded[3].value
                            })
                    ) {
                        Column {


                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(id = R.string.product_details),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    if (expanded[3].value)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }

                            if (expanded[3].value) {

                                ProductDetail(
                                    typeOfProduct =selectedTypeOfProduct.value ,
                                    price = price,
                                    mas = mas,
                                    unit=unit,
                                    supply = supply

                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Green),
                            onClick = {
                                when(
                                    checkCondition(
                                        name=name.value,
                                        description=description.value,
                                        typeOfProduct = selectedTypeOfProduct.value,
                                        mas = mas.value,
                                        supply = supply.value,
                                        unit = unit.value,
                                        price=price.value,
                                    )

                                ){
                                     "there is free field"->{
                                         Toast.makeText(context,
                                             context.getString(R.string.free_field_warning),
                                             Toast.LENGTH_SHORT)
                                             .show()

                                     }
                                    "short description"->{
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.short_description_warning),
                                            Toast.LENGTH_SHORT)
                                            .show()

                                    }
                                    "Ok"->{
                                        showDialog(false)

                                    }

                                }
                                

                            }) {
                            Text(
                                text = stringResource(id = R.string.product_save),
                                color = Color.White
                            )

                        }
                        
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Gray),
                            onClick = {
                                showDialog(false)
                            }) {
                            Text(
                                text = stringResource(id = R.string.cancel),
                                color = Color.White
                            )



                        }
                        }
                    }

                }
                }

            }

    }

fun checkCondition(
    name:String,
    description:String,
    typeOfProduct: String,
    mas:String,
    supply:String,
    unit:String,
    price: String
):String {
    if (name == "" || description=="" || price==""){
        return "there is free field"
    }
    if(typeOfProduct == "محصول فیزیکی"){
        if(mas == "" || supply == "" || unit =="" ){
            return "there is free field"
        }
    }
    if (description.length < 50){
        return "short description"
    }
   else return "Ok"
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductDetail(
    typeOfProduct:String,
    price:MutableState<String>,
    mas:MutableState<String>,
    unit:MutableState<String>,
    supply:MutableState<String>,

){
    val options = listOf("عدد", "متر", "کیلو", "قواره", "بسته")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    Column{
        if (typeOfProduct.equals(stringResource(id = R.string.physical_product))){
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                value = mas.value.toString(),
                label = {
                    Text(text = stringResource(id = R.string.mas_of_product), color = Color.Gray)
                },
                maxLines = 1,
                singleLine = true,
                onValueChange = {
                    mas.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
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
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                value = supply.value,
                label = {
                    Text(text = stringResource(id = R.string.supply_of_product), color = Color.Gray)
                },
                singleLine = true,
                onValueChange = {
                    supply.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                colors = TextFieldDefaults.textFieldColors(

                    textColor = Color.DarkGray,
                    placeholderColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
           /* TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .fillMaxWidth(0.9f),
                shape = MaterialTheme.shapes.medium,
                value = unit.value!!,
                label = {
                    Text(text = stringResource(id = R.string.unit_of_product), color = Color.Gray)
                },
                singleLine = true,
                onValueChange = {
                    unit.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                colors = TextFieldDefaults.textFieldColors(

                    textColor = Color.DarkGray,
                    placeholderColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )*/
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { },
                    label = {
                        Text(text = stringResource(id = R.string.unit_of_product), color = Color.Gray)
                    },
                    trailingIcon = {

                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                     colors = TextFieldDefaults.textFieldColors(

                        textColor = Color.DarkGray,
                        placeholderColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                unit.value=selectionOption
                            }
                        ){
                            Text(text = selectionOption)
                        }
                    }
                }
            }
        }

        TextField(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            value = price.value.toString(),
            label = {
                Text(text = stringResource(id = R.string.price_of_product), color = Color.Gray)
            },
            singleLine = true,
            onValueChange = {
                price.value = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
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


@Composable
fun ProductType(
    zipSelectedFile:(List<Uri?>)->Unit,
    selectedTypeOfProduct:MutableState<String>,
    requestPermission:()->Unit
){
    val context= LocalContext.current
    val selectedFilesPath = remember { mutableStateListOf<Uri?>(Uri.EMPTY) }
    val fileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()) {
        selectedFilesPath.apply {
            clear()
            addAll(it)
            zipSelectedFile(it)

        }

    }

    val radioOptions = listOf(
        stringResource(id = R.string.physical_product),
        stringResource(id = R.string.digital_product)
        )
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        radioOptions.forEach { productType ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            selectedTypeOfProduct.value = productType

                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (productType == selectedTypeOfProduct.value),
                            onClick = { selectedTypeOfProduct.value = productType }

                )
                Text(
                    text = productType,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        if(selectedTypeOfProduct.value.equals(stringResource(id = R.string.digital_product))){

            Text(
                text = stringResource(id = R.string.digital_condition),
                color = Color.LightGray,
               style= MaterialTheme.typography.bodySmall

            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        fileLauncher.launch("*/*")
                    } else {
                        requestPermission()
                    }


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
fun ProductImages(
    selectedImages:SnapshotStateList<Uri?>
){
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
fun ProductNameAndDescription(
    name:MutableState<String>,
    description:MutableState<String>
){

    val characterCounter = remember {
        mutableStateOf(0)
    }
    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .fillMaxWidth(0.9f),
            shape = MaterialTheme.shapes.medium,
            value = name.value,
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
                .padding(top = 8.dp, end = 8.dp, start = 8.dp)
                .fillMaxWidth(0.9f),
            shape = MaterialTheme.shapes.medium,
            value = description.value!!,
            label = {
                Text(text = stringResource(id = R.string.description_of_product), color = Color.Gray)
            },
            maxLines = 4,
            onValueChange = {
                description.value = it
                characterCounter.value= description.value.length

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
        Text(
            modifier = Modifier.padding(start = 8.dp, end=8.dp, bottom = 8.dp),
            text = "${characterCounter.value}/50" ,
            color=Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )

    }

}






