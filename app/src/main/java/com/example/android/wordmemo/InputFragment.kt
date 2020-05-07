package com.example.android.wordmemo


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.wordmemo.databinding.FragmentInputBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class InputFragment : Fragment() {
    private lateinit var binding: FragmentInputBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView")
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_input, container, false
        )
        binding.saveButton.setOnClickListener { saveWord() }

        binding.translationInput.addTextChangedListener(
            // Implementing interface anonymous class
            object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    binding.saveButton.visibility = View.VISIBLE
                }
            }
        )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
        menu.findItem(R.id.inputFragment)?.setVisible(false)
        menu.findItem(R.id.export)?.setVisible(false)
        menu.findItem(R.id.importFile)?.setVisible(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun saveWord() {
        Timber.i("saveWord")
        val phrase = binding.phraseInput.text.toString()
        val translation = binding.translationInput.text.toString()
        Timber.i(phrase)
        Timber.i(translation)
        PhraseManager.addWord(Phrase(phrase, translation))
        Timber.i("output: ${PhraseManager.getAllPhrases()}")
        binding.saveButton.visibility = View.GONE
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.saveButton.windowToken, 0)
        Toast.makeText(activity, "Phrase saved! ✔", Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.i("onActivityCreated called")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView called")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach called")
    }

}
