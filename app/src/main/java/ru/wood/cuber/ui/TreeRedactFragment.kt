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
import com.google.android.material.appbar.MaterialToolbar
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.SimpleDialogFragment
import ru.wood.cuber.Util.DIAMETERS
import ru.wood.cuber.Util.LENGTHS
import ru.wood.cuber.databinding.FragmentTreeRedactBinding

class TreeRedactFragment : Fragment() {
    private var navController: NavController? =null
    private var supportToolbar : ActionBar? =null
    private var manager: FragmentManager?=null

    object Param{
        var lastLength: Double = 0.0
        var lastDiameter: Int = 0
        var lastQuantity: Int =0

        var newLength: Double = 0.0
        var newDiameter: Int = 0
        var newQuantity: Int =0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding= FragmentTreeRedactBinding.inflate(inflater)
        val view =binding.root
        init(binding)


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


        val toolbar=binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_left)
            isClickable
            toolbarClickListener(this)
        }

        return view
    }

    private fun toolbarClickListener(toolbar: MaterialToolbar){
        toolbar.setOnClickListener {
            if(checkParam()){
                quitDialog()
            } else Navigation.findNavController(it).popBackStack()
        }

    }

    private fun quitDialog(){
        val dialog =SimpleDialogFragment("Сохранить?", {
            Loger.log("save")
        }, {
            navController?.popBackStack()
        }
        ).show(manager!!, "simpleDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        supportToolbar?.show()
    }

    private fun init(binding : FragmentTreeRedactBinding){
        manager = requireActivity().supportFragmentManager
        navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        supportToolbar= (activity as AppCompatActivity).supportActionBar?.apply {
            hide()
        }
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


    private fun checkParam() : Boolean{
        Param.apply {
            return lastLength!= newLength &&
                    lastDiameter!= newDiameter &&
                    lastQuantity!= newQuantity
        }
    }
}