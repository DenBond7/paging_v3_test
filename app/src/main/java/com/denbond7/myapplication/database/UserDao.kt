package com.denbond7.myapplication.database

import androidx.paging.PagingSource
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
interface UserDao {
    @Insert
    suspend fun insert(entities: Iterable<User>)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user WHERE uid = :uid")
    suspend fun deleteByUid(uid: Int): Int

    @Query("DELETE FROM user")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM user ORDER BY uid ASC")
    fun getUsers(): PagingSource<Int, User>

    @Query("SELECT COUNT(*) FROM user")
    suspend fun getUsersCount(): Int
}
