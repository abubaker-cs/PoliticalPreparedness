@file:Suppress("unused")

package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * setElectionDay() will set the election day text view to the election day date
 */
@BindingAdapter("day")
fun TextView.setElectionDay(date: Date?) {

    // Get the current instance of the calendar
    val calendar = Calendar.getInstance()

    // Set the calendar to the date passed in
    date?.let {

        // Set the calendar to the date passed in
        calendar.time = it

        // Format the date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Set the text to the month and day
        text = dateFormat.format(calendar.time)

    }
}

/**
 * followOrUnfollow() will set the button text to "Follow Election" or "Unfollow Election" depending
 * on the election
 */
@BindingAdapter("followOrUnfollow")
fun Button.followOrUnfollow(hasSaved: Boolean?) {

    // Set the button text to "Follow Election" or "Unfollow Election" depending on the election
    hasSaved?.let {
        text =
            if (it) resources.getString(R.string.unfollow_btn) else resources.getString(R.string.follow_btn)
    }

}

/**
 * setElectionData() is a binding adapter that sets the data for the ElectionListAdapter
 */
@BindingAdapter("listData")
fun RecyclerView.setElectionData(data: List<Election>?) {

    // Set the adapter to ElectionListAdapter
    val adapter = adapter as ElectionListAdapter

    // Set the data to the adapter
    adapter.submitList(data)

}

/**
 * bindApiStatus() will take the ApiStatus and set the visibility of statusImageView
 */
@BindingAdapter("civicsApiStatus")
fun bindApiStatus(statusImageView: ImageView, status: CivicsApiStatus?) {

    // When the API status is loading, hide the successful and error image views
    when (status) {

        // Loading: Keep the status image view visible and set the source to:
        // @res/drawable/loading_animation.xml
        CivicsApiStatus.LOADING -> {

            // Set the visibility of the status statusImageView to VISIBLE
            statusImageView.visibility = View.VISIBLE

            // Set the image resource to @drawable/loading_animation
            statusImageView.setImageResource(R.drawable.loading_animation)

        }

        // Error: Keep the status image view visible but change the image source to:
        // @res/drawable/ic_connection_error.xml
        CivicsApiStatus.ERROR -> {

            // Set the visibility of the status statusImageView to VISIBLE
            statusImageView.visibility = View.VISIBLE

            // Set the image resource to @drawable/ic_connection_error
            statusImageView.setImageResource(R.drawable.ic_connection_error)

        }

        // Done: Hide the status image view
        CivicsApiStatus.DONE -> {

            // Hide the statusImageView
            statusImageView.visibility = View.GONE

        }

        // Else: Log statement
        else -> {

            // Log a message
            Timber.d("ELSE", "Else-Block in bindApiStatus...")

        }

    }

}
