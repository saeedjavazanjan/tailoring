package com.saeeed.devejump.project.tailoring.interactor.upload_post

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.CreatedPost
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.PostMapper
import com.saeeed.devejump.project.tailoring.utils.GetFileOfUri
import com.saeeed.devejump.project.tailoring.utils.GetPathFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class UploadPostFunctions(
   val  retrofitService: RetrofitService,
    val getFileOfUri: GetFileOfUri,
    val dtoMapper:PostMapper

) {

    @SuppressLint("SuspiciousIndentation")
    fun zipSelectedFiles(
        uris: List<Uri?>,
        outputZipFilePath: String,
        context:Context

    ): Flow<DataState<String>> = flow<DataState<String>> {
        emit(DataState.loading())
        val buffer = ByteArray(1024)
        val filesToCompress= mutableListOf<String?>()
        val getPathFromUri= GetPathFromUri.instance

        try {
                uris.forEach { uri ->
                    filesToCompress.add(getPathFromUri.trigger(uri!!, context))
                }
                val fos = FileOutputStream(outputZipFilePath)
                val zos = ZipOutputStream(fos)

                filesToCompress.forEach { file ->
                    val ze = ZipEntry(File(file).name)
                    zos.putNextEntry(ze)
                    val `in` = FileInputStream(file)
                    while (true) {
                        val len = `in`.read(buffer)
                        if (len <= 0) break
                        zos.write(buffer, 0, len)
                    }

                    `in`.close()
                }

                zos.closeEntry()
                zos.close()
                emit(DataState.success(outputZipFilePath))
             } catch (e: Exception) {
            e.printStackTrace()
            emit(DataState.error(e.message?:"خطایی رخ داده است"))
        }

    }.flowOn(Dispatchers.IO)



    fun uploadPost(
        token:String?,
        post:CreatedPost
    ):Flow<DataState<Post>> = flow{
        emit(DataState.loading())

        val videoPart=getFileOfUri.getVideoFileFromUri(post.videoUri)
        val ImagesPart =convertListOfUriToListOfFiles(post.featuredImage)
       val result= retrofitService.uploadPost(
            token=token,
            title = post.title!!.toRequestBody(),
            category = post.category!!.toRequestBody(),
            postType = post.postType!!.toRequestBody(),
            description = post.description!!.toRequestBody(),
            longDataAdded = post.longDataAdded.toString().toRequestBody(),
            haveProduct = post.haveProduct.toString().toRequestBody(),
            Video = videoPart,
            FeaturedImages = ImagesPart
        )

        if (result.isSuccessful){
            emit(DataState.success(dtoMapper.mapToDomainModel(result.body()!!)))
        }else if (result.code()==401){
            emit(DataState.error("شما دسترسی لازم را ندارید"))
        }else{
            try {
                val errMsg = result.errorBody()?.string()?.let {
                    JSONObject(it).getString("error") // or whatever your message is
                } ?: run {
                    emit(DataState.error( result.code().toString()))
                }
                emit(DataState.error(errMsg.toString()))
            }catch (e:Exception){
                emit(DataState.error("خطای سرور"))


            }
        }


    }

    private fun convertListOfUriToListOfFiles(uris:List<Uri?>):List<MultipartBody.Part>{
        val result= mutableListOf<MultipartBody.Part>()
        uris.forEach {uri->
            result.add(getFileOfUri.getImageFileFromUri(uri!!))

        }
        return result
    }

}


/* emit(DataState.loading())
       val outputZipFile = File.createTempFile("out", ".zip")

       for (file in filesPath ){
           val inputDirectory = File(file)
           ZipOutputStream(BufferedOutputStream(FileOutputStream(outputZipFile))).use { zos ->
               inputDirectory.walkTopDown().forEach { file ->
                   val zipFileName =
                       file.absolutePath.removePrefix(inputDirectory.absolutePath)
                           .removePrefix("/")
                   val entry = ZipEntry("$zipFileName${(if (file.isDirectory) "/" else "")}")
                   zos.putNextEntry(entry)
                   if (file.isFile) {
                       file.inputStream().use { fis -> fis.copyTo(zos) }
                   }
               }
               zos.closeEntry()
               zos.close()
               emit(DataState.success(zos))

           }

       }*/
