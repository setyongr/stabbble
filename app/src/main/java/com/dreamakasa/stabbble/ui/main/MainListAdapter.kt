package com.dreamakasa.stabbble.ui.main

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.inflate
import kotlinx.android.synthetic.main.main_items.view.*

class MainListAdapter(val listItem: MutableList<ListItem> = mutableListOf()): RecyclerView.Adapter<MainListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(listItem[position])

    override fun getItemCount(): Int = listItem.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(parent.inflate(R.layout.main_items))

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(listItem: ListItem) = with(itemView){
            text_title.text = listItem.title
            when(listItem.arrow){
                0 -> text_value.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                1 -> text_value.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_caret_top, 0, 0, 0)
                -1 -> text_value.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_caret_down, 0, 0, 0)
            }
            text_value.text = listItem.value.toString()
        }
    }
}