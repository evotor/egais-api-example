package ru.evotor.egais.api.example.stock_commodity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_egais_commodity_detail.*
import kotlinx.android.synthetic.main.egais_commodity_detail.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.document.stock_commodity.StockCommodity
import ru.evotor.egais.api.query.StockCommodityQuery
import java.util.*

/**
 * A fragment representing a single OrgInfo detail screen.
 * This fragment is either contained in a [StockCommodityListActivity]
 * in two-pane mode (on tablets) or a [StockCommodityDetailActivity]
 * on handsets.
 */
class StockCommodityDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<StockCommodity?> {

    private var mItem: StockCommodity? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<StockCommodity?> {
        class EgaisCommodityLoader : AsyncTaskLoader<StockCommodity?>(context) {
            override fun loadInBackground(): StockCommodity? {
                return arguments?.let {
                    if (it.containsKey(ARG_ITEM_ID)) {
                        // Load the dummy content specified by the fragment
                        // arguments. In a real-world scenario, use a Loader
                        // to load content from a content provider.
                        val cursor = StockCommodityQuery().productInfoAlcCode.equal(it.getString(ARG_ITEM_ID))
                                .execute(context)
                        cursor.moveToFirst()
                        return cursor.getValue()
                    } else {
                        null
                    }
                }
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }

        return EgaisCommodityLoader()
    }

    override fun onLoadFinished(loader: Loader<StockCommodity?>, data: StockCommodity?) {
        updateData(data)
    }


    override fun onLoaderReset(loader: Loader<StockCommodity?>) {
        updateData(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(0, null, this);
    }

    private fun updateData(data: StockCommodity?) {
        mItem = data
        egais_commodity_detail?.text = mItem?.toString()
        activity?.toolbar_layout?.title = mItem?.productInfoAlcCode
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.egais_commodity_detail, container, false)

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
