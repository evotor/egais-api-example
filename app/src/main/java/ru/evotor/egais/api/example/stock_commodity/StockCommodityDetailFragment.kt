package ru.evotor.egais.api.example.stock_commodity

import android.os.Bundle
import androidx.loader.content.AsyncTaskLoader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import kotlinx.android.synthetic.main.activity_egais_commodity_detail.*
import kotlinx.android.synthetic.main.egais_commodity_detail.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.dictionary.StockCommodity
import ru.evotor.egais.api.query.StockCommodityQuery

class StockCommodityDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<StockCommodity?> {

    private var mItem: StockCommodity? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<StockCommodity?> {
        class EgaisCommodityLoader : AsyncTaskLoader<StockCommodity?>(requireContext()) {
            override fun loadInBackground(): StockCommodity? {
                return arguments?.let {
                    if (it.containsKey(ARG_ITEM_ID)) {
                        val cursor = StockCommodityQuery().productInfo.alcCode.equal(it.getString(ARG_ITEM_ID))
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
        activity?.toolbar_layout?.title = mItem?.productInfo?.alcCode
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.egais_commodity_detail, container, false)

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
