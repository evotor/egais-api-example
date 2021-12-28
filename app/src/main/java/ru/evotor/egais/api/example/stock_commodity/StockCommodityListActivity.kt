package ru.evotor.egais.api.example.stock_commodity

import android.content.Intent
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_egais_commodity_list.*
import kotlinx.android.synthetic.main.egais_commodity_list.*
import kotlinx.android.synthetic.main.egais_commodity_list_content.view.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.dictionary.StockCommodity
import ru.evotor.egais.api.query.StockCommodityQuery
import ru.evotor.query.Cursor

class StockCommodityListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor<StockCommodity>> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor<StockCommodity>?> {
        class EgaisCommodityLoader : AsyncTaskLoader<Cursor<StockCommodity>?>(this@StockCommodityListActivity) {
            override fun loadInBackground(): Cursor<StockCommodity>? {
                return StockCommodityQuery().noFilters().execute(context)
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }

        return EgaisCommodityLoader()
    }

    override fun onLoadFinished(loader: Loader<Cursor<StockCommodity>>, data: Cursor<StockCommodity>?) {
        (egais_commodity_list.adapter as SimpleItemRecyclerViewAdapter).swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor<StockCommodity>>) {
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
            mTwoPane = true
        }

        setupRecyclerView(egais_commodity_list)

        supportLoaderManager.initLoader(1, null, this);
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, null, mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: StockCommodityListActivity,
                                        private var mValues: Cursor<StockCommodity>?,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {

            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as StockCommodity
                if (mTwoPane) {
                    val fragment = StockCommodityDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(StockCommodityDetailFragment.ARG_ITEM_ID, item.productInfo.alcCode)
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.stock_commodity_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, StockCommodityDetailActivity::class.java).apply {
                        putExtra(StockCommodityDetailFragment.ARG_ITEM_ID, item.productInfo.alcCode)
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

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues?.count ?: 0
        }

        fun swapCursor(cursor: Cursor<StockCommodity>?) {
            mValues = cursor
            notifyDataSetChanged()
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
}
