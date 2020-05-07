package com.example.android.wordmemo


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.wordmemo.databinding.FragmentPhraseTableBinding
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

/**
 * A simple [Fragment] subclass.
 */
class PhraseTableFragment : Fragment() {
    private lateinit var binding: FragmentPhraseTableBinding

    companion object {
        private const val READ_REQUEST_CODE: Int = 42
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_phrase_table, container, false
        )
        binding.textView.setOnClickListener { getInfo() }
        binding.saveListButton.setOnClickListener { savePhraseList() }
        fillTable()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun savePhraseList() {
        val phrases = PhraseManager.getAllPhrases()
        Timber.i("savePhraseList")
        Timber.i("phrases to save: $phrases")
        val file = File(activity?.filesDir, "yData.txt")

        Timber.i("file content")
        if (file.exists()) {
            file.forEachLine { Timber.i(it) }
        }
        Timber.i("file exist 1: ${file.exists()}")
        Timber.i("file: ${file.absolutePath}")
        Timber.i("file: $activity?.filesDir")

        val osw: OutputStreamWriter
        try {
            osw = OutputStreamWriter(FileOutputStream(file))
            for (p in phrases) {
                osw.write("${p.phrase}:${p.translation}:${p.count}\n")
            }
            osw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Timber.i("file exist 2: ${file.exists()}")
        binding.saveListButton.visibility = View.GONE
        Toast.makeText(activity, "Phrase list saved! ✔", Toast.LENGTH_SHORT).show()
    }

    private fun fillTable() {
        val phrases = PhraseManager.getAllPhrases()
        var row: TableRow
        var text: TextView
        for (p in phrases) {
            row = TableRow(context)
            row.setPadding(4)
            row.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)

            row.setBackgroundColor(Color.rgb(223, 223, 223))
            text = TextView(context)
            text.setPadding(4)
            text.textAlignment = View.TEXT_ALIGNMENT_CENTER
            text.text = p.count.toString()
            row.addView(text)
            text = TextView(context)
            text.setPadding(4)
            text.text = p.phrase.capitalize()
            row.addView(text)
            text = TextView(context)
            text.setPadding(4)
            text.text = p.translation.capitalize()
            row.addView(text)
            binding.table.addView(row)
        }

    }

    private fun getInfo() {
        binding.textView.text = PhraseManager.getAllPhrases().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
        menu.findItem(R.id.phraseTableFragment)?.setVisible(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.export -> shareFile()
            R.id.importFile -> importFile()
        }
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun importFile() {
        val importIntent = Intent(Intent.ACTION_GET_CONTENT)
        importIntent.apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        //READ_REQUEST_CODE es para identificar el Intent en el mtd onActivityResult
        startActivityForResult(importIntent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                Timber.i("Uri: $uri")
            }
            activity?.contentResolver?.openInputStream(data?.data!!).use { inputStream ->
                PhraseManager.importFile(inputStream!!)
            }
            val header = binding.table[0]
            binding.table.removeAllViews()
            binding.table.addView(header)
            fillTable()
            Toast.makeText(activity, "File imported! ✔", Toast.LENGTH_SHORT).show()
        }
    }

    // Creating our Share Intent
    private fun getShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        val file = File(activity?.filesDir, "yData.txt")
        shareIntent.setType("text/plain")
            .putExtra(
                Intent.EXTRA_TEXT, file.readLines().reduce { a, b -> "$a\n$b" }.toString()
            )
        return shareIntent
    }

    // Starting an Activity with our new Intent
    private fun shareFile() {
        startActivity(getShareIntent())
    }

}
