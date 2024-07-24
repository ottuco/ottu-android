package com.ottu.customization

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ottu.databinding.ItemInnerThemeComponentBinding

class ComponentsAdapter(
    private val items: List<ComponentItem>,
    private val onClick: (ComponentItem) -> Unit,
) : RecyclerView.Adapter<ComponentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int) = ViewHolder(
        ItemInnerThemeComponentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    inner class ViewHolder(
        private val binding: ItemInnerThemeComponentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ComponentItem) {
            binding.apply {
                tvItemTitle.text = item.name
                root.setOnClickListener { onClick.invoke(item) }
            }
        }

    }

}