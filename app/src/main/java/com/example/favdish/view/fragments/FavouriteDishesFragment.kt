package com.example.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.databinding.FragmentFavouriteDishesBinding
import com.example.favdish.models.application.FavDishApplication
import com.example.favdish.models.entities.FavDish
import com.example.favdish.view.activities.MainActivity
import com.example.favdish.view.adapters.FavDishAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory

class FavouriteDishesFragment : Fragment() {

   private  var mBinding:FragmentFavouriteDishesBinding? = null

    private  lateinit var adapter:FavDishAdapter

    private  val mFavDishViewModel : FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFavouriteDishesBinding.inflate(inflater,container,false)

        return mBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFavDishViewModel.favouriteDishes.observe(viewLifecycleOwner){
            dishes->
            dishes.let {

                mBinding!!.rvFavouriteDishesList.layoutManager = GridLayoutManager(requireActivity(),2)
                adapter = FavDishAdapter(this)
                mBinding!!.rvFavouriteDishesList.adapter = adapter

                if(it.isNotEmpty()){
                   mBinding!!.rvFavouriteDishesList.visibility = View.VISIBLE
                    mBinding!!.tvNoDishesAvailable.visibility = View.GONE
                    adapter.dishesList(it)
                }else{
                    mBinding!!.rvFavouriteDishesList.visibility = View.GONE
                    mBinding!!.tvNoDishesAvailable.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dishDetails(favDish:FavDish){

        findNavController().navigate(FavouriteDishesFragmentDirections.actionNavigationFavouriteDishesToNavigationDishDetails(favDish))
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

}