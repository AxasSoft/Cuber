package ru.wood.cuber.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import ru.wood.cuber.R
import ru.wood.cuber.utill.Utill.BUNDLE_ID
import ru.wood.cuber.adapters.CommonRecyclerAdapter
import ru.wood.cuber.adapters.RecyclerCallback
import ru.wood.cuber.data.TreePosition
import ru.wood.cuber.databinding.FragmentResultBinding
import ru.wood.cuber.databinding.ItemResultPositionBinding
import ru.wood.cuber.view_models.ResultViewModel

@AndroidEntryPoint
class ResultFragment : Fragment() {
    private var navController: NavController? =null
    val viewModel: ResultViewModel by viewModels()
    private var idOfContain : Long? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        navController= Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        idOfContain=arguments?.getLong(BUNDLE_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentResultBinding.inflate(inflater)
        binding.fragment=this
        val recycler = binding.recycler
        val view = binding.root

        with(viewModel){
            idOfContain?.let {
                getListPosition(it)
                liveData.observe(viewLifecycleOwner, {
                    val adapter = CommonRecyclerAdapter<TreePosition, ItemResultPositionBinding>(
                        it, R.layout.item_result_position,

                        object : RecyclerCallback<ItemResultPositionBinding, TreePosition> {
                            override fun bind(
                                binder: ItemResultPositionBinding,
                                entity: TreePosition,
                                position: Int,
                                itemView: View
                            ) {
                                binder.entity=entity
                            }
                        }
                        )
                    recycler.adapter=adapter
                    adapter.notifyDataSetChanged()
                })
            }
        }

        return view
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
    fun backStack(){
        navController?.popBackStack()
    }

    fun containNameChange (s: CharSequence, start: Int, count: Int, after:Int){

    }

    fun weightChange (s: CharSequence, start: Int, count: Int, after:Int){

    }
}