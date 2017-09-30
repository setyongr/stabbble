package com.dreamakasa.stabbble.ui.newfollower

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.RealmRecyclerViewAdapter
import com.dreamakasa.stabbble.data.model.NewFollower
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.list_item.view.*

class NewFollowerAdapter(data:OrderedRealmCollection<NewFollower>) : RealmRecyclerViewAdapter<NewFollower, NewFollowerAdapter.ViewHolder>(data, true){
    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(data: NewFollower?) = with(itemView){
            Glide.with(context).load(data?.avatar_url).into(avatar)
            name.text = data?.name
            bio.text = data?.bio
        }
    }
}