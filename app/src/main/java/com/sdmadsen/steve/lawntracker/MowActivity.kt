package com.sdmadsen.steve.lawntracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MowActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var refId: String = ""
    private var mow: Mow = Mow(
        UUID.randomUUID().toString(), Direction.HORIZONTAL, null, Status.PENDING, java.sql.Time(
            Calendar.getInstance().getTime().getTime()))

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
        mowTrimmed.setOnClickListener(View.OnClickListener {
            onCheckChange()
        })

        (GlobalScope as CoroutineScope).launch {
            mow = mowViewModel.oneMow(refId)
            val mowTitle: TextView = findViewById(R.id.title)
            mowTitle.text = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", mow.date_created).toString()

            mowStatus.setSelection(mow.status.ordinal)
            mowStatus.isEnabled = false


            mowDirection.setSelection(mow.direction.ordinal)

            mowTrimmed.isChecked = mow.trimmed ?: false

        }

    }

    private fun onCheckChange(){
        val mowViewModel: MowViewModel = ViewModelProviders.of(this).get(MowViewModel::class.java)

        (GlobalScope as CoroutineScope).launch {
            val mowTrimmed: CheckBox = findViewById(R.id.trimmed)

            mowViewModel.updateMows(
                Mow(
                    mow.refId,
                    mow.direction,
                    mowTrimmed.isChecked,
                    mow.status,
                    mow.date_created))
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        val mowViewModel: MowViewModel = ViewModelProviders.of(this).get(MowViewModel::class.java)

        (GlobalScope as CoroutineScope).launch {
            val mowStatus: Spinner = findViewById(R.id.status)
            val mowDirection: Spinner = findViewById(R.id.direction)

            mowViewModel.updateMows(
                Mow(
                    mow.refId,
                    Direction.getDirectionByText(mowDirection.selectedItem.toString()),
                    mow.trimmed,
                    Status.getStatusByText(mowStatus.selectedItem.toString()),
                    mow.date_created))
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}

