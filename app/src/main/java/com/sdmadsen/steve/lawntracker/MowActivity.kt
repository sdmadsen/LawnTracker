package com.sdmadsen.steve.lawntracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MowActivity : AppCompatActivity() {

    companion object {

        /**
         * New default intent for showing settings.
         */
        fun newIntent(context: Context): Intent {
            return Intent(context, MowActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mow)
        val intent = intent
        val refId = intent.getStringExtra("refId")
        val mowViewModel: MowViewModel = ViewModelProviders.of(this).get(MowViewModel::class.java)
        //val mow: Mow = mowViewModel.oneMow(refId)

        (GlobalScope as CoroutineScope).launch {
            val mow: Mow = mowViewModel.oneMow(refId)
            val mowTitle: TextView = findViewById(R.id.title)
            mowTitle.text = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", mow.date_created).toString()

            val mowStatus: TextView = findViewById(R.id.status)
            mowStatus.text = mow.status.text

            val mowDirection: TextView = findViewById(R.id.direction)
            mowDirection.text = mow.direction.text

            val mowTrimmed: TextView = findViewById(R.id.trimmed)
            mowTrimmed.text = if (mow.trimmed == null) "Not Set" else mow.trimmed.toString()
        }

    }
}
