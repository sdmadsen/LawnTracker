package com.sdmadsen.steve.lawntracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.util.*

class MainActivity : AppCompatActivity() {

    private val newMowActivityRequestCode = 1
    private lateinit var mowViewModel: MowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MowListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider.
        mowViewModel = ViewModelProviders.of(this).get(MowViewModel::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mowViewModel.allWords.observe(this, Observer { mows ->
            // Update the cached copy of the mows in the adapter.
            mows?.let { adapter.setMows(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewMowActivity::class.java)
            startActivityForResult(intent, newMowActivityRequestCode)
        }

        val fab2 = findViewById<FloatingActionButton>(R.id.fab2)
        fab2.setOnClickListener {
            mowViewModel.deleteAll()
            Toast.makeText(
                applicationContext,
                R.string.delete_all_mows,
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newMowActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val mow = Mow(UUID.randomUUID().toString(), Direction.getDirectionByText(data.getStringExtra(NewMowActivity.EXTRA_REPLY)), null, Status.PENDING, java.sql.Time(Calendar.getInstance().getTime().getTime()))
                mowViewModel.insert(mow)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
