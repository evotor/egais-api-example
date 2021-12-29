package ru.evotor.egais.api.example.unrocessed_documents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.unprocessed_document_item.view.*
import ru.evotor.egais.api.example.R
import ru.evotor.egais.api.model.document.unprocessed_document.UnprocessedDocument
import ru.evotor.query.Cursor
import java.text.SimpleDateFormat
import java.util.*

class UnprocessedDocumentsRVAdapter(private var values: Cursor<UnprocessedDocument>?) :
    RecyclerView.Adapter<UnprocessedDocumentsRVAdapter.ViewHolder>() {

    private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.unprocessed_document_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        values?.moveToPosition(position)
        val item = values?.getValue() ?: return
        holder.documentId.text = item.id
        holder.content.text = simpleDateFormat.format(item.date)
    }

    override fun getItemCount(): Int {
        return values?.count ?: 0
    }

    fun swapCursor(cursor: Cursor<UnprocessedDocument>?) {
        values = cursor
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val documentId: TextView = view.unprocessedDocumentsId
        val content: TextView = view.unprocessedDocumentsContent
    }
}