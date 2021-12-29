package ru.evotor.egais.api.example.unrocessed_documents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_unprocessed_documents.*
import kotlinx.android.synthetic.main.unprocessed_document_item.view.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.document.unprocessed_document.UnprocessedDocument
import ru.evotor.egais.api.query.UnprocessedDocumentsQuery
import ru.evotor.query.Cursor
import java.util.*

class UnprocessedDocumentsActivity : AppCompatActivity(),
    LoaderManager.LoaderCallbacks<Cursor<UnprocessedDocument>> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor<UnprocessedDocument>?> {
        class UnprocessedDocumentsLoader :
            AsyncTaskLoader<Cursor<UnprocessedDocument>?>(this@UnprocessedDocumentsActivity) {
            override fun loadInBackground(): Cursor<UnprocessedDocument> {
                return UnprocessedDocumentsQuery().noFilters().execute(context)
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }

        return UnprocessedDocumentsLoader()
    }

    override fun onLoadFinished(
        loader: Loader<Cursor<UnprocessedDocument>>,
        data: Cursor<UnprocessedDocument>?
    ) {
        (unprocessedDocumentsList.adapter as UnprocessedDocumentsRVAdapter).swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor<UnprocessedDocument>>) {
        (unprocessedDocumentsList.adapter as UnprocessedDocumentsRVAdapter).swapCursor(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unprocessed_documents)

        setSupportActionBar(toolbar)
        toolbar.title = title

        setupRecyclerView(unprocessedDocumentsList)

        supportLoaderManager.initLoader(1, null, this);
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = UnprocessedDocumentsRVAdapter(null)
    }
}
