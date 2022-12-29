package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ItemRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative

/**
 * RepresentativeListAdapter
 */
class RepresentativeListAdapter :
    ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()) {

    // Establish bindings between ViewHolder and Representative data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {

        // Create ViewHolder from item_representative.xml
        return RepresentativeViewHolder.from(parent)

    }

    // Populate ViewHolder with Representative data
    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {

        // Get representative from list
        val item = getItem(position)

        // Populate ViewHolder with representative data
        holder.bind(item)

    }
}

/**
 * RepresentativeViewHolder
 */
class RepresentativeViewHolder(val binding: ItemRepresentativeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    //DONE: Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRepresentativeBinding.inflate(layoutInflater, parent, false)
            return RepresentativeViewHolder(binding)
        }
    }

    fun bind(item: Representative) {

        // Populate view with representative data
        binding.representative = item

        // Fallback image if no photo available
        binding.representativePhoto.setImageResource(R.drawable.ic_profile)

        //DONE: Show social links ** Hint: Use provided helper methods
        item.official.channels?.let {
            showSocialLinks(it)
        }

        //DONE: Show www link ** Hint: Use provided helper methods
        item.official.urls?.let {
            showWWWLinks(it)
        }

        // executePendingBindings() is used to make sure that all the views are properly bound to the data
        // before the RecyclerView tries to recycle them.
        binding.executePendingBindings()

    }

    // Social Links: Facebook + Twitter
    private fun showSocialLinks(channels: List<Channel>) {

        // URL: Facebook
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) {
            enableLink(binding.facebookIcon, facebookUrl)
        }

        // URL: Twitter
        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) {
            enableLink(binding.twitterIcon, twitterUrl)
        }

    }

    // URL: Website
    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.wwwIcon, urls.first())
    }

    // Retrieve: Facebook URL
    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
            .map { channel -> "https://www.facebook.com/${channel.id}" }
            .firstOrNull()
    }

    // Retrieve: Twitter URL
    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
            .map { channel -> "https://www.twitter.com/${channel.id}" }
            .firstOrNull()
    }

    // Enable: Social Link
    private fun enableLink(view: ImageView, url: String) {

        // Show icon
        view.visibility = View.VISIBLE

        // Click listener for social links
        view.setOnClickListener {

            // Create intent to open URL in browser
            setIntent(url)

        }

    }

    // Intent: Open clicked URL
    private fun setIntent(url: String) {

        // Uri.parse() parses a URI string and converts it into a Uri object.
        val uri = Uri.parse(url)

        // Intent.ACTION_VIEW is an action constant that indicates that the user wants to view a URI.
        val intent = Intent(ACTION_VIEW, uri)

        // Start activity
        itemView.context.startActivity(intent)

    }

}

//DONE: Create RepresentativeDiffCallback
class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
    /**
     * Called to check whether two objects represent the same item.
     *
     *
     * For example, if your items have unique ids, this method should check their id equality.
     *
     *
     * Note: `null` items in the list are assumed to be the same as another `null`
     * item and are assumed to not be the same as a non-`null` item. This callback will
     * not be invoked for either of those cases.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the two items represent the same object or false if they are different.
     *
     * @see RepresentativeDiffCallback.areItemsTheSame
     */
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return (oldItem.office == newItem.office) && (oldItem.official == newItem.official)
    }

    /**
     * Called to check whether two items have the same data.
     *
     *
     * This information is used to detect if the contents of an item have changed.
     *
     *
     * This method to check equality instead of [Object.equals] so that you can
     * change its behavior depending on your UI.
     *
     *
     * For example, if you are using DiffUtil with a
     * [RecyclerView.Adapter], you should
     * return whether the items' visual representations are the same.
     *
     *
     * This method is called only if [.areItemsTheSame] returns `true` for
     * these items.
     *
     *
     * Note: Two `null` items are assumed to represent the same contents. This callback
     * will not be invoked for this case.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the contents of the items are the same or false if they are different.
     *
     * @see RepresentativeDiffCallback.areContentsTheSame
     */
    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }

}
