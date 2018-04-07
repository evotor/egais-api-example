package ru.evotor.egais.api.example.waybill

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_waybill_detail.*
import kotlinx.android.synthetic.main.waybill_detail.*
import ru.evotor.egais.api.WayBillApi
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.document.waybill.WayBill
import ru.evotor.egais.api.model.document.waybill.WayBillPosition
import java.util.*

/**
 * A fragment representing a single Waybill detail screen.
 * This fragment is either contained in a [WaybillListActivity]
 * in two-pane mode (on tablets) or a [WaybillDetailActivity]
 * on handsets.
 */
class WaybillDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<WaybillDetailFragment.WayBillWithPositions?> {

    data class WayBillWithPositions(val wayBill: WayBill?, val positions: List<WayBillPosition>?)

    /**
     * The dummy content this fragment is presenting.
     */
    private var mItem: WayBillWithPositions? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<WayBillWithPositions?> {
        class ProductInfoLoader : AsyncTaskLoader<WayBillWithPositions?>(context) {
            override fun loadInBackground(): WayBillWithPositions? {
                return arguments?.let {
                    if (it.containsKey(WaybillDetailFragment.ARG_ITEM_ID)) {
                        val uuid = it.getString(WaybillDetailFragment.ARG_ITEM_ID).let { UUID.fromString(it) }
                        WayBillWithPositions(
                                WayBillApi.getWayBillByUuid(context, uuid),
                                WayBillApi.getWayBillPositionListByUuid(context, uuid)?.toList()
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

        return ProductInfoLoader()
    }

    override fun onLoadFinished(loader: Loader<WayBillWithPositions?>, data: WayBillWithPositions?) {
        updateData(data)
    }


    override fun onLoaderReset(loader: Loader<WayBillWithPositions?>) {
        updateData(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(0, null, this);
    }

    private fun updateData(data: WayBillWithPositions?) {
        mItem = data
        waybill_detail?.text = mItem?.toString()
        activity?.toolbar_layout?.title = mItem?.wayBill?.uuid?.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.waybill_detail, container, false)

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
