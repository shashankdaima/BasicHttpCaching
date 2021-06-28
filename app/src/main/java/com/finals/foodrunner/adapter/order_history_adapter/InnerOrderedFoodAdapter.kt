package com.finals.foodrunner.adapter.order_history_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finals.foodrunner.databinding.CartItemBinding
import com.finals.foodrunner.objects.FoodItem


class InnerOrderedFoodAdapter(val list: List<FoodItem>) :
    RecyclerView.Adapter<InnerOrderedFoodAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(foodItem: FoodItem){
            binding.apply {
                name.text=foodItem.name
                price.text=foodItem.cost
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position!=RecyclerView.NO_POSITION){
            val currElement = list[position]
            holder.bind(currElement)
        }
    }

    override fun getItemCount() = list.size
}