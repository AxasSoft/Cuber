package ru.wood.cuber.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.SimpleDialogFragment
import ru.wood.cuber.Utill.DECREASE
import ru.wood.cuber.Utill.DIAMETERS
import ru.wood.cuber.Utill.INCREASE
import ru.wood.cuber.Utill.LENGTHS
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.databinding.FragmentTreeRedactBinding
import ru.wood.cuber.view_models.TreesViewModel
@AndroidEntryPoint
class TreeRedactFragment : Fragment() {
    private var navController: NavController? =null
    private var supportToolbar : ActionBar? =null
    private var manager: FragmentManager?=null
    private val viewModel: TreesViewModel by activityViewModels()
    private var currentEntity: TreePosition?=null

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
        binding.fragment=this
        init(binding)

        val spinnerLength = binding.spinnerLength

        val lengths :List<String> = ArrayList<String>().addSpinnerList(false,"Выберите длину", LENGTHS)
        spinnerLength.setAdapter(lengths)
        spinnerLength.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                        position: Int, id: Long) {
                Param.newLength=LENGTHS[position]
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        val spinnerDiameter = binding.spinnerDiameter

        val diameters :List<String> = ArrayList<String>().addSpinnerList(false,"Выберите диаметр", DIAMETERS as List<Int>)
        spinnerDiameter.setAdapter(diameters)
        spinnerDiameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                        position: Int, id: Long) {
                Param.newDiameter= DIAMETERS!![position]
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        val editText=binding.editText


        val toolbar=binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_left)
            isClickable
            toolbarClickListener(this)
        }

        val id=arguments?.getLong("id")
        val quantity=arguments?.getInt("quantity")
        with(viewModel){
            id?.let { getOneTree(it) }
            onePositionLiveData.observe(viewLifecycleOwner,{
                it?.let {
                    currentEntity=it
                    completion(currentEntity!!,spinnerLength,spinnerDiameter,editText,quantity!!)
                    onePositionLiveData.value=null
                }
            })
            quantityChangingLiveData.observe(viewLifecycleOwner,{
                it.let {
                    changeParams()
                }
            })

        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        supportToolbar?.show()
    }

    fun textChanged (s: CharSequence, start: Int, count: Int, after:Int){
        try {
            val quantity=s.toString().toInt()
            Param.newQuantity=quantity

        }catch (t: Throwable){
            Param.apply { newQuantity= lastQuantity }
        }
    }

    private fun completion(tree: TreePosition, spinnerLength: Spinner, spinnerDiameter: Spinner,editText: EditText, quantity: Int){
        Param.apply {
            lastLength=tree.length!!
            lastDiameter=tree.diameter!!
            lastQuantity=quantity
        }

        spinnerLength.setSelection(LENGTHS.indexOf(tree.length))
        spinnerDiameter.setSelection(DIAMETERS!!.indexOf(tree.diameter))

        (editText as TextView).text=quantity.toString()

        Loger.log("tree position")
        Loger.log((tree.length))
        Loger.log((tree.diameter))
        Loger.log(quantity.toString())
    }

    private fun toolbarClickListener(toolbar: MaterialToolbar){
        toolbar.setOnClickListener {
            if (changeQuantity()){
                when(comparisonQuantity()){
                    INCREASE->{ val count =Param.newQuantity-Param.lastQuantity   //увеличение +1
                        quitDialog {viewModel.addPositions(
                                count = count,
                                diameter = Param.newDiameter,
                                length = Param.newLength) }}
                    DECREASE->{                                                  //уменьшение -1
                        val limit= Param.lastQuantity-Param.newQuantity
                        quitDialog {viewModel.limitDelete(
                                diameter =  Param.newDiameter,
                                length = Param.newLength,
                                limit = limit)  }}

                }
            }else if (changeIsTrue()){
                Loger.log("changeParams")
                quitDialog { changeParams()}
            } else{
                Navigation.findNavController(it).popBackStack()
            }
        }
    }
    private fun changeParams(){
        viewModel.changeParams(
                lastdiameter = Param.lastDiameter,
                lastLength = Param.lastLength,
                newDiameter =  Param.newDiameter,
                newLength = Param.newLength )
    }

    private fun quitDialog(positiveFunction: ()-> Unit){
        val dialog =SimpleDialogFragment("Сохранить?",
                positiveFunction={positiveFunction()},
                navigate={navController?.popBackStack()}
        ).show(manager!!, "simpleDialog")
    }

    private fun init(binding : FragmentTreeRedactBinding){
        manager = requireActivity().supportFragmentManager
        navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        supportToolbar= (activity as AppCompatActivity).supportActionBar?.apply {
            hide()
        }
    }

    private fun comparisonQuantity(): Int{
        Param.apply {
            if (newQuantity> lastQuantity){
                return INCREASE
            } else if (newQuantity< lastQuantity){
                return DECREASE
            } else return 0
        }
    }

    private fun changeQuantity(): Boolean{
        Param.apply {
            return !(lastQuantity== newQuantity)
        }
    }

    private fun changeIsTrue() : Boolean{
        Param.apply {
            return !(lastLength== newLength &&
                    lastDiameter== newDiameter)
        }
    }

    private fun <T : Number> ArrayList<String>.addSpinnerList(placeholder: Boolean,msg: String, numbers: List<T>): List<String>{
        if (placeholder){
            this.add(msg)
        }
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