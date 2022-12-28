package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.utils.Constants.CIVICS_API_KEY
import okhttp3.OkHttpClient

class CivicsHttpClient : OkHttpClient() {

    companion object {

        //DONE: Place your API Key Here
        private const val API_KEY = CIVICS_API_KEY

        fun getClient(): OkHttpClient {

            // Builder() is a factory method that returns a new OkHttpClient.Builder instance.
            return Builder()

                // Add an API Interceptor
                .addInterceptor { chain ->

                    // Get the original request
                    val original = chain.request()

                    val url = original

                        // url() returns an HttpUrl instance for the original request
                        .url()

                        // newBuilder() returns a new HttpUrl.Builder instance.
                        .newBuilder()

                        // Add the API Key to the URL
                        .addQueryParameter("key", API_KEY)

                        // build() returns an HttpUrl instance for the new URL
                        .build()

                    // request() returns a new Request instance for the original request
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()

                    chain.proceed(request)

                }
                .build()
        }

    }

}
