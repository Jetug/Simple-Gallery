package com.simplemobiletools.gallery.pro.ui.dialogs

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.room.util.CursorUtil.getColumnIndexOrThrow
import com.google.android.flexbox.FlexboxLayout
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.extensions.setupDialogStuff
import com.simplemobiletools.commons.views.MyEditText
import com.simplemobiletools.gallery.pro.R
import com.simplemobiletools.gallery.pro.data.extensions.launchIO
import com.simplemobiletools.gallery.pro.data.interfaces.ResultListener
import com.simplemobiletools.gallery.pro.data.jetug.workers.createMediaMoveTask
import kotlinx.android.synthetic.main.dialog_task.view.*
import java.io.File

class TaskDialog(val activity: BaseSimpleActivity, val onComplete: () -> Unit = {}) {
    private val view: View = activity.layoutInflater.inflate(R.layout.dialog_task, null)
    private val dialog: AlertDialog

    init {
        if(activity is ResultListener) activity.onResult = ::onActivityResult
        dialog = AlertDialog.Builder(activity)
//            .setPositiveButton(R.string.ok, ::onPositiveButtonClick)
//            .setNegativeButton(R.string.cancel, null)
            .create().apply {
                activity.setupDialogStuff(view, this, R.string.edit_date) {
                    onCreate()
                }
            }
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }

    private fun onCreate(){
          view.apply {
              addBtn.setOnClickListener {
                  val textView = createTextView()
                  flowLayout.addView(textView, layoutParams)
              }

              selectSourcePath.setOnClickListener {
                  val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                  activity.startActivityForResult(intent, 0)
              }

              selectPath.setOnClickListener {
                  val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                  activity.startActivityForResult(intent, 1)
              }
              //okBtn.isEnabled = false
              okBtn.setOnClickListener(::onPositiveButtonClick)
          }
      }

    private fun createTextView(): MyEditText {
        val textView = MyEditText(activity).apply {
          layoutParams = FlexboxLayout.LayoutParams(80,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return textView
    }


    private var sourceUri: String = ""
    private var destinationUri: String = ""

    private fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            sourceUri = uriToPath(data?.data)
            view.sourcePath.setText(sourceUri)
        }
        else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            destinationUri = uriToPath(data?.data)
            view.path.setText(destinationUri)
        }
    }

    private fun uriToPath(uri: Uri?): String {
        if(uri == null) return ""
//        val file = File(uri.path)
//        val split = file.path.split(":".toRegex()).toTypedArray() //split the path.
//        return split[1]

        var realPath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = activity.applicationContext.contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                realPath = it.getString(columnIndex)
            }
            cursor.close()
        }
        return realPath ?: ""
    }

    private fun onPositiveButtonClick(v: View) = launchIO{
        if (sourceUri.isEmpty() || destinationUri.isEmpty()) return@launchIO
        activity.createMediaMoveTask(sourceUri, destinationUri)
        dialog.cancel()
    }
}