package com.zandybillones.fruitstand

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.zandybillones.fruitstand.adapter.BrandListRecyclerAdapter
import com.zandybillones.fruitstand.model.Brand
import com.zandybillones.fruitstand.viewmodel.BrandListViewModel
import kotlinx.android.synthetic.main.activity_brand_list.*

class BrandListActivity : AppCompatActivity() {

    lateinit var adapter : BrandListRecyclerAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var viewModel: BrandListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand_list)

        initRecyclerView()
    }


    private fun initRecyclerView() {
        viewModel = ViewModelProviders.of(this).get(BrandListViewModel::class.java)
        viewModel.init()
        viewModel.getBrandList().observe(this,
            Observer<ArrayList<Brand>> {
                adapter.notifyDataSetChanged()
            })

        linearLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = linearLayoutManager
        adapter = BrandListRecyclerAdapter(this, viewModel.getBrandList().value!!)
        recycler_view.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        // viewModel.clear()

    }
}
