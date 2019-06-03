package com.sdmadsen.steve.lawntracker

import android.content.Context
import android.content.Intent
import android.icu.util.UniversalTimeScale.toLong
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MowActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var refId: String = ""
    private var mow: Mow = Mow(
        UUID.randomUUID().toString(), Direction.HORIZONTAL, null, Status.STARTED, Calendar.getInstance())

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
        refId = intent.getStringExtra("refId")
        val mowViewModel: MowViewModel = ViewModelProviders.of(this).get(MowViewModel::class.java)

        val mowStatus: Spinner = findViewById(R.id.status)
        mowStatus.adapter = ArrayAdapter<Status>(this, android.R.layout.simple_spinner_item, Status.getMowStatusValues())
        mowStatus.onItemSelectedListener = this

        val mowDirection: Spinner = findViewById(R.id.direction)
        mowDirection.adapter = ArrayAdapter<Direction>(this, android.R.layout.simple_spinner_item, Direction.values())
        mowDirection.onItemSelectedListener = this


        val mowTrimmed: CheckBox = findViewById(R.id.trimmed)
        mowTrimmed.setOnClickListener { onCheckChange() }

        (GlobalScope as CoroutineScope).launch {
            mow = mowViewModel.oneMow(refId)
            val mowTitle: TextView = findViewById(R.id.title)
            mowTitle.text = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", mow.date_created).toString()

            mowStatus.setSelection(mow.status.ordinal)
            mowStatus.isEnabled = false


            mowDirection.setSelection(mow.direction.ordinal)

            mowTrimmed.isChecked = mow.trimmed ?: false


            val timeElapsed: TextView = findViewById(R.id.timeElapsed)
            val timeElapsed_milli: Long = mowViewModel.timeElapsed(refId).toLong()
            val timeElapsed_string = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeElapsed_milli),
                TimeUnit.MILLISECONDS.toMinutes(timeElapsed_milli) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeElapsed_milli)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(timeElapsed_milli) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeElapsed_milli)))

            timeElapsed.text = timeElapsed_string


        }.invokeOnCompletion {
            updateStatus(mow.status)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Delete Mow")

            builder.setMessage("Are you sure you want to delete this mow?")

            builder.setPositiveButton("Delete"){_,_ ->
                mowViewModel.deleteMow(mow)
                Toast.makeText(
                    applicationContext,
                    R.string.delete_mow,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            builder.setNeutralButton("Cancel"){_,_ -> }

            val dialog: AlertDialog = builder.create()

            dialog.show()
        }


    }

    private fun updateMowStatus(status: Status){
        val builder = AlertDialog.Builder(this)

        builder.setTitle(status.text + " Mow")

        builder.setMessage("Are you sure you want to " + status.text + " this mow?")

        val mowViewModel: MowViewModel = ViewModelProviders.of(this).get(MowViewModel::class.java)

        builder.setPositiveButton(status.text){_,_ ->
            (GlobalScope as CoroutineScope).launch {
                mowViewModel.updateMow(
                    Mow(
                        mow.refId,
                        mow.direction,
                        mow.trimmed,
                        Status.processStatus(status),
                        mow.date_created
                    )
                )
                mowViewModel.insertTimeLog(
                    TimeLog(
                        UUID.randomUUID().toString(),
                        mow.refId,
                        status,
                        Calendar.getInstance()
                    )
                )
            }
            updateStatus(Status.processStatus(status))
        }

        builder.setNeutralButton("Cancel"){_,_ -> }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun updateStatus(status: Status){
        val mowStatus: Spinner = findViewById(R.id.status)
        mowStatus.setSelection(status.ordinal)

        val fab2 = findViewById<FloatingActionButton>(R.id.fab2)
        val fab3 = findViewById<FloatingActionButton>(R.id.fab3)

        if (status == Status.PENDING || status == Status.PAUSED) {
            fab2.setImageResource(android.R.drawable.ic_media_play)
            fab2.setOnClickListener {
                updateMowStatus(Status.START)
            }

            fab3.hide()
        } else if (status == Status.STARTED) {
            fab2.setImageResource(android.R.drawable.ic_media_pause)
            fab2.setOnClickListener {
                updateMowStatus(Status.PAUSE)
            }

            fab3.show()
            fab3.setOnClickListener {
                updateMowStatus(Status.STOP)
            }
        } else {
            fab2.hide()
            fab3.hide()
        }
    }

    private fun onCheckChange(){
        val mowViewModel: MowViewModel = ViewModelProviders.of(this).get(MowViewModel::class.java)

        (GlobalScope as CoroutineScope).launch {
            val mowTrimmed: CheckBox = findViewById(R.id.trimmed)

            mowViewModel.updateMow(
                Mow(
                    mow.refId,
                    mow.direction,
                    mowTrimmed.isChecked,
                    mow.status,
                    mow.date_created)).invokeOnCompletion {
                mow = mowViewModel.oneMow(refId)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        val mowViewModel: MowViewModel = ViewModelProviders.of(this).get(MowViewModel::class.java)

        (GlobalScope as CoroutineScope).launch {
            val mowStatus: Spinner = findViewById(R.id.status)
            val mowDirection: Spinner = findViewById(R.id.direction)

            mowViewModel.updateMow(
                Mow(
                    mow.refId,
                    Direction.getDirectionByText(mowDirection.selectedItem.toString()),
                    mow.trimmed,
                    Status.getStatusByText(mowStatus.selectedItem.toString()),
                    mow.date_created)).invokeOnCompletion {
                mow = mowViewModel.oneMow(refId)
            }


        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}

