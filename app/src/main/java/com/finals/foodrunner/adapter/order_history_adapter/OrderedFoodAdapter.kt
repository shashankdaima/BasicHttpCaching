package com.finals.foodrunner.adapter.order_history_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finals.foodrunner.databinding.OrderHistoryElementBinding
import com.finals.foodrunner.objects.OrderHistoryElement

class OrderedFoodAdapter :ListAdapter<OrderHistoryElement, OrderedFoodAdapter.ViewHolder>(
    OrderHistoryComparator()
) {
    inner class ViewHolder(val binding: OrderHistoryElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderHistoryElement: OrderHistoryElement) {
            val adapter = InnerOrderedFoodAdapter(orderHistoryElement.food_items)
            binding.apply {
                restaurantName.setText(orderHistoryElement.restaurant_name)
                dateOfOrder.setText(orderHistoryElement.order_placed_at)

                foodList.apply {
                    this.adapter = adapter
                    layoutManager = LinearLayoutManager(itemView.context)
                    setHasFixedSize(true)

                }
            }
        }
    }

    class OrderHistoryComparator : DiffUtil.ItemCallback<OrderHistoryElement>() {
        override fun areItemsTheSame(
            oldItem: OrderHistoryElement,
            newItem: OrderHistoryElement
        ) = oldItem.order_id == newItem.order_id

        override fun areContentsTheSame(
            oldItem: OrderHistoryElement,
            newItem: OrderHistoryElement
        ) = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            OrderHistoryElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curr = getItem(position);
        if (curr != null) {
            holder.bind(curr)
        }
    }
}