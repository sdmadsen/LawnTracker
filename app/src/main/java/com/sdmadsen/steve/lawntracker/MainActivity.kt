package com.sdmadsen.steve.lawntracker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.util.*

class MainActivity : AppCompatActivity() {

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
            val refId = UUID.randomUUID().toString()
            val mow = Mow(refId, Direction.HORIZONTAL, null, Status.PENDING, Calendar.getInstance())
            mowViewModel.insertMow(mow).invokeOnCompletion {
                val mowIntent = MowActivity.newIntent(this)
                mowIntent.putExtra("refId", refId)
                startActivity(mowIntent)
            }

        }

        val fab2 = findViewById<FloatingActionButton>(R.id.fab2)
        fab2.setOnClickListener {

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Delete All Mows")

            builder.setMessage("Are you sure you want to delete all mows?")

            builder.setPositiveButton("Delete"){_,_ ->
                mowViewModel.deleteAllMows()
                Toast.makeText(
                    applicationContext,
                    R.string.delete_all_mows,
                    Toast.LENGTH_SHORT
                ).show()
            }

            builder.setNeutralButton("Cancel"){_,_ -> }

            val dialog: AlertDialog = builder.create()

            dialog.show()
        }

    }
}
