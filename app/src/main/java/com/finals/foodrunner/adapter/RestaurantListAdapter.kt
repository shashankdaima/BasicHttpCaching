package com.finals.foodrunner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.RestaurantItemBinding
import com.finals.foodrunner.objects.Restaurant

class RestaurantListAdapter(val onItemListener: OnItemListener) :
    ListAdapter<Restaurant, RestaurantListAdapter.ViewHolder>(RestaurantComparator()) {
    inner class ViewHolder(private val binding: RestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurantObject: Restaurant) {
            binding.apply {

                restaurantName.text = restaurantObject.name
                restaurantCost.text =
                    "Rs." + restaurantObject.cost_for_one.toString() + "/- Per Person"
                restaurantRating.text = restaurantObject.rating
                if (restaurantObject.isFavourite) {
                    restaurantFav.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            itemView.resources,
                            R.drawable.ic_fav,
                            null
                        )
                    )
                } else {
                    restaurantFav.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            itemView.resources,
                            R.drawable.ic_unfav,
                            null
                        )
                    )

                }

                if (restaurantObject.image_url != null) {

                    ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.food_item_placeholder,
                        null
                    )?.let {

                        Glide
                            .with(itemView)
                            .load(restaurantObject.image_url)
                            .centerCrop()
                            .placeholder(R.drawable.food_item_placeholder)
                            .into(restaurantImage);
                    };
                }
                restaurantFav.setOnClickListener {
                    if (!restaurantObject.isFavourite) {
                        restaurantFav.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                itemView.resources,
                                R.drawable.ic_fav,
                                null
                            )
                        )
                    } else {
                        restaurantFav.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                itemView.resources,
                                R.drawable.ic_unfav,
                                null
                            )
                        )

                    }
                    onItemListener.onItemFavClickListener(restaurantObject)
                }
                root.setOnClickListener {
                    onItemListener.onItemClickListener(restaurantObject)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class RestaurantComparator : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.name==newItem.name && oldItem.image_url==oldItem.image_url

        }

    }


}

interface OnItemListener {
    fun onItemClickListener(restaurant: Restaurant);
    fun onItemFavClickListener(restaurant: Restaurant);
}
