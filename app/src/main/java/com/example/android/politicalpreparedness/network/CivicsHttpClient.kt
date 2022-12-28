package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.utils.Constants.CIVICS_API_KEY
import okhttp3.OkHttpClient

class CivicsHttpClient : OkHttpClient() {

    companion object {

        //DONE: Place your API Key Here
        private const val API_KEY = CIVICS_API_KEY

        fun getClient(): OkHttpClient {

            //
            return Builder()
                .addInterceptor { chain ->

                    //
                    val original = chain.request()

                    //
                    val url = original
                        .url()
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()

                    //
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()

                    //
                    chain.proceed(request)

                }
                .build()
        }

    }

}
