package com.denbond7.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * @author Denis Bondarenko
 *         Date: 5/27/22
 *         Time: 6:21 PM
 *         E-mail: DenBond7@gmail.com
 */
@Dao
interface ItemDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<ItemEntity>

    @Query("SELECT * FROM user WHERE uid IN (:uids)")
    fun loadAllByIds(uids: IntArray): List<ItemEntity>

    @Query(
        "SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1"
    )
    fun findByName(first: String, last: String): ItemEntity

    @Insert
    fun insertAll(vararg users: ItemEntity)

    @Delete
    fun delete(user: ItemEntity)
}
