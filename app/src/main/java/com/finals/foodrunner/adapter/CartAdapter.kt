package com.finals.foodrunner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finals.foodrunner.databinding.CartItemBinding
import com.finals.foodrunner.objects.MenuItem

class CartAdapter() : ListAdapter<MenuItem, CartAdapter.ViewHolder>(CartComparator()) {
    class CartComparator : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(
            oldItem: MenuItem,
            newItem: MenuItem
        ): Boolean {
            return oldItem.sno == newItem.sno
        }

        override fun areContentsTheSame(
            oldItem: MenuItem,
            newItem: MenuItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(private val binding:CartItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem){
            binding.apply {
                name.text=menuItem.name
                price.text="₹ ${menuItem.price}/-"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false);
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curr=getItem(position)
        if(curr!=null){
            holder.bind(curr)
        }
    }
}