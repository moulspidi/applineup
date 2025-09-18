package com.fvbib.lineup

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.fvbib.lineup.databinding.ActivityHomeBinding
import com.fvbib.lineup.ui.ArbitrosActivity
import com.fvbib.lineup.ui.EntrenadoresActivity
import com.fvbib.lineup.ui.InstruccionesActivity

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(b.root)

        val deportes = listOf("VOLLEY", "BEACH VOLLEY", "SNOW VOLLEY")
        b.spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, deportes)

        b.btnEntrenadores.setOnClickListener { startActivity(Intent(this, EntrenadoresActivity::class.java)) }
        b.btnArbitros.setOnClickListener { startActivity(Intent(this, ArbitrosActivity::class.java)) }
        b.btnInstrucciones.setOnClickListener { startActivity(Intent(this, InstruccionesActivity::class.java)) }
    }
}
