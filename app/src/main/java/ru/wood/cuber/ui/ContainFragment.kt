package ru.wood.cuber.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.wood.cuber.R
import ru.wood.cuber.view_models.ContainsViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.daimajia.swipe.SwipeLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.wood.cuber.Loger
import ru.wood.cuber.ViewDialog
import ru.wood.cuber.adapters.RecyclerCallback
import ru.wood.cuber.adapters.SwipeRecyclerAdapter2
import ru.wood.cuber.data.MyСontainer
import ru.wood.cuber.databinding.FragmentContainBinding
import ru.wood.cuber.databinding.ItemContainerSwipeBinding
import ru.wood.cuber.interactors.CommonQuantity
import ru.wood.cuber.repositories.RepositoryContains
import ru.wood.cuber.room.AppDatabase
import ru.wood.cuber.utill.Utill.BUNDLE_CONTAINER_ID

@AndroidEntryPoint
class ContainFragment : Fragment() {
    private lateinit var navController: NavController
    private val viewModel: ContainsViewModel by viewModels()
    private lateinit var adapter: SwipeRecyclerAdapter2<MyСontainer, ItemContainerSwipeBinding>
    private var idOfCalculate: Long? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Мои расчеты"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=FragmentContainBinding.inflate(inflater);
        val view=binding.root
        binding.fragment=this
        navController= NavHostFragment.findNavController(this)

        /*(activity as AppCompatActivity).supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_HOME
            displayOptions = ActionBar.DISPLAY_SHOW_TITLE
            setDisplayHomeAsUpEnabled(true)
            title="3434634634"
        }*/
        val recycler=binding.recycler

        idOfCalculate= arguments?.getLong(BUNDLE_CONTAINER_ID)
        with(viewModel){
            idOfCalculate?.let {refreshList(it)}
            liveData.observe(viewLifecycleOwner,{
                if (it==null){return@observe}

                adapter=SwipeRecyclerAdapter2(it,R.layout.item_container_swipe,
                        object : RecyclerCallback<ItemContainerSwipeBinding, MyСontainer> {
                            override fun bind(binder: ItemContainerSwipeBinding, entity: MyСontainer, position: Int,itemView: View) {
                                swipeHolderAction(binder, entity, position, itemView)
                                subscribeClickPosition(binder.include.clicableLayout, entity.id)

                                Loger.log("id ${entity.id}")

                               //Расчет кол-ва
/*
                                commonQuantity.observe(this@ContainFragment.viewLifecycleOwner , {
                                    it?.let {
                                        binder.include.quantity.text=it.toString()
                                    }
                                })*/

                            }
                        })
               // val adapter= SwipeRecyclerAdapter_UNUSED(requireContext(),it, viewModel)
                recycler.adapter=adapter
                //adapter.notifyDataSetChanged()
            })

        }

        return view
    }
    fun backStack(){
        navController.popBackStack()
    }
    fun createNew( view: View){
        ViewDialog.showCreateCalculationDialog(requireContext(), "Введите номер контейнера"){
            viewModel.addNew(it,idOfCalculate!!)
        }
    }

    fun goToTrees(v:View){
        Navigation.findNavController(v).navigate(R.id.action_containersFragment_to_treesFragment)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.contains_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ backStack(); return true }
            else ->{
                return super.onOptionsItemSelected(item)}
        }
    }

    fun subscribeClickPosition(clicableLayout: View, idPosition: Long){
        clicableLayout.setOnClickListener {
            val bundle= Bundle()
            bundle.putLong(BUNDLE_CONTAINER_ID, idPosition)
            navController.navigate(R.id.action_containersFragment_to_treesFragment,bundle)
        }
    }

    private fun swipeHolderAction(binder: ItemContainerSwipeBinding, entity: MyСontainer, position: Int, itemView: View){
        with(binder){
            this.entity=entity
            val textView=binder.include.quantity

            //------------------------------------------------
            lifecycleScope.launch {
               val quantity= async { withContext(Dispatchers.IO){
                   Loger.log(viewModel.getQuantity(entity.id).toString()+"в корутинах //////////")
                   viewModel.getQuantity(entity.id) }
               }
                textView.text=quantity.await().toString()
            }
            //------------------------------------------------

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
                viewModel.deltePosition(entity, idOfCalculate!!)

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