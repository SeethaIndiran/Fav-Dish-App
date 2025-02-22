package com.example.favdish.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.databinding.FragmentRandomDishesBinding
import com.example.favdish.models.application.FavDishApplication
import com.example.favdish.models.entities.FavDish
import com.example.favdish.models.entities.RandomDish
import com.example.favdish.utils.Constants
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import com.example.favdish.viewmodel.RandomDishViewModel

class RandomDishesFragment : Fragment() {

   private var mBinding: FragmentRandomDishesBinding? = null
    private  lateinit var mRandomDishViewModel: RandomDishViewModel
    private var mProgressDialog: Dialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentRandomDishesBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)

        mRandomDishViewModel.getRandomRecipeFromApi()

        randomDishViewModelObserver()

        mBinding!!.srlRandomDish.setOnRefreshListener {
            mRandomDishViewModel.getRandomRecipeFromApi()
        }

    }



    private  fun randomDishViewModelObserver(){
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner,{
            randomDishResponse->
            randomDishResponse?.let {

                if(mBinding!!.srlRandomDish.isRefreshing){
                    mBinding!!.srlRandomDish.isRefreshing = false
                }

                Log.i("Random Dish Response","${randomDishResponse.recipes[0]}")
             setUpRandomDishresponseInUI(randomDishResponse.recipes[0])

            }
        })
        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner,{
            dataError->
            dataError?.let {
                Log.e("Random Dish  Api Error","$dataError")
                if(mBinding!!.srlRandomDish.isRefreshing){
                    mBinding!!.srlRandomDish.isRefreshing = false
                }

            }
        })
        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner,{
            loadRandomDish->
            loadRandomDish?.let {
                Log.i("Random Dish Loading","$loadRandomDish")

                if(loadRandomDish && !mBinding!!.srlRandomDish.isRefreshing){
                    showCustomProgressDialog()
                }else{
                    hideCustomProgressDialog()
                }
            }
        })
    }

    private  fun showCustomProgressDialog(){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideCustomProgressDialog(){
        mProgressDialog?.dismiss()
    }

    private  fun setUpRandomDishresponseInUI(recipe:RandomDish.Recipe){

        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(mBinding!!.ivDishImage)

        mBinding!!.tvTitle.text = recipe.title

        var dishType = "other"

        if(recipe.dishTypes.isNotEmpty()){
            dishType = recipe.dishTypes[0]
            mBinding!!.tvType.text = dishType
        }

        mBinding!!.tvCategory.text = "other"

        var ingredients = ""

        for(value in recipe.extendedIngredients){
            if(ingredients.isEmpty()){
                ingredients = value.original
            }else{
                ingredients = ingredients + ", \n" + value.original
            }
        }

        mBinding!!.tvIngredients.text = ingredients

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mBinding!!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT)
        }else{
            @Suppress("DEPRECATION")
            mBinding!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        mBinding!!.ivFavouriteDish.setImageDrawable(
            ContextCompat.getDrawable(requireActivity(),
                R.drawable.ic_favourite_unselected)
        )
        var addedToFavourites = false

        mBinding!!.tvCookingTime.text = resources.getString(
            R.string.lbl_estimate_cooking_time,
            recipe.readyInMinutes.toString())

        mBinding!!.ivFavouriteDish.setOnClickListener {

            if(addedToFavourites){
                Toast.makeText(requireActivity(),resources.getString(
                    R.string.msg_already_added_to_favourites),
                    Toast.LENGTH_SHORT).show()
            }else{
                val randomDishDetails= FavDish(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true)

                val mFavDishViewModel : FavDishViewModel by viewModels {
                    FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
                }

                mFavDishViewModel.insert(randomDishDetails)

                addedToFavourites = true

                mBinding!!.ivFavouriteDish.setImageDrawable(
                    ContextCompat.getDrawable(requireActivity(),
                        R.drawable.ic_favorite_selected))

                Toast.makeText(requireActivity(),
                    resources.getString(R.string.msg_added_to_favourites),
                    Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


}