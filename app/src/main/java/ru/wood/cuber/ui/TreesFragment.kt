package ru.wood.cuber.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.daimajia.swipe.SwipeLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.wood.cuber.R
import ru.wood.cuber.adapters.RecyclerCallback
import ru.wood.cuber.adapters.SimpleRecyclerAdapter
import ru.wood.cuber.adapters.SwipeRecyclerAdapter2
import ru.wood.cuber.data.MyCalculation
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.databinding.FragmentTreesBinding
import ru.wood.cuber.databinding.ItemCalculateSwipeBinding
import ru.wood.cuber.databinding.ItemTreePositionBinding
import ru.wood.cuber.databinding.ItemTreesSwipeBinding
import ru.wood.cuber.view_models.TreesViewModel

@AndroidEntryPoint
class TreesFragment : Fragment(), SimpleRecyclerAdapter.OnPositionClickListener{
    private lateinit var navController: NavController
    private val viewModel: TreesViewModel by viewModels()
    private lateinit var adapter: SwipeRecyclerAdapter2<TreePosition, ItemTreesSwipeBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        navController= NavHostFragment.findNavController(this)
        val view= binding.root
        val recycler=binding.recycler

        val id= arguments?.getInt("id")
        with(viewModel){
            id?.let {getListTrees(it)}
            liveData.observe(viewLifecycleOwner, {
                it?.let {
                    if (it==null){return@observe}
                    adapter=SwipeRecyclerAdapter2(it, R.layout.item_trees_swipe,
                            object : RecyclerCallback<ItemTreesSwipeBinding, TreePosition>{
                                override fun bind(binder: ItemTreesSwipeBinding, entity: TreePosition, position: Int, itemView: View) {
                                    swipeHolderAction(binder, entity, position, itemView)
                                    subscribeClickPosition(binder.include.clicableLayout,entity.id)
                                }
                            })
                    recycler.adapter=adapter
                    adapter.notifyDataSetChanged()

                    actionBar?.title = "name"
                }
            })
        }

        return view
    }

    fun subscribeClickPosition(clicableLayout: View, idPosition: Int){
        clicableLayout.setOnClickListener {
            val bundle= Bundle()
            bundle.putInt("id", idPosition)
            //navController.navigate(R.id.,bundle)
        }
    }
    fun backStack(){
        navController.popBackStack()
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
    private fun swipeHolderAction(binder: ItemTreesSwipeBinding, entity: TreePosition, position: Int, itemView: View){
        with(binder){
            this.entity=entity
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
                bundle.putInt("id", entity.id)
                Navigation.findNavController(v).navigate(R.id.treesFragment, bundle)
            })

            Delete.setOnClickListener(View.OnClickListener { v ->
                //viewModel.deleteExactly(list[position].id) //Удаление из БД

                adapter.apply {
                    mItemManger.removeShownLayouts(binder.swipe)
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, adapter.list.size)
                    mItemManger.closeAllItems()
                }

                Toast.makeText(requireContext(), "Deleted " + binder.include.entity?.id,
                        Toast.LENGTH_SHORT).show()
            })

        }
        adapter.mItemManger.bindView(itemView, position)
    }
}