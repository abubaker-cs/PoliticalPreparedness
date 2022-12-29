package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {

    // onCreateViewHolder is called when the RecyclerView needs a new ViewHolder of the given type
    // to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    //DONE: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    //DONE: Create ElectionViewHolder
    class ElectionViewHolder(var binding: ItemElectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //DONE: Add companion object to inflate ViewHolder (from)
        companion object {

            fun from(parent: ViewGroup): ElectionViewHolder {

                // Inflate view and return ViewHolder
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemElectionBinding.inflate(layoutInflater, parent, false)
                return ElectionViewHolder(binding)

            }

        }

        // Adding binding values
        fun bind(listener: ElectionListener, election: Election) {

            // Election
            binding.election = election

            // Election click listener
            binding.clickListener = listener

            // executePendingBindings() is used to make sure that all the views are updated immediately
            binding.executePendingBindings()

        }

    }

    //DONE: Create ElectionDiffCallback
    class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
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
         * @see ElectionDiffCallback.areItemsTheSame
         */
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
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
         * @see ElectionDiffCallback.areContentsTheSame
         */
        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem == newItem
        }

    }

    //DONE: Create ElectionListener
    class ElectionListener(val clickListener: (election: Election) -> Unit) {

        // onClick method for ElectionListener
        fun onClick(election: Election) = clickListener(election)

    }


}
