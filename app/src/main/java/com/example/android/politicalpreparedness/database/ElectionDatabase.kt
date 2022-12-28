package com.example.android.politicalpreparedness.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.network.models.Election

@Database(entities = [Election::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ElectionDatabase : RoomDatabase() {

    // electionDao to be used in the repository
    abstract val electionDao: ElectionDao

    companion object {

        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: ElectionDatabase? = null


        fun getInstance(context: Context): ElectionDatabase {

            synchronized(this) {

                var instance = INSTANCE

                // If instance is null, make a new database instance.
                if (instance == null) {

                    // Create database here
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ElectionDatabase::class.java,
                        "election_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance

                }

                // Return instance
                return instance

            }
        }

    }

}
