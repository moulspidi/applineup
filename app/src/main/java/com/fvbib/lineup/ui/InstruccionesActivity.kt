package com.fvbib.lineup.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fvbib.lineup.databinding.ActivityInstruccionesBinding

class InstruccionesActivity : AppCompatActivity() {
    private lateinit var b: ActivityInstruccionesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityInstruccionesBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Instrucciones"
        b.text.text = "1) Entrenadores: complete I..VI, equipo, set y lado; pulse GENERAR QR.\n" + 
                      "2) Árbitros: escanee el QR para ver la alineación.\n" + 
                      "3) Use los tabs para cambiar de set."
    }
    override fun onSupportNavigateUp(): Boolean { onBackPressedDispatcher.onBackPressed(); return true }
}
