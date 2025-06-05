package com.example.materialplayer.util

import android.net.Uri
import android.provider.DocumentsContract
import androidx.core.net.toUri


/** canonical ENCODED ‘document’-URI базовой папки */
fun Uri.docBaseEncoded(): String =
    if (toString().contains("/document/")) toString()
    else DocumentsContract.buildDocumentUriUsingTree(
        this, DocumentsContract.getTreeDocumentId(this)
    ).toString()

fun Uri.safeDocId(): String =
    runCatching { DocumentsContract.getDocumentId(this) }       // encoded URI?
        .getOrElse { Uri.decode(toString()).substringAfter("/document/") }

/** «красивое» имя, не падает на decoded-URI */
val Uri.displayName: String
    get() = safeDocId()
        .substringAfterLast('/') // последняя часть пути
        .substringAfterLast(':') // убираем “primary:”
