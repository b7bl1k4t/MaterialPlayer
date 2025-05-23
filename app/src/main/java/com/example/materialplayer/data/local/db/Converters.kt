package com.example.materialplayer.data.local.db

import android.net.Uri
import androidx.room.TypeConverter
import java.util.Date

class Converters {

    /* Uri  <->  String */
    @TypeConverter
    fun uriToString(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun stringToUri(str: String?): Uri? = str?.let(Uri::parse)

    /* Date (timestamp ms) */
    @TypeConverter
    fun longToDate(value: Long?): Date? =
        value?.let(::Date)

    @TypeConverter
    fun dateToLong(date: Date?): Long? =
        date?.time
}