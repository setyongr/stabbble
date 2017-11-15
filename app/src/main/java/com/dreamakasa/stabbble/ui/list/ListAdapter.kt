package com.dreamakasa.stabbble.ui.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.RealmRecyclerViewAdapter
import com.dreamakasa.stabbble.data.model.*
import io.realm.OrderedRealmCollection
import io.realm.RealmObject
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter<T: RealmObject>(item: OrderedRealmCollection<T>?):
        RealmRecyclerViewAdapter<T, ListAdapter<T>.ViewHolder>(item, true){

    override fun onBindViewHolder(holder: ListAdapter<T>.ViewHolder, position: Int) = holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter<T>.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun bind(data: RealmObject?) = with(itemView){

            var avatar_url = ""
            var name_val = ""
            var bio_val = ""

            when (data) {
                is Followers -> {
                    avatar_url = data.avatar_url.toString()
                    name_val = data.name.toString()
                    bio_val = data.bio.toString()
                }
                is Following -> {
                    avatar_url = data.avatar_url.toString()
                    name_val = data.name.toString()
                    bio_val = data.bio.toString()
                }
                is Fans -> {
                    avatar_url = data.avatar_url.toString()
                    name_val = data.name.toString()
                    bio_val = data.bio.toString()
                }
                is Friends -> {
                    avatar_url = data.avatar_url.toString()
                    name_val = data.name.toString()
                    bio_val = data.bio.toString()
                }
                is NewFollower -> {
                    avatar_url = data.avatar_url.toString()
                    name_val = data.name.toString()
                    bio_val = data.bio.toString()
                }
                is NewUnfollower -> {
                    avatar_url = data.avatar_url.toString()
                    name_val = data.name.toString()
                    bio_val = data.bio.toString()
                }
                is NotFollowingBack -> {
                    avatar_url = data.avatar_url.toString()
                    name_val = data.name.toString()
                    bio_val = data.bio.toString()
                }
            }

            Glide.with(context).load(avatar_url).into(avatar)
            name.text = name_val
            bio.text = bio_val
        }
    }
}