package com.simplemobiletools.gallery.pro.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simplemobiletools.gallery.pro.data.models.DateTaken

@Dao
interface DateTakensDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dateTakens: List<DateTaken>)

    @Query("SELECT full_path, filename, parent_path, date_taken, last_fixed, last_modified FROM date_takens WHERE parent_path = :path COLLATE NOCASE")
    fun getDateTakensFromPath(path: String): List<DateTaken>

    @Query("SELECT full_path, filename, parent_path, date_taken, last_fixed, last_modified FROM date_takens WHERE full_path = :path COLLATE NOCASE")
    fun getDateTakenFromPath(path: String): DateTaken

    @Query("SELECT full_path, filename, parent_path, date_taken, last_fixed, last_modified FROM date_takens")
    fun getAllDateTakens(): List<DateTaken>
}
