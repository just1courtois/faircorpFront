package com.faircorp

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.faircorp.service.ApiServices
import com.faircorp.model.WindowAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
java.lang.VerifyError: Verifier rejected class com.faircorp.WindowsActivity$onCreate$1: java.lang.Object com.faircorp.WindowsActivity$onCreate$1.invokeSuspend(java.lang.Object) failed to verify: java.lang.Object com.faircorp.WindowsActivity$onCreate$1.invokeSuspend(java.lang.Object): [0x9E] register v2 has type Reference: java.lang.Throwable but expected Precise Reference: kotlin.jvm.internal.Ref$ObjectRef (declaration of 'com.faircorp.WindowsActivity$onCreate$1' appears in /data/app/~~K7jvadRthAsS1We_hrqysA==/com.faircorp-rRIP2OsdFCF-nHd_APsLYw==/base.apk!classes3.dex)
        at com.faircorp.WindowsActivity.onCreate(WindowsActivity.kt:34)
*/

class WindowsActivity : BasicActivity() , OnWindowSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_windows)
        val recyclerView = findViewById<RecyclerView>(R.id.list_windows) // (2)
        val adapter = WindowAdapter(this) // (3)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().windowsApiService.findAll().execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        adapter.update(it.body() ?: emptyList())
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Error on windows loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }


    }

    override fun onWindowSelected(id: Long) {
        val intent = Intent(this, WindowActivity::class.java).putExtra(WINDOW_NAME_PARAM, id)
        startActivity(intent)
    }
}
