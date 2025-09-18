package com.fvbib.lineup.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fvbib.lineup.databinding.ActivityArbitrosBinding
import com.fvbib.lineup.model.LineupPayload
import com.google.gson.Gson
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ArbitrosActivity : AppCompatActivity() {
    private lateinit var b: ActivityArbitrosBinding
    private lateinit var capture: CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityArbitrosBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "LineUp Scanner"

        val barcodeView: DecoratedBarcodeView = b.barcodeScanner
        capture = CaptureManager(this, barcodeView)
        capture.initializeFromIntent(intent, savedInstanceState)

        barcodeView.decodeContinuous { result {
            val txt = result?.text ?: return@decodeContinuous
            try {
                val obj = Gson().fromJson(txt, LineupPayload::class.java)
                val msg = "Team: ${obj.teamCode}\nSet: ${obj.set}  Side: ${obj.side}\nI..VI: ${obj.players.joinToString(", ")}"
                runOnUiThread {
                    AlertDialog.Builder(this)
                        .setTitle("Lineup recibido")
                        .setMessage(msg)
                        .setPositiveButton("OK", null)
                        .show()
                }
            } catch (_: Exception) { /* ignore */ }
        }
    }
    override fun onResume() { super.onResume(); capture.onResume() }
    override fun onPause() { super.onPause(); capture.onPause() }
    override fun onDestroy() { super.onDestroy(); capture.onDestroy() }
    override fun onSaveInstanceState(outState: Bundle) { super.onSaveInstanceState(outState); capture.onSaveInstanceState(outState) }
    override fun onSupportNavigateUp(): Boolean { onBackPressedDispatcher.onBackPressed(); return true }
}
