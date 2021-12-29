package ru.evotor.egais.api.example.client_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.query.settings.ClientSettingsQuery

class ClientSettingsActivity : AppCompatActivity() {

    val getFsRarIdButton by lazy { findViewById<AppCompatButton>(R.id.get_fsrarid_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_settings)

        getFsRarIdButton.setOnClickListener {
            getFsRarId()
        }
    }

    private fun getFsRarId() {
        val fsRarId = ClientSettingsQuery().getFsRarId(this)

        println("fsrarid = $fsRarId")
        Toast.makeText(this, "fsrarid = \"$fsRarId\"", Toast.LENGTH_SHORT).show()
    }
}
