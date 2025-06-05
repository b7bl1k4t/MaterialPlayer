package com.example.materialplayer.util

import android.net.Uri
import android.provider.DocumentsContract


val String.asUri get() = Uri.parse(this)

/** canonical ENCODED ‘document’-URI базовой папки */
fun Uri.docBaseEncoded(): String =
    if (toString().contains("/document/")) toString()
    else DocumentsContract.buildDocumentUriUsingTree(
        this, DocumentsContract.getTreeDocumentId(this)
    ).toString()

/** то же, но DECODED – для БД/LIKE */
fun Uri.docBaseDecoded(): String = Uri.decode(docBaseEncoded())

fun Uri.safeDocId(): String =
    runCatching { DocumentsContract.getDocumentId(this) }       // encoded URI?
        .getOrElse { Uri.decode(toString()).substringAfter("/document/") }

fun Uri.parentEncoded(): Uri? {
    val docId = safeDocId()
    val cut = docId.lastIndexOf('/')
    if (cut == -1) return null          // уже корень
    val parentId = docId.substring(0, cut)
    return DocumentsContract.buildDocumentUriUsingTree(this, parentId)
}

/** «красивое» имя, не падает на decoded-URI */
val Uri.displayName: String
    get() = safeDocId()
        .substringAfterLast('/')        // последняя часть пути
        .substringAfterLast(':')        // убираем “primary:”
