package com.saeeed.devejump.project.tailoring.interactor.upload_post

import android.annotation.SuppressLint
import com.saeeed.devejump.project.tailoring.domain.data.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class UploadPostFunctions() {



    /*fun main(args: Array<String>) {
        var files: Array<String> = arrayOf("/home/matte/theres_no_place.png", "/home/matte/vladstudio_the_moon_and_the_ocean_1920x1440_signed.jpg")
        var out = ZipOutputStream(BufferedOutputStream(FileOutputStream("/home/matte/Desktop/test.zip")))
        var data = ByteArray(1024)
        for (file in files) {
            var fi = FileInputStream(file)
            var origin = BufferedInputStream(fi)
            var entry = ZipEntry(file.substring(file.lastIndexOf("/")))
            out.putNextEntry(entry)
            origin.buffered(1024).reader().forEachLine {
                out.write(data)
            }
            origin.close()
        }
        out.close()}*/
    @SuppressLint("SuspiciousIndentation")
    fun zipSelectedFiles(filesPath:List<String>): Flow<DataState<ZipOutputStream>> = flow<DataState<ZipOutputStream>> {

        emit(DataState.loading())
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

        }

    }.flowOn(Dispatchers.IO)



}