package com.sdmadsen.steve.lawntracker

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
        holder.mowItemView.text = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", current.date_created).toString() + " : " + current.status.text

        when(current.status) {
            Status.PENDING -> holder.mowItemView.setBackgroundColor(Color.CYAN)
            Status.STARTED -> holder.mowItemView.setBackgroundColor(Color.GREEN)
            Status.PAUSED -> holder.mowItemView.setBackgroundColor(Color.YELLOW)
            Status.COMPLETED -> holder.mowItemView.setBackgroundColor(Color.RED)
        }

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