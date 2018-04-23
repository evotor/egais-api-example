package ru.evotor.egais.api.example.client_settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.widget.Toast
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.query.ClientSettingsQuery

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
        val clientSetting = ClientSettingsQuery()
                .settingName.equal(ClientSettingsQuery.COLUMN_FSRAR_ID)
                .execute(this)
                .let { cursor ->
                    cursor.moveToFirst()
                    cursor.getValue()
                }

        println("fsrarid = ${clientSetting.fsRarId}")
        Toast.makeText(this, "fsrarid = \"${clientSetting.fsRarId}\"", Toast.LENGTH_SHORT).show()
    }
}
