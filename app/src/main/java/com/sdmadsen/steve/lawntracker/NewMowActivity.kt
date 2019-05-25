package com.sdmadsen.steve.lawntracker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class NewMowActivity : AppCompatActivity() {

    private lateinit var editMowView: Spinner

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_mow)
        editMowView = findViewById(R.id.edit_mow)


        val spinner: Spinner = findViewById(R.id.edit_mow) as Spinner
        spinner.setAdapter(ArrayAdapter<Direction>(this, android.R.layout.simple_spinner_item, Direction.values()))

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()

            val word = editMowView.getSelectedItem().toString()
            replyIntent.putExtra(EXTRA_REPLY, word)
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.mowlistsql.REPLY"
    }
}