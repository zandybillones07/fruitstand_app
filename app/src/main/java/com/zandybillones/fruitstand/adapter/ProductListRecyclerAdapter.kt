package com.zandybillones.fruitstand.adapter

import android.app.Activity
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import com.zandybillones.fruitstand.R
import com.zandybillones.fruitstand.model.Product
import com.zandybillones.traceutil.Trace
import kotlinx.android.synthetic.main.layout_brand_item.view.name
import kotlinx.android.synthetic.main.layout_product_item.view.*

class ProductListRecyclerAdapter(activity: Activity, list:ArrayList<Product>) : RecyclerView.Adapter<ProductListRecyclerAdapter.ItemHolder>() {

    var list = list
    var activity =  activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.layout_product_item, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHolder, pos: Int) {
        holder.bindData(list[pos])
    }

    open class ItemHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view = v

        fun bindData(product: Product) {
            view.name.text = "" + product.name
            view.checkbox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                Trace.show("isCheck?" + b)
                product.isSelected = b

            }
            view.quantity_btn.setOnClickListener {
                showPopupMenu(view.quantity_btn, product)
            }
        }
        fun showPopupMenu(view: View, product:Product) = PopupMenu(view.context, view).run {
            menuInflater.inflate(R.menu.quantity_menu, menu)
            setOnMenuItemClickListener { item ->
                (view as Button).text = item.title
                product.quantity = item.title as String
                true
            }
            show()
        }

    }


}