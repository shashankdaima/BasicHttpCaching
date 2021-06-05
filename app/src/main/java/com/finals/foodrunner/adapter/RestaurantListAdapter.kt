package com.finals.foodrunner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finals.foodrunner.R
import com.finals.foodrunner.objects.RestaurantObject
import com.finals.foodrunner.databinding.RestaurantItemBinding
import com.squareup.picasso.Picasso

class RestaurantListAdapter : ListAdapter<RestaurantObject,RestaurantListAdapter.ViewHolder>(RestaurantComparator()) {
    class ViewHolder(private val binding: RestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurantObject: RestaurantObject) {
            binding.apply {

                restaurantName.text = restaurantObject.name
                restaurantCost.text = "Rs."+restaurantObject.cost.toString()+"/- Per Person"
                restaurantRating.text = restaurantObject.rating.toString()
                if(restaurantObject.isFavourite){
                    restaurantFav.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources,R.drawable.ic_fav,null))
                }
                else{
                    restaurantFav.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources,R.drawable.ic_unfav,null))

                }

                if(restaurantObject.photo!=null){

                    ResourcesCompat.getDrawable(itemView.resources,R.drawable.food_item_placeholder,null)?.let {
                        Picasso.get().load(restaurantObject.photo).placeholder(
                            it
                        ).into(restaurantImage)
                    };
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

    class RestaurantComparator:DiffUtil.ItemCallback<RestaurantObject>(){
        override fun areItemsTheSame(
            oldItem: RestaurantObject,
            newItem: RestaurantObject
        ): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RestaurantObject,
            newItem: RestaurantObject
        ): Boolean {
            return oldItem==newItem

        }

    }


}