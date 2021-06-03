package ru.wood.cuber.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.daimajia.swipe.SwipeLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.wood.cuber.Loger
import ru.wood.cuber.R
import ru.wood.cuber.ViewDialog
import ru.wood.cuber.adapters.RecyclerCallback
import ru.wood.cuber.adapters.SwipeRecyclerAdapter2
import ru.wood.cuber.data.MyCalculation
import ru.wood.cuber.databinding.FragmentMyCalculationsBinding
import ru.wood.cuber.databinding.ItemCalculateSwipeBinding
import ru.wood.cuber.view_models.CalculationsViewModel
import ru.wood.cuber.view_models.ContainsViewModel
@AndroidEntryPoint
class MyCalculationsFragment : Fragment() {
    private lateinit var navController: NavController
    private val viewModel: CalculationsViewModel by viewModels()
    private lateinit var adapter:SwipeRecyclerAdapter2<MyCalculation,ItemCalculateSwipeBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*(activity as AppCompatActivity).supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_HOME
            displayOptions = ActionBar.DISPLAY_SHOW_TITLE
            setDisplayHomeAsUpEnabled(true)
            title="3434634634"
        }*/
        val binding=FragmentMyCalculationsBinding.inflate(inflater)
        val view=binding.root
        binding.fragment=this
        navController=findNavController(this)

        val recycler=binding.recycler
        with(viewModel){
            refreshList()
            liveData.observe(viewLifecycleOwner,{
                if (it==null){return@observe}
                adapter=SwipeRecyclerAdapter2(it,R.layout.item_calculate_swipe,
                        object : RecyclerCallback<ItemCalculateSwipeBinding, MyCalculation> {
                            override fun bind(binder: ItemCalculateSwipeBinding, entity: MyCalculation, position: Int,itemView: View) {
                                swipeHolderAction(binder, entity, position, itemView)
                                subscribeClickPosition(binder.include.clicableLayout, entity.id)
                            }
                        })
                recycler.adapter=adapter
                adapter.notifyDataSetChanged()
            })
        }

        return view
    }

    fun createNew( view: View){
        ViewDialog.showCreateCalculationDialog(requireContext(),"Введите номер расчета"){
            viewModel.addNew(it)
        }
    }

    fun subscribeClickPosition(clicableLayout: View, idPosition: Long){
        clicableLayout.setOnClickListener {
            val bundle= Bundle()
            bundle.putLong("id", idPosition)
            navController.navigate(R.id.action_myCalculationsFragment_to_containersFragment,bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.my_calculate_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            /* R.id.download_pdf-> {
                 return true
             }*/
            else ->{
                return super.onOptionsItemSelected(item)}
        }
    }
    private fun swipeHolderAction(binder: ItemCalculateSwipeBinding, entity: MyCalculation, position: Int,itemView: View){
        with(binder){
            this.include.entity=entity
            swipe.setShowMode(SwipeLayout.ShowMode.PullOut)
            //dari kanan
            swipe.addDrag(
                    SwipeLayout.DragEdge.Right, binder.swipe.findViewById(R.id.bottom_wraper))
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
                viewModel.deletePosition(entity)

                adapter.apply {
                    mItemManger.removeShownLayouts(binder.swipe)
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, adapter.list.size)
                    mItemManger.closeAllItems()
                }

                Toast.makeText(requireContext(), "Deleted " + binder.include.entity?.name.toString(),
                        Toast.LENGTH_SHORT).show()
            })

        }
        adapter.mItemManger.bindView(itemView, position)
    }
}