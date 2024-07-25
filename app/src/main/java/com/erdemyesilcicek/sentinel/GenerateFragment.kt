package com.erdemyesilcicek.sentinel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.Navigation
import com.erdemyesilcicek.sentinel.databinding.FragmentGenerateBinding
import kotlin.random.Random
class GenerateFragment : Fragment() {
    private var _binding: FragmentGenerateBinding? = null
    private val binding get() = _binding!!

    var startPoint = 0
    var endPoint = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenerateBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.generateButton.setOnClickListener { generateButtonClicked(it) }
        seekBarController()
    }
    fun generateButtonClicked(view: View){
        binding.apply {
            generateButton.setOnClickListener {
                val selectedOptions = mutableListOf<Char>()

                if(checkboxLowercase.isChecked){
                    selectedOptions.addAll(('a'..'z'))
                }
                if(checkboxUppercase.isChecked){
                    selectedOptions.addAll(('A'..'Z'))
                }
                if(checkboxNumbers.isChecked){
                    selectedOptions.addAll(('0'..'9'))
                }
                if(checkboxSymbols.isChecked){
                    selectedOptions.addAll(("!+$%#*_?".toList()))
                }
                if(selectedOptions.isEmpty()){
                    Toast.makeText(requireContext(), "please select options.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val passwordLength = binding.seekbarText.text.toString().toInt()

                if(checkboxLowercase.isChecked && checkboxUppercase.isChecked && checkboxNumbers.isChecked && checkboxSymbols.isChecked && passwordLength >= 10){
                    Toast.makeText(requireContext(), "YOUR PASSWORD IS SO STRONG!", Toast.LENGTH_SHORT).show()
                }

                val randomPassword = buildString {
                    repeat(passwordLength){
                        val randomIndex = Random.nextInt(0,selectedOptions.size)
                        append(selectedOptions[randomIndex])
                    }
                }
                passwordText.text = randomPassword
            }
            passwordText.setOnClickListener {
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("TextViewText", passwordText.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(),"copied.", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun seekBarController(){
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.seekbarText.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if(seekBar != null){
                    startPoint = seekBar.progress
                }
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(seekBar != null){
                    endPoint = seekBar.progress
                }
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}