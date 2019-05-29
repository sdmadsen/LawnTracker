package com.sdmadsen.steve.lawntracker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MowListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<MowListAdapter.MowViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mows = emptyList<Mow>() // Cached copy of mows
    private val context = context

    inner class MowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mowItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MowViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return MowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MowViewHolder, position: Int) {
        val current = mows[position]
        holder.mowItemView.text = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", current.date_created).toString() + " : " + current.direction.text
        holder.mowItemView.setOnClickListener(View.OnClickListener {
            val mowIntent = MowActivity.newIntent(context)
            mowIntent.putExtra("refId", current.refId)
            context.startActivity(mowIntent)
        })
    }

    internal fun setMows(mows: List<Mow>) {
        this.mows = mows
        notifyDataSetChanged()
    }

    override fun getItemCount() = mows.size
}