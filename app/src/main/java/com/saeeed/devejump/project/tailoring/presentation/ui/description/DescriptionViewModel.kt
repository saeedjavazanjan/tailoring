package com.saeeed.devejump.project.tailoring.presentation.ui.description

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Comment
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.interactor.description.GetComments
import com.saeeed.devejump.project.tailoring.interactor.description.GetSewMethod
import com.saeeed.devejump.project.tailoring.interactor.description.UserActivityOnPost
import com.saeeed.devejump.project.tailoring.presentation.components.SnackbarController
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_SEW = "sew.state.sew.key"

@ExperimentalCoroutinesApi
@HiltViewModel
class DescriptionViewModel
@Inject
constructor(
    private val getSewMethod: GetSewMethod,
    private val getComments: GetComments,
    private val connectivityManager: ConnectivityManager,
    @Named("auth_token") private val token: String,
    private val state: SavedStateHandle,
    private val userActivityOnPost: UserActivityOnPost,
): ViewModel(){
    val post: MutableState<Post?> = mutableStateOf(null)

    val loading = mutableStateOf(false)
    val productLoading = mutableStateOf(false)

    val commentSendLoading= mutableStateOf(false)
    val bookMarkState= mutableStateOf(false)
    val liKeState= mutableStateOf(false)
    val likeCount= mutableStateOf(0)
    val product:MutableState<Product?> = mutableStateOf(null)
 /*   private val _comments = MutableLiveData<MutableList<Comment>>()
     val comments: LiveData<MutableList<Comment>>
        get() = _comments*/
  val comments: MutableState<MutableList<Comment>> = mutableStateOf(ArrayList())

    //   var comments= emptyList<PostWitComment>()
    val dialogQueue=DialogQueue()

    init {

        // restore if process dies
        state.get<Int>(STATE_KEY_SEW)?.let{ sewId ->
            onTriggerEvent(SewEvent.GetSewEvent(sewId))
        }
    }

    fun onTriggerEvent(event: SewEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is SewEvent.GetSewEvent -> {
                      //  if(sewMethod.value == null){

                        getSewMethod(event.id)
                            Log.d(TAG, "sewId:${event.id}")

                        //   }
                    }

                    else -> {

                    }
                }
            }catch (e: Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    private fun getSewMethod(id: Int){
        getSewMethod.execute(id, token,connectivityManager.isNetworkAvailable.value).onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { data ->
                post.value = data
                state.set(STATE_KEY_SEW, data.id)
                checkSewBookMarkState()
                checkLikeState()
                getPostComments(data.id)
                likeCount.value=data.like
                if(data.haveProduct==1){
                    getProductOfCurrentPost(data.id)
                }
              //  _comments.value=data.comments.toMutableList()
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getSew: ${error}")
                dialogQueue.appendErrorMessage("An Error Occurred", error)

            }
        }.launchIn(viewModelScope)
    }

    fun getProductOfCurrentPost(postId: Int) {
        getSewMethod.getProductOfCurrentPost(
            postId,
            token,
            connectivityManager.isNetworkAvailable.value
        ).onEach { dataState->
            productLoading.value=dataState.loading
        dataState.data.let {
            product.value=it
        }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("خطای دریافت محصول",it)

            }

        }.catch {
            dialogQueue.appendErrorMessage("خطای دریافت محصول",it.message.toString())
        }.launchIn(viewModelScope)

    }

    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalMaterialApi::class)
     fun bookMark(scaffoldState: ScaffoldState, scope:CoroutineScope) {
        val snackbarController=SnackbarController(scope)

            userActivityOnPost.bookMark(post.value!!.id).onEach { dataState ->
                  dataState.data?.let {
                     snackbarController.getScope().launch {
                         if (it> 0){
                             bookMarkState.value=true
                             snackbarController.showSnackbar(
                                 scaffoldState = scaffoldState,
                                 message =  "با موفقیت ذخیره شد.",
                                 actionLabel ="Ok"
                             )

                         }else{
                             snackbarController.showSnackbar(
                                 scaffoldState = scaffoldState,
                                 message =  "خطایی رخ داده است.",
                                 actionLabel ="Ok"
                             )
                         }

                     }

                  }

                dataState.error?.let { error ->
                    dialogQueue.appendErrorMessage("An Error Occurred", error)
                   // Log.e(TAG, "save in database faild ${error}")

                }

            }.launchIn(viewModelScope)

    }
    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalMaterialApi::class)
    fun removeFromBookMarkDataBase(scaffoldState: ScaffoldState,scope:CoroutineScope) {
        val snackbarController=SnackbarController(scope)

        userActivityOnPost.unBookMark(post.value!!.id).onEach { dataState ->
            dataState.data?.let {
                snackbarController.getScope().launch {
                    if (it > 0){
                        bookMarkState.value=false
                        snackbarController.showSnackbar(
                            scaffoldState = scaffoldState,
                            message =  "از علاقه مندی ها حذف شد.",
                            actionLabel ="Ok"
                        )

                    }else{
                        snackbarController.showSnackbar(
                            scaffoldState = scaffoldState,
                            message =  "خطایی رخ داده است.",
                            actionLabel ="Ok"
                        )
                    }

                }

            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
                // Log.e(TAG, "save in database faild ${error}")

            }

        }.launchIn(viewModelScope)
    }
    fun checkSewBookMarkState(){
            userActivityOnPost.checkBookMarkState(post.value!!.id).onEach { dataState ->

                dataState.data.let {
                   bookMarkState.value=it!!
                     Log.d(TAG, "check bookmark ${post.value!!.id}")

                }
                dataState.error.let {error->
                 //   dialogQueue.appendErrorMessage("An Error Occurred", error!!)
                     Log.e(TAG, "check bookmark ${post.value!!.id}")

                }

            }.launchIn(viewModelScope)


    }

    fun checkLikeState(){
        userActivityOnPost.checkLikeState(post.value!!.id).onEach { dataState ->

            dataState.data.let {
                liKeState.value=it!!
                Log.d(TAG, "check like ${post.value!!.id}")

            }
            dataState.error.let {error->
                //   dialogQueue.appendErrorMessage("An Error Occurred", error!!)
                Log.e(TAG, "check like ${post.value!!.id}")

            }

        }.launchIn(viewModelScope)


    }

    @SuppressLint("SuspiciousIndentation")
    fun likePost() {

        userActivityOnPost.likePost(post.value!!.id, likeCount = (likeCount.value+1).toString()).onEach { dataState ->
            dataState.data?.let {
                    if (it> 0){
                        liKeState.value=true
                        likeCount.value++
                         Log.d(TAG, "like ${it}")


                    }

                }



            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
                // Log.e(TAG, "save in database faild ${error}")

            }

        }.launchIn(viewModelScope)

    }

    @SuppressLint("SuspiciousIndentation")
    fun unLikePost() {

        userActivityOnPost.unLikePost(post.value!!.id, likeCount =(likeCount.value-1).toString()).onEach { dataState ->
            dataState.data?.let {
                if (it> 0){
                    liKeState.value=false
                    likeCount.value--

                    Log.d(TAG, "unlike ${it}")

                }

            }



            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("An Error Occurred", error)
                // Log.e(TAG, "save in database faild ${error}")

            }

        }.launchIn(viewModelScope)

    }
    fun getPostComments(postId:Int){
        getComments.getPostComments(
            postId,
            token,
            connectivityManager.isNetworkAvailable.value)
            .onEach { dataState->
           // loading.value = dataState.loading
            dataState.data?.let {


                    comments.value= it.toMutableList()

               // }

            }
         /*   dataState.error?.let {
                dialogQueue.appendErrorMessage("An Error Occurred", it.toString())
                Log.e(TAG, "postComment ${it}")

            }*/
        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())


        }.launchIn(viewModelScope)

    }

   @SuppressLint("SuspiciousIndentation")
   fun commentOnPost(comment: Comment, postId:Int){
        userActivityOnPost.commentOnPost(comment=comment,postId=postId).onEach { dataState ->
            dataState.loading.let {
                commentSendLoading.value=it

            }
            dataState.data?.let {
                if (it> 0)
                    try {
                        comments.value.add(0,comment)

                    }catch (e:Exception){
                        e.printStackTrace()
                    }
            }


        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

        }.launchIn(viewModelScope)

    }
    @SuppressLint("SuspiciousIndentation")
    fun editComment(comment: Comment, postId:Int){
        userActivityOnPost.editComment(comment=comment,postId=postId).onEach { dataState ->
            dataState.data?.let {
              comments.value.firstOrNull(){
                comments.value.indexOf(it)== comments.value.indexOf(comment)
                }?.comment=comment.comment

            }


        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

        }.launchIn(viewModelScope)

    }

    fun reportCommenet(comment: Comment, postId:Int){
        userActivityOnPost.reportComment(comment, postId =postId ).onEach {dataState ->

            dataState.data?.let {



            }
        }.catch {

        }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("SuspiciousIndentation")
     fun removeComment(comment: Comment, postId:Int, scaffoldState: ScaffoldState, scope:CoroutineScope) {
        val snackbarController = SnackbarController(scope)
        snackbarController.getScope().launch {
            comments.value.remove(comment)
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                //  scaffoldState = scaffoldState,
                message = "کامنت شما حذف شد.",
                actionLabel = "UNDO"
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed ->
                    userActivityOnPost.removeComment(comment = comment, postId = postId)
                        .onEach { dataState ->
                            dataState.data?.let {
                                snackbarController.getScope().launch {
                                    if (it <= 0) {

                                        snackbarController.showSnackbar(
                                            scaffoldState = scaffoldState,
                                            message = "خطایی رخ داده است.",
                                            actionLabel = "Ok"
                                        )
                                    }

                                }
                            }

                        }.catch {
                            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

                        }.launchIn(viewModelScope)
                SnackbarResult.ActionPerformed -> comments.value.add(0,comment)
            }


        }


        }

}