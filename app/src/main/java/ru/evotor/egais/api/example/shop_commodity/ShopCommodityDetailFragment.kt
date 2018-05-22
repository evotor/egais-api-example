package ru.evotor.egais.api.example.shop_commodity

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
import ru.evotor.egais.api.model.dictionary.ShopCommodity
import ru.evotor.egais.api.query.ShopCommodityQuery

class ShopCommodityDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<ShopCommodity?> {

    private var mItem: ShopCommodity? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ShopCommodity?> {
        class EgaisCommodityLoader : AsyncTaskLoader<ShopCommodity?>(context) {
            override fun loadInBackground(): ShopCommodity? {
                return arguments?.let {
                    if (it.containsKey(ARG_ITEM_ID)) {
                        // Load the dummy content specified by the fragment
                        // arguments. In a real-world scenario, use a Loader
                        // to load content from a content provider.
                        val cursor = ShopCommodityQuery().productInfo.alcCode.equal(it.getString(ARG_ITEM_ID))
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

    override fun onLoadFinished(loader: Loader<ShopCommodity?>, data: ShopCommodity?) {
        updateData(data)
    }


    override fun onLoaderReset(loader: Loader<ShopCommodity?>) {
        updateData(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(0, null, this);
    }

    private fun updateData(data: ShopCommodity?) {
        mItem = data
        egais_commodity_detail?.text = mItem?.toString()
        activity?.toolbar_layout?.title = mItem?.productInfo?.alcCode
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
