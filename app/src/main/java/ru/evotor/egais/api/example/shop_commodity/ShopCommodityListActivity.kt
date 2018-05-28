package ru.evotor.egais.api.example.shop_commodity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_egais_commodity_list.*
import kotlinx.android.synthetic.main.egais_commodity_list.*
import kotlinx.android.synthetic.main.egais_commodity_list_content.view.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.example.stock_commodity.StockCommodityDetailFragment
import ru.evotor.egais.api.model.dictionary.ShopCommodity
import ru.evotor.egais.api.query.ShopCommodityQuery
import ru.evotor.query.Cursor

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ShopCommodityDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ShopCommodityListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor<ShopCommodity>> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor<ShopCommodity>?> {
        class EgaisCommodityLoader : AsyncTaskLoader<Cursor<ShopCommodity>?>(this@ShopCommodityListActivity) {
            override fun loadInBackground(): Cursor<ShopCommodity>? {
                return ShopCommodityQuery().noFilters().execute(context)
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }

        return EgaisCommodityLoader()
    }

    override fun onLoadFinished(loader: Loader<Cursor<ShopCommodity>>?, data: Cursor<ShopCommodity>?) {
        (egais_commodity_list.adapter as SimpleItemRecyclerViewAdapter).swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor<ShopCommodity>>?) {
        (egais_commodity_list.adapter as SimpleItemRecyclerViewAdapter).swapCursor(null)
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egais_commodity_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (stock_commodity_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true
        }

        setupRecyclerView(egais_commodity_list)

        supportLoaderManager.initLoader(1, null, this);
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, null, mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: ShopCommodityListActivity,
                                        private var mValues: Cursor<ShopCommodity>?,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {

            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as ShopCommodity
                if (mTwoPane) {
                    val fragment = StockCommodityDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ShopCommodityDetailFragment.ARG_ITEM_ID, item.productInfo.alcCode)
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.stock_commodity_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ShopCommodityDetailActivity::class.java).apply {
                        putExtra(ShopCommodityDetailFragment.ARG_ITEM_ID, item.productInfo.alcCode)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.egais_commodity_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            mValues?.moveToPosition(position)
            val item = mValues?.getValue() ?: return
            holder.mIdView.text = item.productInfo.alcCode
            //holder.mContentView.text = item.productInfoAlcCode + '\n' + item.quantity.toPlainString()

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues?.count ?: 0
        }

        fun swapCursor(cursor: Cursor<ShopCommodity>?) {
            mValues = cursor
            notifyDataSetChanged()
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
}
