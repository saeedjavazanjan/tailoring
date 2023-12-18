package com.saeeed.devejump.project.tailoring.interactor.upload_post

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class UploadPost {
    fun main(args: Array<String>) {
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
        out.close()}
    fun zipSelectedFiles(args: Array<String>) {
        val inputDirectory = File("/home/fred")
        val outputZipFile = File.createTempFile("out", ".zip")
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputZipFile))).use { zos ->
            inputDirectory.walkTopDown().forEach { file ->
                val zipFileName = file.absolutePath.removePrefix(inputDirectory.absolutePath).removePrefix("/")
                val entry = ZipEntry( "$zipFileName${(if (file.isDirectory) "/" else "" )}")
                zos.putNextEntry(entry)
                if (file.isFile) {
                    file.inputStream().use { fis -> fis.copyTo(zos) }
                }
            }
        }

    }
}