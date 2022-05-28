package com.denbond7.myapplication.model

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.denbond7.myapplication.database.AppDatabase
import com.denbond7.myapplication.database.User
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * @author Denis Bondarenko
 *         Date: 5/28/22
 *         Time: 11:13 AM
 *         E-mail: DenBond7@gmail.com
 */
@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val roomDatabase: AppDatabase,
    private val maxSize: Int
) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        Log.d("DDDD", "$loadType")
        try {
            val userDao = roomDatabase.userDao()
            return when (loadType) {
                LoadType.PREPEND, LoadType.REFRESH -> {
                    MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val countOfExistingUsersBefore = userDao.getUsersCount()
                    if (countOfExistingUsersBefore < maxSize) {
                        delay(2000)
                        val pageSize = state.config.pageSize
                        val insertCandidate = mutableListOf<User>()
                        for (i in 0 until pageSize) {
                            insertCandidate.add(
                                User(
                                    firstName = FIRST_NAMES[Random.nextInt(0, FIRST_NAMES.size)],
                                    lastName = LAST_NAMES[Random.nextInt(0, LAST_NAMES.size)]
                                )
                            )
                        }
                        userDao.insert(insertCandidate)
                    }
                    val countOfExistingUsersAfter = userDao.getUsersCount()
                    MediatorResult.Success(endOfPaginationReached = countOfExistingUsersAfter >= maxSize)
                }
            }
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    companion object {
        val FIRST_NAMES = arrayOf(
            "James",
            "Robert",
            "John",
            "Michael",
            "David",
            "William",
            "Richard",
            "Joseph",
            "Thomas",
            "Charles",
            "Christopher",
            "Daniel",
            "Matthew",
            "Anthony",
            "Mark",
            "Donald",
            "Steven",
            "Paul",
            "Andrew",
            "Joshua",
            "Kenneth",
            "Kevin",
            "Brian",
            "George",
            "Timothy",
            "Ronald",
            "Edward",
            "Jason",
            "Jeffrey",
            "Ryan",
            "Jacob",
            "Gary",
            "Nicholas",
            "Eric",
            "Jonathan",
            "Stephen",
            "Larry",
            "Justin",
            "Scott",
            "Brandon",
            "Benjamin",
            "Samuel",
            "Gregory",
            "Alexander",
            "Frank",
            "Patrick",
            "Raymond",
            "Jack",
            "Dennis",
            "Jerry"
        )
        val LAST_NAMES = arrayOf(
            "SMITH",
            "JOHNSON",
            "WILLIAMS",
            "BROWN",
            "JONES",
            "GARCIA",
            "MILLER",
            "DAVIS",
            "RODRIGUEZ",
            "MARTINEZ",
            "HERNANDEZ",
            "LOPEZ",
            "GONZALEZ",
            "WILSON",
            "ANDERSON",
            "THOMAS",
            "TAYLOR",
            "MOORE",
            "JACKSON",
            "MARTIN",
            "LEE",
            "PEREZ",
            "THOMPSON",
            "WHITE",
            "HARRIS",
            "SANCHEZ",
            "CLARK"
        )
    }
}
