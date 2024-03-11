package com.saeeed.devejump.project.tailoring.presentation.ui.UpdatePost
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.saeeed.devejump.project.tailoring.domain.model.OnUpdatePost
import com.saeeed.devejump.project.tailoring.domain.model.OnUpdateProduct
import com.saeeed.devejump.project.tailoring.domain.model.OnUploadPost
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.presentation.components.ProductEditDialog
import com.saeeed.devejump.project.tailoring.presentation.components.VideoPlayer
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.search.Category
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState",
    "StateFlowValueCalledInComposition", "SuspiciousIndentation"
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun UpdatePostScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    viewModel: UpdatePostViewModel,
    postId:Int?,
    navController:NavController,
    onNavigateTpProductDetailScreen: (String) -> Unit,
    onNavigateToAuthorProfile:()->Unit
) {
    val context = LocalContext.current

    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState = rememberScaffoldState()
    val successFulUpdate = viewModel.successFulUpdate
    val successFulProductUpdate = viewModel.successFulProductUpdate
    var post = viewModel.post.value
    val radioOptions = listOf(
        Category.TAILORING.value,
        Category.DOLL_MAKING.value,
        Category.EMBROIDERY.value,
        Category.KNITTING.value,
        Category.TERMEHDOOZY.value,
        Category.OTHER.value,
        Category.LEATHERING.value,

        )
    LaunchedEffect(Unit) {
        viewModel.getPostFromServerForEdit(postId!!)

    }
    LaunchedEffect(key1 = successFulUpdate.value ,key2=successFulProductUpdate.value) {

        if (successFulProductUpdate.value) {
            Toast.makeText(
                context,
                context.getString(R.string.product_saved_successfully),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (successFulUpdate.value) {
            Toast.makeText(
                context,
                context.getString(R.string.post_updated_successfully),
                Toast.LENGTH_SHORT
            ).show()
            onNavigateToAuthorProfile()
            successFulUpdate.value = false
            viewModel.product.value = null
            viewModel.post.value=null


        }
    }
    val permissionDialogQueue = viewModel.visiblePermissionDialogQueue
    if (loading && post == null) {
        // LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
    } else if (!loading && post == null ) {
        // TODO("Show Invalid Recipe")
    } else {
        post?.let {
            val typeOfPost = remember {
                mutableStateOf(post!!.postType)
            }
            val category = remember {
                mutableStateOf(post!!.category)
            }

            val postImages= post.featuredImage.map{
                Uri.parse(it)
            }
            val loadedImages= remember {
                mutableStateOf<List<Uri?>>(
                   postImages
                )
            }

            val haveProduct= remember {
                mutableStateOf(post.haveProduct)
            }

            val loadedVideoUri = remember {
                mutableStateOf<String>(post!!.videoUrl)

            }

            val title = remember {
                mutableStateOf<String>(post!!.title)
            }
            val description = remember {
                mutableStateOf<String>(post!!.description)
            }


            val exitDialogShow = remember {
                mutableStateOf(false)
            }

            val removeDialogShow = remember {
                mutableStateOf(false)
            }

            val showDialog = remember { mutableStateOf(false) }





            AppTheme(
                displayProgressBar = loading,
                darkTheme = isDarkTheme,
                isNetworkAvailable = isNetworkAvailable,
                dialogQueue = dialogQueue.queue.value,
                scaffoldState = scaffoldState

            ) {

                if (exitDialogShow.value) {
                    GenericDialog(
                        onDismiss = { /*TODO*/ },
                        title = "",
                        description = stringResource(id = R.string.not_saved_warning),
                        positiveAction = PositiveAction(
                            positiveBtnTxt = "بله",
                            onPositiveAction = {
                                exitDialogShow.value = false
                                viewModel.post.value=null
                                navController.popBackStack()
                            }
                        ),
                        negativeAction = NegativeAction(
                            negativeBtnTxt = "خیر",
                            onNegativeAction = {
                                exitDialogShow.value = false

                            }

                        )
                    )
                }
                if (removeDialogShow.value) {
                    GenericDialog(
                        onDismiss = { /*TODO*/ },
                        title = "",
                        description = stringResource(id = R.string.remove_product_warning),
                        positiveAction = PositiveAction(
                            positiveBtnTxt = "بله",
                            onPositiveAction = {
                                removeDialogShow.value = false
                                viewModel.product.value = null /*Product(
                                    id = 0,
                                    name = "",
                                    description = "",
                                    typeOfProduct = "محصول فیزیکی",
                                    unit = "عدد",
                                    mas = "",
                                    supply = "",
                                    price = "",
                                    postId = 0
                                )*/
                            }
                        ),
                        negativeAction = NegativeAction(
                            negativeBtnTxt = "خیر",
                            onNegativeAction = {
                                removeDialogShow.value = false

                            }

                        )
                    )
                }




                if (showDialog.value) {
                    val fileZippingLoading = viewModel.fileZippingLoading.value
                    val productAttachedFile = viewModel.productAttachedFile.value

                    ProductEditDialog(
                        state="update",
                        product = viewModel.product.value!!,
                        showDialog = {
                            showDialog.value = it
                        },
                        requestPermission = {
                           // storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        },
                        zipSelectedFile = {
                            viewModel.zipSelectedFile(it, context)
                        },
                        setProduct = {product->
                            viewModel.product.value = product
                            viewModel.updateProduct(
                                product.id,
                                product = OnUpdateProduct(
                                    name = product.name,
                                    description=product.description,
                                    price = product.price,
                                    mas=product.mas,
                                    supply = product.supply,
                                    unit = product.unit
                                )
                            )

                        },
                        productAttachedFile = productAttachedFile,
                        fileZippingLoading = fileZippingLoading
                    )
                }

              /*  permissionDialogQueue
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
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                activity,
                                permission
                            ),
                            onDismiss = viewModel::dismissDialog,
                            onOkClick = {
                                viewModel.dismissDialog()

                                when (permission) {
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

                    }*/






                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,

                    ) {

                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .verticalScroll(scrollState),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        ) {

                                when (typeOfPost.value) {
                                    "video" -> {
                                            VideoPlayer(
                                                videoUrl = loadedVideoUri.value,
                                                context = context
                                            )

                                    }

                                    "image" -> {

                                        LoadedImagesPager(
                                            selectedImages = loadedImages.value
                                        )
                                    }


                                    else -> {
                                        Image(
                                            painter = painterResource(id = R.drawable.empty_plate),
                                            contentDescription = null
                                        )
                                    }
                                }

                        }

                      /*  Row(
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
                                Icon(
                                    painterResource(
                                        id = R.drawable.baseline_photo_24
                                    ),
                                    contentDescription = null,
                                    tint = Color.White
                                )

                            }

                            IconButton(modifier = Modifier
                                .weight(1f),
                                onClick = {
                                    val permissionCheckResult =
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.CAMERA
                                        )
                                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                        cameraLauncher.launch(uri)
                                    } else {
                                        // Request a permission
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }


                                }) {
                                Icon(
                                    painterResource(
                                        id = R.drawable.baseline_photo_camera_24
                                    ),
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                            }
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
                                Icon(
                                    painterResource(
                                        id = R.drawable.baseline_videocam_24
                                    ),
                                    contentDescription = null,
                                    tint = Color.White
                                )

                            }
                        }*/
                        TitleAndDescription(
                            postCategory=category.value,
                            title = title,
                            description = description,
                            radioOptions = radioOptions,
                            selectCategory = {
                                category.value = it
                            }
                        )

                        if (haveProduct.value==1 && viewModel.product.value!=null) {
                            ProductPreview(
                                product = viewModel.product.value!!,
                                removeProduct = {
                                    removeDialogShow.value = true

                                },
                                editProduct = {
                                    showDialog.value = true
                                },
                                onNavigateTpProductDetailScreen = { prod ->
                                    val productJson = viewModel.jsonStringOfProduct(prod)
                                    val route = Screen.ProductDetail.route + "/" + productJson
                                    onNavigateTpProductDetailScreen(route)
                                }
                            )
                        } else {
                            Button(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = ButtonDefaults.buttonColors(Color.LightGray),
                                onClick = {
                                    showDialog.value = true

                                }) {
                                Text(
                                    text = stringResource(id = R.string.attach_product),
                                    color = Color.White
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_attach_file_24),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }



                        Button(
                            enabled = !loading,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 100.dp),
                            colors = ButtonDefaults.buttonColors(Color.Green),
                            onClick = {

                                if (title.value == "" || description.value == "") {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.free_field_warning),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    val finalPost = OnUpdatePost(
                                        title = title.value,
                                        category = category.value,
                                        description = description.value,
                                        longDataAdded = System.currentTimeMillis(),
                                        haveProduct =
                                        if (viewModel.product.value != null) {
                                            1
                                        } else {
                                            0
                                        }

                                    )

                                    viewModel.updatePost(
                                        postId = postId!!,
                                        post = finalPost
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

            BackHandler {

                exitDialogShow.value = true

            }


        }
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
        border = BorderStroke(1.dp, color = Color.LightGray)
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
    selectedImages:SnapshotStateList<Uri?>
){
    Log.i("SELECTED",selectedImages.toList().toString())
    val pagerState = rememberPagerState(pageCount = {
        selectedImages.size
    })
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun LoadedImagesPager(
    selectedImages:List<Uri?>
){
    Log.i("SELECTED",selectedImages.toList().toString())
    val pagerState = rememberPagerState(pageCount = {
        selectedImages.size
    })
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
    postCategory:String,
    title:MutableState<String>,
    description:MutableState<String>,
    radioOptions:List<String>,
    selectCategory:(String)->Unit
){

    val expandedCategory = remember { mutableStateOf(false)}
    val selectedCat = remember { mutableStateOf(radioOptions[0])}
    val selectCategoryText= remember {
        mutableStateOf(postCategory)
    }

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
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation =CardDefaults.cardElevation(10.dp),
        colors=CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = {
                expandedCategory.value = !expandedCategory.value
            })
    ) {
        Column {


            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = selectCategoryText.value,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    if (expandedCategory.value)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            if (expandedCategory.value) {

                radioOptions.forEach { productType ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    selectCategory(productType)
                                    selectedCat.value = productType
                                    expandedCategory.value = false
                                    selectCategoryText.value = productType
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (productType==selectedCat.value),
                            onClick = { selectCategory(productType) }

                        )
                        Text(
                            text = productType,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

            }
        }
    }
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

fun getResourceUri(resources: Resources, resourceID: Int): Uri {
    return Uri.parse(
        "android.resource://" + resources.getResourcePackageName(resourceID) + "/" +
                resources.getResourceTypeName(resourceID) + '/'
                + resources.getResourceEntryName(resourceID)
    )
}
