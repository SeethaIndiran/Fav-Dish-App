package com.example.favdish.view.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.databinding.ItemDishLayoutBinding
import com.example.favdish.models.entities.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.view.activities.AddUpdateDishActivity
import com.example.favdish.view.fragments.AllDishesFragment
import com.example.favdish.view.fragments.FavouriteDishesFragment

class FavDishAdapter(private val fragment:Fragment):
    RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

   private var dishes:List<FavDish> = listOf()

    class ViewHolder(view:ItemDishLayoutBinding): RecyclerView.ViewHolder(view.root){
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding:ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
            LayoutInflater.from(fragment.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text= dish.title

        holder.itemView.setOnClickListener {
            /*if(fragment is AllDishesFragment){
                fragment.dishDetails(dish)
            }
            else (fragment is FavouriteDishesFragment){
                fragment.dishDetails(dish)
            }*/

            when(fragment){
                is AllDishesFragment -> fragment.dishDetails(dish)
                is FavouriteDishesFragment -> fragment.dishDetails(dish)
            }


        }

        holder.ibMore.setOnClickListener {
            val popUp = PopupMenu(fragment.context,holder.ibMore)
            popUp.menuInflater.inflate(R.menu.menu_adapter,popUp.menu)

            popUp.setOnMenuItemClickListener {
                if(it.itemId == R.id.action_edit_dish){
                   val intent = Intent(fragment.requireActivity(),AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS,dish)
                    fragment.requireActivity().startActivity(intent)
                }else if(it.itemId == R.id.action_delete_dish){
                   if(fragment is AllDishesFragment){
                       fragment.deleteDish(dish)
                   }
                }
                true
            }
            popUp.show()
        }
        if(fragment is AllDishesFragment){
            holder.ibMore.visibility = View.VISIBLE
        }else if(fragment is FavouriteDishesFragment){
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {

       return dishes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dishesList(list:List<FavDish>){
        dishes = list
        notifyDataSetChanged()
    }
}