package com.simplemobiletools.gallery.pro.data.extensions

import com.simplemobiletools.gallery.pro.data.helpers.*
import com.simplemobiletools.gallery.pro.data.models.*

fun ArrayList<Medium>.getDirMediaTypes(): Int {
    var types = 0
    if (any { it.isImage() }) {
        types += TYPE_IMAGES
    }

    if (any { it.isVideo() }) {
        types += TYPE_VIDEOS
    }

    if (any { it.isGIF() }) {
        types += TYPE_GIFS
    }

    if (any { it.isRaw() }) {
        types += TYPE_RAWS
    }

    if (any { it.isSVG() }) {
        types += TYPE_SVGS
    }

    if (any { it.isPortrait() }) {
        types += TYPE_PORTRAITS
    }

    return types
}

fun ArrayList<ThumbnailItem>.getMediums(): ArrayList<Medium>{
    val result = this.takeWhile { it is Medium } as List<Medium>
    return ArrayList(result)
}

///Jet
fun ArrayList<FolderItem>.getDirectories(): ArrayList<Directory>{
    val result = arrayListOf<Directory>()
    this.forEach{ item ->
        if (item is Directory)
            result.add(item)
        else if(item is DirectoryGroup){
            val dirs = item.innerDirs.clone() as ArrayList<Directory>
            //dirs.forEach{d -> d.groupName = "" }
            result.addAll(dirs)
        }
    }
    return result
}

val ArrayList<Medium>.names: ArrayList<String> get(){
    return ArrayList(this.map { it.name })
}


fun <T>ArrayList<T>.takeLast(): T{
    val item = this.last()
    this.removeAt(this.size - 1)
    return item
}

fun <T>ArrayList<T>.takeFirst(): T{
    val item = this.first()
    this.removeAt(0)
    return item
}

inline fun <reified Y>ArrayList<*>.filterByType(): ArrayList<Y>{
    return this.filter { it is Y } as ArrayList<Y>
}
//////
