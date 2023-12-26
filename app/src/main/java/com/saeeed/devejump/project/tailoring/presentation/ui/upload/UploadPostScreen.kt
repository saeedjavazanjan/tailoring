package com.saeeed.devejump.project.tailoring.presentation.ui.upload

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.components.CameraPermissionTextProvider
import com.saeeed.devejump.project.tailoring.components.GenericDialog
import com.saeeed.devejump.project.tailoring.components.NegativeAction
import com.saeeed.devejump.project.tailoring.components.PermissionDialog
import com.saeeed.devejump.project.tailoring.components.PositiveAction
import com.saeeed.devejump.project.tailoring.components.StoragePermissionTextProvider
import com.saeeed.devejump.project.tailoring.domain.model.CreatedPost
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.presentation.components.ProductEditDialog
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.components.VideoPlayer
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import com.saeeed.devejump.project.tailoring.utils.USERID
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun UploadPostScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: UploadPostViewModel,
    navController:NavController,
    onNavigateTpProductDetailScreen: (String) -> Unit
){
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()
    val permissionDialogQueue = viewModel.visiblePermissionDialogQueue

    val typeOfPost= remember {
        mutableStateOf("")
    }
    var selectedImages=remember { mutableStateListOf<Uri?>(Uri.EMPTY) }
    val selectedVideoUri = remember { mutableStateOf<Uri?>(Uri.EMPTY) }

    val pagerState = rememberPagerState(pageCount = {
        selectedImages.size
    })
    val title= remember {
        mutableStateOf<String>("")
    }
    val description= remember {
        mutableStateOf<String>("")
    }
    val exitDialogShow= remember {
        mutableStateOf(false)
    }

    val removeDialogShow= remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val activity =context as Activity

    val file = context.createImageFile()

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

   /* var capturedImageUri = remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }*/
    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)) {
        selectedVideoUri.value= Uri.EMPTY
        selectedImages.apply {
            clear()
            addAll(it)
        }
        typeOfPost.value="photo"
    }
    val imageCropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {

            selectedImages.apply {
                clear()
                selectedImages.add(result.uriContent)
            }
            selectedVideoUri.value= Uri.EMPTY
            typeOfPost.value="cameraPhoto"
            // Got image data. Use it according to your need
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            val cropOptions = CropImageContractOptions(uri, CropImageOptions())
            imageCropLauncher.launch(cropOptions)


        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
       contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission =Manifest.permission.CAMERA,
                isGranted = isGranted
            )
        }
    )

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission =Manifest.permission.WRITE_EXTERNAL_STORAGE,
                isGranted = isGranted
            )
        }
    )


    val videoLauncher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri.let {
            selectedVideoUri.value=uri
            typeOfPost.value="video"

        }
    }

    val showDialog =  remember { mutableStateOf(false) }


    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {
        
        if (exitDialogShow.value){
            GenericDialog(
                onDismiss = { /*TODO*/ },
                title = "",
                description = stringResource(id = R.string.not_saved_warning) ,
                positiveAction = PositiveAction(
                  positiveBtnTxt =   "بله",
                    onPositiveAction = {
                        exitDialogShow.value=false
                        navController.popBackStack()
                    }
                ),
                negativeAction = NegativeAction(
                    negativeBtnTxt="خیر",
                    onNegativeAction = {
                        exitDialogShow.value=false

                    }

                )
            )
        }
        if (removeDialogShow.value){
            GenericDialog(
                onDismiss = { /*TODO*/ },
                title = "",
                description = stringResource(id = R.string.remove_product_warning) ,
                positiveAction = PositiveAction(
                    positiveBtnTxt =   "بله",
                    onPositiveAction = {
                        removeDialogShow.value=false
                    viewModel.product.value=Product(
                        id=0,
                        name = "",
                        description = "",
                        typeOfProduct = "محصول فیزیکی",
                        unit = "عدد",
                        mas = "",
                        supply = "",
                        price = "",
                        postId = 0
                    )
                    }
                ),
                negativeAction = NegativeAction(
                    negativeBtnTxt="خیر",
                    onNegativeAction = {
                        removeDialogShow.value=false

                    }

                )
            )
        }
        
        
        

        if (showDialog.value){
            val fileZippingLoading=viewModel.fileZippingLoading.value
            val productAttachedFile=viewModel.productAttachedFile.value

            ProductEditDialog(
                product=viewModel.product.value!!,
                showDialog = {
                    showDialog.value=it
                             },
                requestPermission = {
                    storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                },
                zipSelectedFile = {
                    viewModel.zipSelectedFile(it,context)
                },
                setProduct = {
                             viewModel.product.value=it

                },
                productAttachedFile = productAttachedFile,
                fileZippingLoading=fileZippingLoading
            )
        }

        permissionDialogQueue
            .reversed()
            .forEach { permission ->
                PermissionDialog(
                    permissionTextProvider = when (permission) {
                        Manifest.permission.CAMERA -> {
                            CameraPermissionTextProvider()
                        }
                        Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                            StoragePermissionTextProvider()
                        }

                        else -> return@forEach
                    },
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(activity,
                        permission
                    ),
                    onDismiss = viewModel::dismissDialog,
                    onOkClick = {
                        viewModel.dismissDialog()

                        when(permission){
                            Manifest.permission.CAMERA -> {
                                cameraPermissionLauncher.launch(
                                    Manifest.permission.CAMERA
                                )
                            }
                            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                                storagePermissionLauncher.launch(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                            }
                        }

                    },
                    onGoToAppSettingsClick = {
                        activity.openAppSettings()
                    }
                )

    }





        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                topBar = {
                    TopBar()
                }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(rememberScrollState()),
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
                                        model = selectedImages[0],
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
                                imageLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = R.string.choose_photo),
                                color = Color.DarkGray
                            )
                            Icon(painterResource(
                                id = R.drawable.baseline_photo_24),
                                contentDescription = null,
                                tint = Color.White
                            )
                            
                        }

                        IconButton(modifier = Modifier
                            .weight(1f),
                            onClick = {
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    // Request a permission
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }


                            }) {
                            Icon(painterResource(
                                id = R.drawable.baseline_photo_camera_24),
                                contentDescription = null,
                                tint = Color.LightGray
                            )                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.LightGray),
                            shape = RoundedCornerShape(5.dp),

                            onClick = {
                                videoLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.VideoOnly
                                    )
                                )

                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = stringResource(id = R.string.choose_video),
                                color = Color.DarkGray
                            )
                            Icon(painterResource(
                                id = R.drawable.baseline_videocam_24),
                                contentDescription = null,
                                tint = Color.White
                            )

                        }
                    }
                    TitleAndDescription(
                        title=title,
                        description=description
                    )

                    if(viewModel.product.value!!.name!=""){
                        ProductPreview(
                            product = viewModel.product.value!!,
                            removeProduct = {
                                removeDialogShow.value=true

                            },
                            editProduct = {
                                showDialog.value=true
                            },
                            onNavigateTpProductDetailScreen = { prod->
                                val productJson=viewModel.jsonStringOfProduct(prod)
                                val route = Screen.ProductDetail.route + "/" + productJson
                                onNavigateTpProductDetailScreen(route)
                            }
                        )
                    }else{
                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(Color.LightGray),
                            onClick = {
                                showDialog.value=true

                            }) {
                            Text(
                                text = stringResource(id = R.string.attach_product),
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_attach_file_24),
                                contentDescription =null,
                                tint = Color.White
                            )
                        }
                    }



                            Button(
                                modifier= Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 100.dp),
                                colors = ButtonDefaults.buttonColors(Color.Green),
                                onClick = {

                                    if(title.value=="" || description.value==""){
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.free_field_warning),
                                            Toast.LENGTH_SHORT
                                            ).show()

                                    }else{
                                        viewModel.uploadPostAndProduct(
                                            post = CreatedPost(
                                                id=0,
                                                title = title.value,
                                                postType =
                                                    if(selectedVideoUri.value==Uri.EMPTY){
                                                        "image"
                                                    }else{
                                                        "video"
                                                    }
                                                ,
                                                publisher = USERID.toString(),
                                                authorId = USERID,
                                                featuredImage = selectedImages.toList(),
                                                videoUri = selectedVideoUri.value!!,
                                                description = description.value,
                                                dateAdded =Date() ,
                                                haveProduct =
                                                if (viewModel.product.value==null){
                                                    0
                                                }else{
                                                    1
                                                }


                                            )
                                        )
                                    }

                                }) {
                                Text(
                                    text = stringResource(id = R.string.product_save),
                                    color = Color.White
                                )

                            }







                }
            }
        }
    }

 BackHandler {
     
     exitDialogShow.value=true

 }


}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductPreview(
    product: Product,
    removeProduct:()->Unit,
    editProduct:()->Unit,
    onNavigateTpProductDetailScreen:(Product)->Unit
){
    Card(
        modifier=Modifier.padding(20.dp),
        elevation=CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
                border = BorderStroke(1.dp, color = Color.Gray)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)

        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                GlideImage(
                    model = product.images[0],
                    contentDescription = "",
                    loading=placeholder(R.drawable.empty_plate),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    modifier=Modifier.align(Alignment.Bottom),
                    text =product.name
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    editProduct()

                }) {
                    Icon(Icons.Default.Edit , contentDescription =null )

                }
                IconButton(onClick = {
                    removeProduct()
                }) {
                    Icon(Icons.Default.Delete , contentDescription =null )

                }

            }
            Text(
                modifier=Modifier.padding(10.dp),
                text =product.description
            )

            Text(
                modifier=Modifier.padding(10.dp),

                text ="قیمت:  ${product.price} تومان "
            )
         /*   TextButton(
                modifier=Modifier
                    .align(Alignment.End),
                onClick = {
                    onNavigateTpProductDetailScreen(product)

                }) {
                Text(
                    modifier=Modifier.padding(10.dp),
                    text = stringResource(id =R.string.more),
                    color= Color.Blue
                )
            }*/

        }




    }



}



@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun SelectedImagesPager(
    pagerState:PagerState,
    selectedImages:SnapshotStateList<Uri?>
){
    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fill,
        pageSpacing = 15.dp,
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 5.dp
        )

    ){
        val currentImage=selectedImages[it]
        GlideImage(
            model = currentImage,
            loading = placeholder(R.drawable.empty_plate),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .clip(RectangleShape),
            contentScale = ContentScale.Crop,
        )
    }

}

@Composable
fun TitleAndDescription(
    title:MutableState<String>,
    description:MutableState<String>
){


    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, start = 20.dp, top = 10.dp, bottom = 10.dp),
        value = title.value!!,
        label = {
                Text(text = stringResource(id = R.string.post_title)+"*", color = Color.Gray)
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
            Text(text = stringResource(id = R.string.post_description)+"*",color = Color.Gray)
        },
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
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}