package com.saeeed.devejump.project.tailoring.presentation.ui.register

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.components.progress_bar.DotsFlashing
import com.saeeed.devejump.project.tailoring.presentation.ui.splash.SplashViewModel
import com.saeeed.devejump.project.tailoring.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterDialog(
    registerShowDialog: (Boolean) -> Unit,
    ) {
    val registerViewModel = hiltViewModel<RegisterViewModel>()

    val showType = remember {
        mutableStateOf("register")
    }

    val phoneNumber = remember {
        mutableStateOf("")
    }

    val userName = remember {
        mutableStateOf("")
    }

    val password= remember {
        mutableStateOf("")
    }


    val loading = registerViewModel.loading.value

    var stateOfLoginPasswordRequest=  registerViewModel.stateOfLoginPasswordRequest.value
    var stateOfRegisterPasswordRequest=registerViewModel.stateOfRegisterPasswordRequest.value


    if(stateOfLoginPasswordRequest || stateOfRegisterPasswordRequest){
        showType.value="EnterPassword"
    }



    val composableScope = rememberCoroutineScope()
    val scaffoldState= rememberScaffoldState()

        Dialog(
            onDismissRequest = {
                registerShowDialog(false)
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            )

        ) {

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp),
                    scaffoldState = scaffoldState
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                    )
                    {
                        when (showType.value) {
                            "register" -> {
                                Column {
                                    ConstraintLayout(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .align(Alignment.CenterHorizontally)
                                    ) {
                                        val (avatarHolder, gustUser) = createRefs()
                                        GlideImage(
                                            model = placeholder(R.drawable.empty_plate),
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
                                        Text(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .constrainAs(gustUser) {
                                                    start.linkTo(avatarHolder.end)
                                                    top.linkTo(avatarHolder.top)
                                                    bottom.linkTo(avatarHolder.bottom)
                                                    end.linkTo(parent.end)

                                                },
                                            text = stringResource(id = R.string.gust_user),
                                            color = Color.DarkGray
                                        )


                                    }

                                    Button(
                                        colors = ButtonDefaults.buttonColors(Color.Blue),
                                        shape = RoundedCornerShape(5.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        onClick = {
                                            showType.value = "signUp"
                                        }) {
                                        Text(text = stringResource(id = R.string.signUp))
                                    }

                                    Button(
                                        colors = ButtonDefaults.buttonColors(Color.Green),
                                        shape = RoundedCornerShape(5.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        onClick = {
                                            showType.value = "signIn"
                                        }) {
                                        Text(text = stringResource(id = R.string.signIn))
                                    }

                                }
                            }

                            "signUp" -> {
                                Column {
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .fillMaxWidth(0.9f),
                                        shape = MaterialTheme.shapes.medium,
                                        value = phoneNumber.value,
                                        singleLine = true,
                                        maxLines = 1,
                                        label = {
                                            Text(
                                                text = stringResource(id = R.string.enter_phone_number),
                                                color = Color.Gray
                                            )
                                        },
                                        onValueChange = {
                                            phoneNumber.value = it
                                        },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Phone,
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
                                        value = userName.value,
                                        singleLine = true,
                                        maxLines = 1,
                                        label = {
                                            Text(
                                                text = stringResource(id = R.string.enter_user_name),
                                                color = Color.Gray
                                            )
                                        },
                                        onValueChange = {
                                            userName.value = it
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
                                    if (loading) {
                                        DotsFlashing()
                                    } else {
                                        Button(
                                            colors = ButtonDefaults.buttonColors(Color.Blue),
                                            shape = RoundedCornerShape(5.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            onClick = {
                                                registerViewModel.registerPasswordRequest(
                                                    userName = userName.value,
                                                    phoneNumber = phoneNumber.value,
                                                    scaffoldState,
                                                    composableScope
                                                )
                                            }) {
                                            Text(text = stringResource(id = R.string.signUp))
                                        }
                                    }

                                }
                            }

                            "signIn" -> {
                                Column {
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .fillMaxWidth(0.9f),
                                        shape = MaterialTheme.shapes.medium,
                                        value = phoneNumber.value,
                                        singleLine = true,
                                        maxLines = 1,
                                        label = {
                                            Text(
                                                text = stringResource(id = R.string.enter_phone_number),
                                                color = Color.Gray
                                            )
                                        },
                                        onValueChange = {
                                            phoneNumber.value = it
                                        },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Phone,
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
                                    if (loading) {
                                        DotsFlashing()
                                    } else {
                                        Button(
                                            colors = ButtonDefaults.buttonColors(Color.Green),
                                            shape = RoundedCornerShape(5.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            onClick = {
                                                registerViewModel.loginPasswordRequest(
                                                    phoneNumber.value,
                                                    scaffoldState,
                                                    composableScope
                                                )
                                            }) {
                                            Text(text = stringResource(id = R.string.signIn))
                                        }
                                    }
                                }

                            }
                            "EnterPassword"->{
                                Column{
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .fillMaxWidth(0.9f),
                                        shape = MaterialTheme.shapes.medium,
                                        value = password.value,
                                        singleLine = true,
                                        maxLines = 1,
                                        label = {
                                            Text(
                                                text = stringResource(id = R.string.received_password),
                                                color = Color.Gray
                                            )
                                        },
                                        onValueChange = {
                                            password.value = it
                                        },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Phone,
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
                                    if (loading) {
                                        Box(
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        ) {
                                            DotsFlashing()

                                        }
                                    } else {
                                        Button(
                                            colors = ButtonDefaults.buttonColors(Color.Green),
                                            shape = RoundedCornerShape(5.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            onClick = {
                                                registerViewModel.loginPasswordRequest(
                                                    phoneNumber.value,
                                                    scaffoldState,
                                                    composableScope
                                                )
                                            }) {
                                            Text(text = stringResource(id = R.string.signIn))
                                        }
                                    }
                                }


                            }
                        }

                    }
                }
                BackHandler {
                    when (showType.value) {
                        "signIn" -> {
                            showType.value = "register"
                        }

                        "signUp" -> {
                            showType.value = "register"
                        }

                        "register" -> {
                            registerShowDialog(false)
                        }
                        "EnterPassword"->{
                            registerShowDialog(false)
                            registerViewModel.stateOfLoginPasswordRequest.value=false
                            registerViewModel.stateOfRegisterPasswordRequest.value=false
                        }
                    }
                }
            }
        }


}