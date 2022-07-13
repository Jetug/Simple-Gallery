package com.simplemobiletools.gallery.pro.ui.dialogs.raname

import com.google.vr.sdk.widgets.video.deps.na
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.dialogs.rename.RenameDialogBase
import com.simplemobiletools.commons.dialogs.rename.RenameItemsDialogBase
import com.simplemobiletools.gallery.pro.data.extensions.context.renameGroup
import com.simplemobiletools.gallery.pro.data.models.DirectoryGroup

class RenameGroupsDialog(activity: BaseSimpleActivity, private val dirGroup: ArrayList<DirectoryGroup>, val callback: (newNames: ArrayList<String>) -> Unit): RenameItemsDialogBase(activity) {
    override fun onPositiveClick() {
        val names = arrayListOf<String>()

        dirGroup.forEach { group ->
            val name = group.name
            val newName = if (append)
                "$name$valueToAdd"
            else
                "$valueToAdd$name"
            names.add(newName)
            activity.renameGroup(group, newName)
        }
        callback(names)
        builder.dismiss()
    }
}
