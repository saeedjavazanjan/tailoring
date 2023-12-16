package com.saeeed.devejump.project.tailoring.presentation.ui.upload

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.ImageButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.media3.ui.BuildConfig
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.presentation.components.TopBar
import com.saeeed.devejump.project.tailoring.presentation.components.VideoPlayer
import com.saeeed.devejump.project.tailoring.presentation.navigation.Screen
import com.saeeed.devejump.project.tailoring.presentation.ui.splash.SplashViewModel
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import com.saeeed.devejump.project.tailoring.utils.posts
import java.nio.file.WatchEvent
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
){
    val loading = viewModel.loading.value
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState= rememberScaffoldState()

    val typeOfPost= remember {
        mutableStateOf("")
    }
    var selectedImages=remember { mutableStateListOf<Uri?>(null) }
    val selectedVideoUri = remember { mutableStateOf<Uri?>(null) }

    val pagerState = rememberPagerState(pageCount = {
        selectedImages.size
    })

    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)) {
        selectedImages.apply {
            clear()
            addAll(it)
        }
        typeOfPost.value="photo"
    }
    val context = LocalContext.current






    val videoLauncher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri.let {
            selectedVideoUri.value=uri
            typeOfPost.value="video"

        }
    }

    AppTheme(
        displayProgressBar = loading,
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        dialogQueue = dialogQueue.queue.value,
        scaffoldState = scaffoldState

    ) {
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

                   /* TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .fillMaxWidth(0.9f),
                        shape = MaterialTheme.shapes.medium,
                        value = userBio.value!!,

                        maxLines = 4,
                        onValueChange = {
                            userBio.value = it
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
                    )*/
                }
            }
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