package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //DONE: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    //DONE: Add select all election query
    @Query("SELECT * FROM election_table ORDER BY electionDay")
    suspend fun getAllElections(): List<Election>

    //DONE: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElection(id: Int): Election?

    //DONE: Add delete query
    @Delete
    suspend fun deleteElection(election: Election)

    //DONE: Add clear query
    @Query("DELETE FROM election_table")
    suspend fun clear()

}
