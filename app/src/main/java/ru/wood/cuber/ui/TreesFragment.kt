package ru.wood.cuber.ui

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.daimajia.swipe.SwipeLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.Util
import ru.wood.cuber.ViewDialog
import ru.wood.cuber.adapters.RecyclerCallback
import ru.wood.cuber.adapters.SimpleRecyclerAdapter
import ru.wood.cuber.adapters.SwipeRecyclerAdapter2
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.databinding.FragmentTreesBinding
import ru.wood.cuber.databinding.ItemTreesSwipeBinding
import ru.wood.cuber.view_models.TreesViewModel

@AndroidEntryPoint
class TreesFragment : Fragment(), SimpleRecyclerAdapter.OnPositionClickListener{
    private var navController: NavController? =null
    private val viewModel: TreesViewModel by activityViewModels()
    private var adapter: SwipeRecyclerAdapter2<TreePosition, ItemTreesSwipeBinding>?=null
    private var currentPositionLength : Int = 0
    private lateinit var spinnerText: TextView
    private var idOfContain : Long? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        currentPositionLength=1
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val actionBar=(activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val binding=FragmentTreesBinding.inflate(inflater)
        binding.textView1.paintFlags = binding.textView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val view= binding.root
        val recycler=binding.recycler
        spinnerText=binding.spinnerText

        val spinnerLength : Spinner? = binding.spinnerLength

        val lengths :List<String> = ArrayList<String>().addSpinnerList(
                Util.LENGTHS
        )

        spinnerLength?.apply {
            setAdapter(lengths)
            setSelection(currentPositionLength)
            onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    spinnerText.text = lengths[position] + "м"
/*
                    (parent?.getChildAt(0) as TextView?)?.apply {
                        setTextColor(Color.WHITE)
                        textSize = 14f
                        text="$text м"
                    }*/

                    if (position!=currentPositionLength){
                        ViewDialog.showDialogOfLength(context,
                                {
                                    currentPositionLength = position
                                    Loger.log(position)
                                }, {
                            setSelection(currentPositionLength)
                        }
                        )
                    }

                    viewModel.commonLength=Util.LENGTHS[currentPositionLength]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        idOfContain= arguments?.getLong("id")
        with(viewModel){
            commonСontainerId=idOfContain
            refreshList(commonСontainerId!!)
            liveData.observe(viewLifecycleOwner, {
                it?.let {
                    adapter = SwipeRecyclerAdapter2(it, R.layout.item_trees_swipe,
                            object : RecyclerCallback<ItemTreesSwipeBinding, TreePosition> {
                                override fun bind(
                                        binder: ItemTreesSwipeBinding,
                                        entity: TreePosition,
                                        position: Int,
                                        itemView: View
                                ) {
                                    swipeHolderAction(binder, entity, position, itemView)
                                    subscribeClickPosition(binder.include.clicableLayout, entity.id)
                                }
                            })
                    recycler.adapter = adapter
                    adapter!!.notifyDataSetChanged()

                    actionBar?.title = "name"
                }
            })
        }

        return view
    }

    fun backStack(){
        navController?.popBackStack()
    }

    fun subscribeClickPosition(clicableLayout: View, idPosition: Long){
        clicableLayout.setOnClickListener {
            val bundle= Bundle()
            bundle.putLong("id", idPosition)
            navController?.navigate(R.id.action_treesFragment_to_treeRedactFragment, bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.trees_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                backStack(); return true
            }
            else ->{
                return super.onOptionsItemSelected(item)}
        }
    }

    override fun onPositionClick(view: View, id: Int) {

    }
    private fun swipeHolderAction(
            binder: ItemTreesSwipeBinding,
            entity: TreePosition,
            position: Int,
            itemView: View
    ){
        with(binder){
            this.entity=entity
            swipe.setShowMode(SwipeLayout.ShowMode.PullOut)
            //dari kanan
            swipe.addDrag(
                    SwipeLayout.DragEdge.Right, binder.swipe.findViewById(R.id.bottom_wraper)
            )
            swipe.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout?) {}
                override fun onOpen(layout: SwipeLayout?) {}
                override fun onStartClose(layout: SwipeLayout?) {}
                override fun onClose(layout: SwipeLayout?) {}
                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}
                override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {}
            })
            swipe.getSurfaceView().setOnClickListener(View.OnClickListener { v ->
                val bundle = Bundle()
                bundle.putLong("id", entity.id)
                Navigation.findNavController(v).navigate(R.id.treesFragment, bundle)
            })

            Delete.setOnClickListener(View.OnClickListener { v ->
                //viewModel.deleteExactly(list[position].id) //Удаление из БД
                viewModel.deltePosition(entity, idOfContain!!)

                adapter!!.apply {
                    mItemManger.removeShownLayouts(binder.swipe)
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, adapter!!.list.size)
                    mItemManger.closeAllItems()
                }

                Toast.makeText(
                        requireContext(), "Deleted " + binder.include.entity?.id,
                        Toast.LENGTH_SHORT
                ).show()
            })

        }
        adapter!!.mItemManger.bindView(itemView, position)
    }
    private fun <T : Number> ArrayList<String>.addSpinnerList(numbers: List<T>): MutableList<String>{
        for (item: T in numbers){
            this.add(item.toString())
        }
        return this
    }
    private fun Spinner.setAdapter(list: List<String>){
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }
}