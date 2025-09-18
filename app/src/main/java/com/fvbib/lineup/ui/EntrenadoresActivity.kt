package com.fvbib.lineup.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fvbib.lineup.databinding.ActivityEntrenadoresBinding
import com.fvbib.lineup.databinding.DialogQrBinding
import com.fvbib.lineup.model.LineupPayload
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.android.material.tabs.TabLayout

class EntrenadoresActivity : AppCompatActivity() {
    private lateinit var b: ActivityEntrenadoresBinding
    private var currentSet: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEntrenadoresBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "AppLineup V.1"

        for (i in 1..5) b.tabs.addTab(b.tabs.newTab().setText("SET $i"))
        b.tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) { currentSet = tab.position + 1 }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        b.btnVaciar.setOnClickListener { clearFields() }
        b.btnGenerar.setOnClickListener { showQR() }
    }

    private fun clearFields() {
        listOf(b.edI, b.edII, b.edIII, b.edIV, b.edV, b.edVI).forEach { it.setText("") }
    }

    private fun showQR() {
        val team = b.edTeam.text.toString().trim().ifEmpty { "TEAM" }
        val side = if (b.rbA.isChecked) "A" else "B"
        val payload = LineupPayload(
            teamCode = team,
            set = currentSet,
            side = side,
            players = listOf(
                b.edI.text.toString(),
                b.edII.text.toString(),
                b.edIII.text.toString(),
                b.edIV.text.toString(),
                b.edV.text.toString(),
                b.edVI.text.toString()
            )
        )
        val json = Gson().toJson(payload)
        val bmp: Bitmap = BarcodeEncoder().encodeBitmap(json, BarcodeFormat.QR_CODE, 900, 900)

        val dialogBinding = DialogQrBinding.inflate(LayoutInflater.from(this))
        dialogBinding.img.setImageBitmap(bmp)
        dialogBinding.txt.text = json

        AlertDialog.Builder(this)
            .setTitle("QR Set %d - %s".format(currentSet, side))
            .setView(dialogBinding.root)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed(); return true
    }
}
