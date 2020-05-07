package com.example.android.wordmemo

import timber.log.Timber
import java.io.*
import java.util.*

object PhraseManager {


    fun importFile(inputStream: InputStream) {
        Timber.i("importFile")
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line = reader.readLine()
            while (line != null) {
                Timber.i("line: $line")
                val tokens = line.split(":")
                this.addWord(Phrase(tokens[0], tokens[1], Integer.parseInt(tokens[2])))
                line = reader.readLine()
            }
        }
        Timber.i("File imported")
    }

    fun load(filesDir: File) {
        PhraseList.clear()
        Timber.i("file: $filesDir")
        val file = File(filesDir, "yData.txt")

        Timber.i("file content")
        if (file.exists()) {
            file.forEachLine { Timber.i(it) }
        }
        Timber.i("file exist 1: ${file.exists()}")
        Timber.i("file: ${file.absolutePath}")
        Timber.i("file: $filesDir")

        val isr: InputStreamReader?
        try {
            isr = InputStreamReader(FileInputStream(file))

            isr.forEachLine {
                val tokens = it.split(":")
                Timber.i("tokens: $tokens")
                PhraseList.addPhrase(tokens[0], tokens[1], Integer.parseInt(tokens[2]))
            }
            isr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Timber.i("result: $PhraseList")
    }

    fun addWord(phrase: Phrase) {
        Timber.i("addWord")
        Timber.i("begin: $PhraseList")
        PhraseList.addPhrase(phrase)
        Timber.i("end: $PhraseList")
    }

    fun getAllPhrases(): List<Phrase> {
        return PhraseList.getSortedPhrases()
    }


}

object PhraseList {
    private var phrases = mutableListOf<Phrase>()

    fun addPhrase(phrase: Phrase) {
        var newPhrase = Phrase(
            phrase.phrase.replace(":", "").toLowerCase(Locale.getDefault()).trim(),
            phrase.translation.replace(":", "").toLowerCase(Locale.getDefault()).trim(),
            phrase.count
        )

        val pos: Int = phrases.indexOf(phrase)
        Timber.i("pos: $pos")
        Timber.i("hash: ${phrase.hashCode()}")

        if (pos != -1) {
            val newCount: Int = phrases[pos].count + phrase.count + 1
            phrases.removeAt(pos)
            Timber.i("removeAt[$pos]: $phrases")
            newPhrase.count = newCount
        }

        phrases.add(newPhrase)
    }

    fun addPhrase(phrase: String, translation: String, count: Int) {
        addPhrase(Phrase(phrase, translation, count))
    }

    fun clear() {
        phrases.clear()
    }

    fun getSortedPhrases(): List<Phrase> {
        return phrases.sortedWith(compareBy({ -it.count }, Phrase::phrase))
    }

    override fun toString(): String {
        return "PhraseList(phrases='$phrases')"
    }
}

data class Phrase(val phrase: String, val translation: String, var count: Int = 0) {
    override fun hashCode(): Int {
        Timber.i("hash: ${phrase.hashCode()}")
        return phrase.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Phrase) {
            return this.phrase.equals(other.phrase)
        }
        return false
    }

}