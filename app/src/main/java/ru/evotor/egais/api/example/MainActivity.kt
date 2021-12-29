package ru.evotor.egais.api.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.evotor.egais.api.example.stock_commodity.StockCommodityListActivity
import ru.evotor.egais.api.example.actwriteoff.ActWriteOffListActivity
import ru.evotor.egais.api.example.client_settings.ClientSettingsActivity
import ru.evotor.egais.api.example.orginfo.OrgInfoListActivity
import ru.evotor.egais.api.example.product.ProductListActivity
import ru.evotor.egais.api.example.shop_commodity.ShopCommodityListActivity
import ru.evotor.egais.api.example.unrocessed_documents.UnprocessedDocumentsActivity
import ru.evotor.egais.api.example.waybill.WaybillListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewOrgInfo.setOnClickListener {
            startActivity(Intent(this@MainActivity, OrgInfoListActivity::class.java))
        }

        viewProducts.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProductListActivity::class.java))
        }

        viewWayBills.setOnClickListener {
            startActivity(Intent(this@MainActivity, WaybillListActivity::class.java))
        }

        viewStockCommodity.setOnClickListener {
            startActivity(Intent(this@MainActivity, StockCommodityListActivity::class.java))
        }

        viewShopCommodity.setOnClickListener {
            startActivity(Intent(this@MainActivity, ShopCommodityListActivity::class.java))
        }

        viewActWriteOffs.setOnClickListener {
            startActivity(Intent(this@MainActivity, ActWriteOffListActivity::class.java))
        }

        clientSettings.setOnClickListener {
            startActivity(Intent(this@MainActivity, ClientSettingsActivity::class.java))
        }
        unprocessedDocuments.setOnClickListener {
            startActivity(Intent(this@MainActivity, UnprocessedDocumentsActivity::class.java))
        }
    }
}
