package com.zandybillones.fruitstand.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zandybillones.fruitstand.ProductListActivity
import com.zandybillones.fruitstand.R
import com.zandybillones.fruitstand.model.Brand
import com.zandybillones.traceutil.Trace
import kotlinx.android.synthetic.main.layout_brand_item.view.*

class BrandListRecyclerAdapter(activity: Activity, list:ArrayList<Brand>) : RecyclerView.Adapter<BrandListRecyclerAdapter.ItemHolder>() {

    var list = list
    var activity =  activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.layout_brand_item, parent, false)
        return ItemHolder(view, object : ItemHolder.IItemClickListener {
            override fun onClick(_pos: Int) {}

        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHolder, pos: Int) {
        holder.bindData(list[pos], pos)
    }

    open class ItemHolder(v: View, _itemClickListener:IItemClickListener) : RecyclerView.ViewHolder(v) {

        private var view = v
        private var pos = 0
        private var itemClickListener = _itemClickListener

        init {
            v.setOnClickListener {
                itemClickListener.onClick(pos)
            }
        }

        fun bindData(brand: Brand, _pos:Int) {
            pos = _pos
            view.name.text = "" + brand.name
        }

        interface IItemClickListener {
            fun onClick(_pos: Int)
        }

    }


}