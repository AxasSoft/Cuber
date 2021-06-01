package ru.wood.cuber.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.SimpleDialogFragment
import ru.wood.cuber.Util.DIAMETERS
import ru.wood.cuber.Util.LENGTHS
import ru.wood.cuber.databinding.FragmentTreeRedactBinding

@Suppress("CAST_NEVER_SUCCEEDS")
class TreeRedactFragment : Fragment() {
    private lateinit var navController: NavController
    private var supportToolbar : ActionBar? =null
    private lateinit var manager: FragmentManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding= FragmentTreeRedactBinding.inflate(inflater)
        val view =binding.root
        manager = requireActivity().supportFragmentManager
        navController= NavHostFragment.findNavController(this)
        supportToolbar= (activity as AppCompatActivity).supportActionBar?.apply {
            hide()
        }

        val button=binding.button.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        val toolbar=binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_left)
            isClickable
            setOnClickListener {

            }
        }

        val id=arguments?.getInt("id")

        val spinnerLength = binding.spinnerLength

        val lengths :List<String> = ArrayList<String>().addSpinnerList("Выберите длину", LENGTHS)
        spinnerLength.setAdapter(lengths)
        spinnerLength.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                        position: Int, id: Long) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        val spinnerDiameter = binding.spinnerDiameter
        val diameters :List<String> = ArrayList<String>().addSpinnerList("Выберите диаметр", DIAMETERS as List<Int>)
        spinnerDiameter.setAdapter(diameters)
        spinnerDiameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                        position: Int, id: Long) {

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        return view
    }
    private fun dialog(){
        val dialog =SimpleDialogFragment("Сохранить?", {
            Loger.log("save")
        }, {
            navController.popBackStack()
        }
        ).show(manager,"simpleDialog")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        supportToolbar?.show()
    }

    private fun <T : Number> ArrayList<String>.addSpinnerList(placeholder: String, numbers: List<T>): List<String>{
        this.add(placeholder)
        for (item: T in numbers){
            this.add(item.toString())
        }
        return this
    }

    private fun Spinner.setAdapter(list: List<String>){
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }
}