package com.denbond7.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Denis Bondarenko
 *         Date: 5/27/22
 *         Time: 6:20 PM
 *         E-mail: DenBond7@gmail.com
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?
)
