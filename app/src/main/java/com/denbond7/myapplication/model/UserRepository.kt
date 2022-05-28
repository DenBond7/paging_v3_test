package com.denbond7.myapplication.model

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.denbond7.myapplication.database.AppDatabase
import com.denbond7.myapplication.database.User

/**
 * @author Denis Bondarenko
 *         Date: 5/28/22
 *         Time: 10:42 AM
 *         E-mail: DenBond7@gmail.com
 */
object UserRepository {
    private const val PAGE_SIZE = 30

    @OptIn(ExperimentalPagingApi::class)
    fun getUsersPager(
        context: Context
    ): Pager<Int, User> {
        val roomDatabase = AppDatabase.getDatabase(context)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { roomDatabase.userDao().getUsers() },
            remoteMediator = UserRemoteMediator(
                roomDatabase = roomDatabase,
                maxSize = PAGE_SIZE
            )
        )
    }
}
