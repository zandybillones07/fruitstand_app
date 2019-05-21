package com.zandybillones.fruitstand

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.text.TextUtils
import android.view.View
import com.zandybillones.fruitstand.adapter.ProductListRecyclerAdapter
import com.zandybillones.fruitstand.model.Brand
import com.zandybillones.fruitstand.model.Order
import com.zandybillones.fruitstand.model.Product
import com.zandybillones.fruitstand.viewmodel.BrandListViewModel
import com.zandybillones.fruitstand.viewmodel.ProductListViewModel
import com.zandybillones.traceutil.Trace
import kotlinx.android.synthetic.main.activity_product_list.*

class ProductListActivity : AppCompatActivity()  {


    lateinit var adapter : ProductListRecyclerAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var viewModel:ProductListViewModel
    lateinit var brandListVM:BrandListViewModel

    var brandId:String = ""
    var filterList = ArrayList<Brand>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        if (intent.hasExtra("brand-id")) {
            brandId = intent.getStringExtra("brand-id")
        }

        initRecyclerView()
        initFilter()
        initClickListeners()
    }

    private fun initClickListeners() {
        filter_btn.setOnClickListener {
            showPopupMenu(filter_btn)
        }
        order_btn.setOnClickListener {
            placeOrder()
        }
        brands_btn.setOnClickListener {
            val intent = Intent(this, BrandListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        viewModel.init()
        viewModel.getProductList().observe(this,
            Observer<ArrayList<Product>> {
                adapter.notifyDataSetChanged()
                Trace.show("we have changes!")
            })
        viewModel.getOrderId().observe(this, object: Observer<String> {
            override fun onChanged(t: String?) {
                if (!TextUtils.isEmpty(t)) {
                    val intent = Intent(this@ProductListActivity, OrderSummaryActivity::class.java)
                    intent.putExtra("id", t)
                    startActivity(intent)
                }

            }

        })


        linearLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = linearLayoutManager
        adapter = ProductListRecyclerAdapter(this, viewModel.getProductList().value!!)
        recycler_view.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
        brandListVM.clear()

    }

    private fun initFilter() {
        brandListVM = ViewModelProviders.of(this).get(BrandListViewModel::class.java)
        brandListVM.init()
        brandListVM.getBrandList().observe(this,
            Observer<ArrayList<Brand>> {
                filterList.clear()

                for (brand in brandListVM.getBrandList().value!!) {
                    filterList.add(brand)
                }

            })

    }

    private fun showPopupMenu(view: View) = PopupMenu(view.context, view).run {
        for (i in 0..filterList.size - 1) {
            menu.add(i, i, i, filterList[i].name)
        }
        setOnMenuItemClickListener { item ->
            Trace.show("order? " + filterList[item.order].name + " id: " + filterList[item.order].id)
            brandId = filterList[item.order].id
            //viewModel.clear()
            viewModel.getProductListByBrand(brandId)
            true
        }
        show()
    }

    private fun placeOrder() {
        val list = ArrayList<Product>()
        for (p in viewModel.getProductList().value!!) {
            if (p.isSelected) {
                list.add(p)
            }
        }
        val order = Order("Customer XYZ", "+1234567", list)
        order.isReceive = false

        if (list.size > 0) {
            viewModel.submitOrders(order)
        }
    }

}
