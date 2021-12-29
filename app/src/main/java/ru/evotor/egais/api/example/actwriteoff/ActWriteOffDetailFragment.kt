package ru.evotor.egais.api.example.actwriteoff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import kotlinx.android.synthetic.main.activity_actwriteoff_detail.*
import kotlinx.android.synthetic.main.actwriteoff_detail.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.document.actwriteoff.ActWriteOff
import ru.evotor.egais.api.model.document.actwriteoff.ActWriteOffPosition
import ru.evotor.egais.api.query.ActWriteOffPositionQuery
import ru.evotor.egais.api.query.ActWriteOffQuery
import java.util.*

class ActWriteOffDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<ActWriteOffDetailFragment.ActWriteOffWithPositions?> {

    data class ActWriteOffWithPositions(val actWriteOff: ActWriteOff?, val positions: List<ActWriteOffPosition>?)

    private var mItem: ActWriteOffWithPositions? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ActWriteOffWithPositions?> {
        class ActWriteOffLoader : AsyncTaskLoader<ActWriteOffWithPositions?>(requireContext()) {
            override fun loadInBackground(): ActWriteOffWithPositions? {
                return arguments?.let {
                    if (it.containsKey(ActWriteOffDetailFragment.ARG_ITEM_ID)) {
                        // Load the dummy content specified by the fragment
                        // arguments. In a real-world scenario, use a Loader
                        // to load content from a content provider.
                        val uuid = it.getString(ActWriteOffDetailFragment.ARG_ITEM_ID).let { UUID.fromString(it) }
                        ActWriteOffWithPositions(
                                ActWriteOffQuery()
                                        .uuid.equal(uuid)
                                        .execute(context)
                                        .let { cursor ->
                                            cursor.moveToFirst()
                                            cursor.getValue()
                                        },
                                ActWriteOffPositionQuery()
                                        .actWriteOffUuid.equal(uuid)
                                        .execute(context).toList()
                        )
                    } else {
                        null
                    }
                }
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }

        return ActWriteOffLoader()
    }

    override fun onLoadFinished(loader: Loader<ActWriteOffWithPositions?>, data: ActWriteOffWithPositions?) {
        updateData(data)
    }


    override fun onLoaderReset(loader: Loader<ActWriteOffWithPositions?>) {
        updateData(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(0, null, this);
    }

    private fun updateData(data: ActWriteOffWithPositions?) {
        mItem = data
        actwriteoff_detail?.text = mItem?.toString()
        activity?.toolbar_layout?.title = mItem?.actWriteOff?.uuid?.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.waybill_detail, container, false)

        // Show the dummy content as text in a TextView.
        mItem?.let {
            updateData(mItem)
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
