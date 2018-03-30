package ru.evotor.egais.api.example

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.evotor.egais.api.example.orginfo.OrgInfoListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewOrgInfo.setOnClickListener {
            startActivity(Intent(this@MainActivity, OrgInfoListActivity::class.java))
        }
    }
}
