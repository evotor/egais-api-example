package ru.evotor.egais.api.example.product

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.product_detail.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.dictionary.ProductInfo
import ru.evotor.egais.api.query.ProductInfoQuery

/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a [ProductListActivity]
 * in two-pane mode (on tablets) or a [ProductDetailActivity]
 * on handsets.
 */
class ProductDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<ProductInfo?> {

    /**
     * The dummy content this fragment is presenting.
     */
    private var mItem: ProductInfo? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ProductInfo?> {
        class ProductInfoLoader : AsyncTaskLoader<ProductInfo?>(context) {
            override fun loadInBackground(): ProductInfo? {
                return arguments?.let {
                    if (it.containsKey(ProductDetailFragment.ARG_ITEM_ID)) {
                        // Load the dummy content specified by the fragment
                        // arguments. In a real-world scenario, use a Loader
                        // to load content from a content provider.
                        ProductInfoQuery()
                                .alcCode.equal(it.getString(ProductDetailFragment.ARG_ITEM_ID))
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

        return ProductInfoLoader()
    }

    override fun onLoadFinished(loader: Loader<ProductInfo?>, data: ProductInfo?) {
        updateData(data)
    }


    override fun onLoaderReset(loader: Loader<ProductInfo?>) {
        updateData(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(0, null, this);
    }

    private fun updateData(data: ProductInfo?) {
        mItem = data
        product_detail?.text = mItem?.toString()
        activity?.toolbar_layout?.title = mItem?.alcCode
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.product_detail, container, false)

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
