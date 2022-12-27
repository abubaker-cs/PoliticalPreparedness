package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient

class CivicsHttpClient : OkHttpClient() {

    companion object {

        //DONE: Place your API Key Here
        // private const val API_KEY = CIVICS_API_KEY
        private const val API_KEY = "AIzaSyB_OAEuKO_bimjoNmnQYrtvz32nYRpeQtA"

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
