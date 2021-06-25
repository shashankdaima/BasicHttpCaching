package com.finals.foodrunner.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finals.foodrunner.R
import com.finals.foodrunner.databinding.FoodItemElementBinding
import com.finals.foodrunner.objects.MenuItem


class MenuListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<MenuItem, MenuListAdapter.ViewHolder>(MenuComparator()) {
    inner class ViewHolder(private val binding: FoodItemElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem) {
            binding.apply {
                name.text = menuItem.name
                price.text = "Price:" + menuItem.price.toString() + "/- Rs"
                button2.text = "Add"
                if(!menuItem.isOrder){
                    button2.backgroundTintList = ColorStateList.valueOf(
                        itemView.resources.getColor(
                            R.color.dark_red
                        )
                    );
                    button2.setText("Add")
                    button2.setTextColor(ColorStateList.valueOf(itemView.resources.getColor(R.color.whitegrey)))

                }
                else{
                    button2.backgroundTintList = ColorStateList.valueOf(
                        itemView.resources.getColor(
                            R.color.grey
                        )
                    );
                    button2.text = "Remove"
                    button2.setTextColor(ColorStateList.valueOf(itemView.resources.getColor(R.color.black)))
                }
                button2.setOnClickListener {
                    if(menuItem.isOrder){
                        button2.backgroundTintList = ColorStateList.valueOf(
                            itemView.resources.getColor(
                                R.color.dark_red
                            )
                        );
                        button2.setText("Add")
                        button2.setTextColor(ColorStateList.valueOf(itemView.resources.getColor(R.color.whitegrey)))

                    }
                    else{
                        button2.backgroundTintList = ColorStateList.valueOf(
                            itemView.resources.getColor(
                                R.color.grey
                            )
                        );
                        button2.setText("Remove")
                        button2.setTextColor(ColorStateList.valueOf(itemView.resources.getColor(R.color.black)))
                    }
                    listener.onItemClick(menuItem)
                }


            }
        }
    }

    class MenuComparator : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem.sno == newItem.sno
        }

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FoodItemElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    interface OnItemClickListener {
        fun onItemClick(menuItem: MenuItem);
    }
}
