package ru.evotor.egais.api.example.waybill

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_waybill_list.*
import kotlinx.android.synthetic.main.waybill_list.*
import kotlinx.android.synthetic.main.waybill_list_content.view.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.document.waybill.WayBill
import ru.evotor.egais.api.query.WayBillQuery
import ru.evotor.query.Cursor

class WaybillListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor<WayBill>> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor<WayBill>?> {
        class ProductInfoLoader : AsyncTaskLoader<Cursor<WayBill>?>(this@WaybillListActivity) {
            override fun loadInBackground(): Cursor<WayBill>? {
                return WayBillQuery().noFilters().execute(context)
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }

        return ProductInfoLoader()
    }

    override fun onLoadFinished(loader: Loader<Cursor<WayBill>>, data: Cursor<WayBill>?) {
        (waybill_list.adapter as WaybillListActivity.SimpleItemRecyclerViewAdapter).swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor<WayBill>>) {
        (waybill_list.adapter as WaybillListActivity.SimpleItemRecyclerViewAdapter).swapCursor(null)
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waybill_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (waybill_detail_container != null) {
            mTwoPane = true
        }

        setupRecyclerView(waybill_list)

        supportLoaderManager.initLoader(1, null, this);
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, null, mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: WaybillListActivity,
                                        private var mValues: Cursor<WayBill>?,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as WayBill
                if (mTwoPane) {
                    val fragment = WaybillDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(WaybillDetailFragment.ARG_ITEM_ID, item.uuid.toString())
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.waybill_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, WaybillDetailActivity::class.java).apply {
                        putExtra(WaybillDetailFragment.ARG_ITEM_ID, item.uuid.toString())
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.waybill_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            mValues?.moveToPosition(position)
            val item = mValues?.getValue() ?: return
            holder.mIdView.text = item.uuid.toString()
            holder.mContentView.text = item.number + ", " + item.date.toString()

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues?.count ?: 0
        }

        fun swapCursor(cursor: Cursor<WayBill>?) {
            mValues = cursor
            notifyDataSetChanged()
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
}
