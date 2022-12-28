package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.DateAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// DONE: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()

    // Formats dates using RFC 3339 , which is formatted like 2015-09-26T18:23:50.250Z.
    // This adapter is null-safe.
    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())

    // Formats dates using the DateAdapter
    .add(DateAdapter())

    // Formats elections using the ElectionAdapter
    .add(ElectionAdapter())

    // It is a factory for Kotlin classes generated by the Kotlin Moshi codegen compiler plugin.
    .add(KotlinJsonAdapterFactory())

    // Create the Moshi object
    .build()

private val retrofit = Retrofit.Builder()

    // Add the MoshiConverterFactory
    .addConverterFactory(MoshiConverterFactory.create(moshi))

    // Add the CoroutineCallAdapterFactory
    .addCallAdapterFactory(CoroutineCallAdapterFactory())

    // CivicsHttpClient is the client that will make the HTTP requests
    .client(CivicsHttpClient.getClient())

    // Set the base URL
    .baseUrl(BASE_URL)

    // Build the Retrofit object
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    //DONE: Add elections API Call
    @GET("elections")
    suspend fun getElections(): ElectionResponse

    //DONE: Add voterinfo API Call
    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: Int
    ): VoterInfoResponse

    //DONE: Add representatives API Call
    @GET("representatives")
    suspend fun getRepresentatives(@Query("address") address: String): RepresentativeResponse

}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}
