package com.faircorp

import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.faircorp.service.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// display room detail
class WindowActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //get the room id
        val id = intent.getLongExtra(WINDOW_NAME_PARAM, 0)
        val switch : Switch = findViewById(R.id.switchButton)

        //set the main variables
        var window_name : String
        var window_status : String
        var room_name : String
        var current_temperature : String?

        // call the API to get information
        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().windowsApiService.findById(id).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        val window = it.body()
                        if (window != null) {
                            findViewById<TextView>(R.id.txt_window_name).text = window.name
                            findViewById<TextView>(R.id.txt_room_name).text = window.room.roomName
                            findViewById<TextView>(R.id.txt_window_current_temperature).text =
                                window.room.currentTemperature?.toString()
                            findViewById<TextView>(R.id.txt_window_target_temperature).text =
                                window.room.targetTemperature?.toString()
                            findViewById<TextView>(R.id.txt_window_status).text =
                                window.status.toString()
                            val room_id = window.roomId

                        }
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Error on window loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }

        /* insert switch button */

    }
}