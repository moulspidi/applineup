package com.fvbib.lineup.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fvbib.lineup.R
import com.fvbib.lineup.model.LineupPayload
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class EntrenadoresActivity : AppCompatActivity() {

    private lateinit var tabs: TabLayout
    private lateinit var btnVaciar: Button
    private lateinit var btnGenerar: Button
    private lateinit var edTeam: EditText
    private lateinit var rbA: RadioButton
    private lateinit var rbB: RadioButton
    private lateinit var edI: EditText
    private lateinit var edII: EditText
    private lateinit var edIII: EditText
    private lateinit var edIV: EditText
    private lateinit var edV: EditText
    private lateinit var edVI: EditText

    private var currentSet = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_entrenadores)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "AppLineup V.1"

            // -- findViewById (ids deben existir en activity_entrenadores.xml) --
            tabs = findViewById(R.id.tabs)
            btnVaciar = findViewById(R.id.btnVaciar)
            btnGenerar = findViewById(R.id.btnGenerar)
            edTeam = findViewById(R.id.edTeam)
            rbA = findViewById(R.id.rbA)
            rbB = findViewById(R.id.rbB)
            edI = findViewById(R.id.edI)
            edII = findViewById(R.id.edII)
            edIII = findViewById(R.id.edIII)
            edIV = findViewById(R.id.edIV)
            edV = findViewById(R.id.edV)
            edVI = findViewById(R.id.edVI)

            if (tabs.tabCount == 0) for (i in 1..5) tabs.addTab(tabs.newTab().setText("SET $i"))
            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) { currentSet = tab.position + 1 }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })

            btnVaciar.setOnClickListener { clearFields() }
            btnGenerar.setOnClickListener { generateAndShowQR() }

        } catch (e: Throwable) {
            AlertDialog.Builder(this)
                .setTitle("Error iniciando pantalla")
                .setMessage(e.message ?: e.toString())
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun clearFields() {
        listOf(edI, edII, edIII, edIV, edV, edVI).forEach { it.setText("") }
        edTeam.setText("")
        rbA.isChecked = true
        tabs.getTabAt(0)?.select()
        currentSet = 1
    }

    private fun generateAndShowQR() {
        try {
            val team = edTeam.text?.toString()?.trim().orEmpty().ifEmpty { "TEAM" }
            val side = if (rbA.isChecked) "A" else "B"
            val payload = LineupPayload(
                teamCode = team,
                set = currentSet,
                side = side,
                players = listOf(
                    edI.text?.toString().orEmpty(),
                    edII.text?.toString().orEmpty(),
                    edIII.text?.toString().orEmpty(),
                    edIV.text?.toString().orEmpty(),
                    edV.text?.toString().orEmpty(),
                    edVI.text?.toString().orEmpty()
                )
            )
            val json = Gson().toJson(payload)
            val bmp: Bitmap = BarcodeEncoder().encodeBitmap(json, BarcodeFormat.QR_CODE, 900, 900)

            // Cargamos dialog_qr sin ViewBinding para evitar crashes
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_qr, null)
            val img = dialogView.findViewById<ImageView>(R.id.img)
            val txt = dialogView.findViewById<TextView>(R.id.txt)
            img.setImageBitmap(bmp)
            txt.text = json

            AlertDialog.Builder(this)
                .setTitle("QR Set %d - %s".format(currentSet, side))
                .setView(dialogView)
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
