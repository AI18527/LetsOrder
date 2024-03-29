package com.example.letsorder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letsorder.R
import com.example.letsorder.model.Dish
import com.example.letsorder.viewmodel.MenuViewModel
import com.example.letsorder.views.admin.DishEditListener

class MenuAdminAdapter(viewModel: MenuViewModel, private val dishEditListener: DishEditListener) :
    RecyclerView.Adapter<MenuAdminAdapter.MenuEditViewHolder>() {

    private val menu = ArrayList<Dish>()

    init {
        viewModel.menu.observeForever {
            menu.clear()
            menu.addAll(it)
            notifyDataSetChanged()
        }
    }

    class MenuEditViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.title)!!
        val category = view.findViewById<TextView>(R.id.category)!!
        val price = view.findViewById<TextView>(R.id.price)!!
        val buttonDelete = view.findViewById<Button>(R.id.buttonDelete)!!
    }

    override fun getItemCount(): Int = menu.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuEditViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu_admin_view, parent, false)
        return MenuEditViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuEditViewHolder, position: Int) {
        val dish = menu[position]

        holder.title.text = dish.title
        holder.category.text = dish.category
        holder.price.text = dish.price.toString()

        holder.buttonDelete.setOnClickListener {
            dishEditListener.dishDeleted(dish.title)
        }
    }

}