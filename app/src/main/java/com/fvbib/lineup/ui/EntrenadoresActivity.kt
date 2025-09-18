package com.fvbib.lineup.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fvbib.lineup.databinding.ActivityEntrenadoresBinding
import com.fvbib.lineup.databinding.DialogQrBinding
import com.fvbib.lineup.model.LineupPayload
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class EntrenadoresActivity : AppCompatActivity() {
    private lateinit var b: ActivityEntrenadoresBinding
    private var currentSet: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEntrenadoresBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "AppLineup V.1"

        // Tabs SET 1..5
        if (b.tabs.tabCount == 0) {
            for (i in 1..5) b.tabs.addTab(b.tabs.newTab().setText("SET $i"))
        }
        b.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) { currentSet = tab.position + 1 }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        b.btnVaciar.setOnClickListener { clearFields() }
        b.btnGenerar.setOnClickListener { generateAndShowQR() }
    }

    private fun clearFields() {
        listOf(b.edI, b.edII, b.edIII, b.edIV, b.edV, b.edVI).forEach { it.setText("") }
        b.edTeam.setText("")
        b.rbA.isChecked = true
        b.tabs.getTabAt(0)?.select()
        currentSet = 1
    }

    private fun generateAndShowQR() {
        try {
            val team = b.edTeam.text?.toString()?.trim().orEmpty().ifEmpty { "TEAM" }
            val side = if (b.rbA.isChecked) "A" else "B"
            val payload = LineupPayload(
                teamCode = team,
                set = currentSet,
                side = side,
                players = listOf(
                    b.edI.text?.toString().orEmpty(),
                    b.edII.text?.toString().orEmpty(),
                    b.edIII.text?.toString().orEmpty(),
                    b.edIV.text?.toString().orEmpty(),
                    b.edV.text?.toString().orEmpty(),
                    b.edVI.text?.toString().orEmpty()
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
        } catch (e: Exception) {
            AlertDialog.Builder(this)
                .setTitle("Error al generar QR")
                .setMessage(e.message ?: "Error desconocido")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
