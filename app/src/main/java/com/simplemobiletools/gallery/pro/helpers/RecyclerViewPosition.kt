package com.simplemobiletools.gallery.pro.helpers

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.simplemobiletools.commons.views.MyGridLayoutManager
import com.simplemobiletools.gallery.pro.extensions.*

class RecyclerViewPosition (val view: RecyclerView?){
    private val rvPosition = arrayListOf<Pair<Int,Int>>()

    fun saveRVPosition(){
        if(view != null) {
            val ox = view.computeHorizontalScrollOffset()
            val oy = view.computeVerticalScrollOffset()
            rvPosition.add(Pair(ox, oy))
            //Toast.makeText(view.context, "save: $oy", Toast.LENGTH_SHORT).show()
        }
    }

    fun restoreRVPosition(){
        if(rvPosition.isNotEmpty() && view != null) {
            val pos = rvPosition.takeLast()
            val layoutManager = view.layoutManager as MyGridLayoutManager
            layoutManager.scrollToPositionWithOffset(pos.first, -pos.second)
            //Toast.makeText(view.context, "restore: $pos.second", Toast.LENGTH_SHORT).show()
        }
    }

}