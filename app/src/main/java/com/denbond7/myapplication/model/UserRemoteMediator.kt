package com.denbond7.myapplication.model

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.denbond7.myapplication.database.AppDatabase
import com.denbond7.myapplication.database.User
import kotlinx.coroutines.delay
import java.util.*

/**
 * @author Denis Bondarenko
 *         Date: 5/28/22
 *         Time: 11:13 AM
 *         E-mail: DenBond7@gmail.com
 */
@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(private val context: Context, private val roomDatabase: AppDatabase) :
    RemoteMediator<Int, User>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        try {
            val userDao = roomDatabase.userDao()
            return when (loadType) {
                LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    //userDao.deleteAll()
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
                LoadType.APPEND -> {
                    delay(2000)
                    val pageSize = state.config.pageSize
                    val insertCandidate = mutableListOf<User>()
                    for (i in 0..pageSize) {
                        insertCandidate.add(
                            User(
                                firstName = UUID.randomUUID().toString().substring(0, 15),
                                lastName = UUID.randomUUID().toString().substring(0, 15)
                            )
                        )
                    }
                    userDao.insert(insertCandidate)
                    MediatorResult.Success(endOfPaginationReached = false)
                }
            }
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
}
