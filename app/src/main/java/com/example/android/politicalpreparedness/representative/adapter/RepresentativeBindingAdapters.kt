package com.example.android.politicalpreparedness.representative.adapter

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.model.Representative

/**
 * RecyclerView binding adapter that updates the list data and list click handler.
 */
fun RecyclerView.setRepresentativeData(data: List<Representative>?) {

    // Add binding adapter to bind the representative data to the RecyclerView
    val adapter = adapter as RepresentativeListAdapter

    // Submit the list to the adapter
    adapter.submitList(data)

}


/**
 * Spinner -----------------------------------------------------------------------------------------
 */

// setNewValue() is used to set the value of the spinner
// Annotate the method that sets the initial value and updates when the value changes using @BindingAdapter
@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {

    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)

    // Get the position of the value in the adapter
    val position = when (adapter.getItem(0)) {

        // If the first item is a string, get the position of the value
        is String -> adapter.getPosition(value)

        // Otherwise, use the current position
        else -> this.selectedItemPosition

    }

    // Set the selection to the position of the value
    if (position >= 0) {
        setSelection(position)
    }

}

// getNewValue() is used to get the value of the spinner
// Annotate the method that reads the value from the view using @InverseBindingAdapter
@InverseBindingAdapter(attribute = "stateValue")
fun Spinner.getNewValue(): String {

    // Get the states array from @res/values/strings.xml
    val states: Array<String> = resources.getStringArray(R.array.states)

    // Return the selected state
    return states[this.selectedItemPosition]

}

// setStateListener is used to set the listener for the spinner
@BindingAdapter("stateValueAttrChanged")
fun setStateListener(spinner: Spinner, stateChange: InverseBindingListener?) {

    // If the stateChange is null, then set the spinner's onItemSelectedListener to null
    if (stateChange == null) {
        spinner.onItemSelectedListener = null
    } else {

        // Create an object of the AdapterView.OnItemSelectedListener interface
        // and override the onItemSelected() and onNothingSelected() methods
        val listener: AdapterView.OnItemSelectedListener =

            object : AdapterView.OnItemSelectedListener {

                // onItemSelected() is called when an item is selected
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {

                    // Call the stateChange listener
                    stateChange.onChange()

                }

                // onNothingSelected() is called when nothing is selected
                override fun onNothingSelected(parent: AdapterView<*>) {

                    // Call the stateChange listener
                    stateChange.onChange()

                }

            }

        // Set the spinner's onItemSelectedListener to the listener
        spinner.onItemSelectedListener = listener

    }
}

/**
 * fetchImage() is used to fetch the image from the url and set it to the image view
 */

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {

    src?.let {

        // Create a uri from the src
        val uri = src.toUri().buildUpon().scheme("https").build()

        //DONE: Add Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
        // Load the image from the uri and set it to the image view
        Glide.with(view.context)
            .load(uri)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .circleCrop()
            .into(view)

    }

}

/**
 * toTypedAdapter() is used to convert the adapter to a typed adapter
 */
inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T> {

    // Create a new array adapter
    return adapter as ArrayAdapter<T>

}
