package ru.evotor.egais.api.example.actwriteoff

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
import kotlinx.android.synthetic.main.activity_actwriteoff_list.*
import kotlinx.android.synthetic.main.actwriteoff_list.*
import kotlinx.android.synthetic.main.actwriteoff_list_content.view.*
import ru.evotor.egais.api.ActWriteOffApi
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.document.actwriteoff.ActWriteOff
import ru.evotor.query.Cursor

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ActWriteOffDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ActWriteOffListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor<ActWriteOff>> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor<ActWriteOff>?> {
        class ProductInfoLoader : AsyncTaskLoader<Cursor<ActWriteOff>?>(this@ActWriteOffListActivity) {
            override fun loadInBackground(): Cursor<ActWriteOff>? {
                return ActWriteOffApi.getActWriteOffList(context)
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }

        return ProductInfoLoader()
    }

    override fun onLoadFinished(loader: Loader<Cursor<ActWriteOff>>?, data: Cursor<ActWriteOff>?) {
        (actwriteoff_list.adapter as ActWriteOffListActivity.SimpleItemRecyclerViewAdapter).swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor<ActWriteOff>>?) {
        (actwriteoff_list.adapter as ActWriteOffListActivity.SimpleItemRecyclerViewAdapter).swapCursor(null)
    }


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actwriteoff_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (actwriteoff_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true
        }

        setupRecyclerView(actwriteoff_list)

        supportLoaderManager.initLoader(1, null, this);
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, null, mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: ActWriteOffListActivity,
                                        private var mValues: Cursor<ActWriteOff>?,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as ActWriteOff
                if (mTwoPane) {
                    val fragment = ActWriteOffDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ActWriteOffDetailFragment.ARG_ITEM_ID, item.uuid.toString())
                        }
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.actwriteoff_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ActWriteOffDetailActivity::class.java).apply {
                        putExtra(ActWriteOffDetailFragment.ARG_ITEM_ID, item.uuid.toString())
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.actwriteoff_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            mValues?.moveToPosition(position)
            val item = mValues?.getValue() ?: return

            holder.mIdView.text = item.uuid.toString()
            holder.mContentView.text = item.number + ", " + item.actDate.toString()

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues?.count ?: 0
        }

        fun swapCursor(cursor: Cursor<ActWriteOff>?) {
            mValues = cursor
            notifyDataSetChanged()
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
}
