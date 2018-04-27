package ru.evotor.egais.api.example.client_settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.widget.Toast
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.provider.client_settings.ClientSettingsContract

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
        val fsRarId = contentResolver.query(ClientSettingsContract.FSRAR_ID_URI, null, null, null, null)
                ?.let { cursor ->
                    cursor.moveToFirst()
                    cursor.getString(cursor.getColumnIndex(ClientSettingsContract.FSRAR_ID_COLUMN_NAME))
                }

        println("fsrarid = $fsRarId")
        Toast.makeText(this, "fsrarid = \"$fsRarId\"", Toast.LENGTH_SHORT).show()
    }
}
