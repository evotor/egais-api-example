package ru.evotor.egais.api.example.orginfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import kotlinx.android.synthetic.main.activity_orginfo_detail.*
import kotlinx.android.synthetic.main.orginfo_detail.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.dictionary.OrgInfo
import ru.evotor.egais.api.query.OrgInfoQuery

/**
 * A fragment representing a single OrgInfo detail screen.
 * This fragment is either contained in a [OrgInfoListActivity]
 * in two-pane mode (on tablets) or a [OrgInfoDetailActivity]
 * on handsets.
 */
class OrgInfoDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<OrgInfo?> {

    private var mItem: OrgInfo? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<OrgInfo?> {
        class OrgInfoLoader : AsyncTaskLoader<OrgInfo?>(requireContext()) {
            override fun loadInBackground(): OrgInfo? {
                return arguments?.let {
                    if (it.containsKey(ARG_ITEM_ID)) {
                        // Load the dummy content specified by the fragment
                        // arguments. In a real-world scenario, use a Loader
                        // to load content from a content provider.
                        OrgInfoQuery()
                            .clientRegId.equal(it.getString(ARG_ITEM_ID) ?: "")
                            .execute(context)
                            .let { cursor ->
                                cursor.moveToFirst()
                                cursor.getValue()
                            }
                    } else {
                        null
                    }
                }
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }

        return OrgInfoLoader()
    }

    override fun onLoadFinished(loader: Loader<OrgInfo?>, data: OrgInfo?) {
        updateData(data)
    }


    override fun onLoaderReset(loader: Loader<OrgInfo?>) {
        updateData(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(0, null, this);
    }

    private fun updateData(data: OrgInfo?) {
        mItem = data
        orginfo_detail?.text = mItem?.toString()
        activity?.toolbar_layout?.title = mItem?.clientRegId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.orginfo_detail, container, false)

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
