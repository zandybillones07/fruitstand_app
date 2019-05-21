package com.zandybillones.fruitstand

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.zandybillones.fruitstand.model.Order
import com.zandybillones.fruitstand.viewmodel.OrderSummaryViewModel
import com.zandybillones.traceutil.Trace
import kotlinx.android.synthetic.main.activity_order_summary.*
import kotlinx.android.synthetic.main.layout_summary_item.view.*

class OrderSummaryActivity : AppCompatActivity() {


    lateinit var viewModel:OrderSummaryViewModel
    lateinit var id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        if (intent.hasExtra("id")) {
            val id = intent.getStringExtra("id")
            if (id != null) {
                this@OrderSummaryActivity.id = id
                init(id)
            }
        }

        initClickListeners()

    }

    private fun initClickListeners() {
        receive_btn.setOnClickListener {
            viewModel.setReceive(id)
        }
    }

    private fun init(id:String) {
        viewModel = ViewModelProviders.of(this).get(OrderSummaryViewModel::class.java)
        viewModel.init(id)
        viewModel.getOrder().observe(this, object : Observer<Order> {
            override fun onChanged(t: Order?) {
                customer_name.text = t!!.fullname
                customer_contact.text = t!!.contact
                for (item in t!!.items) {
                    val view = LayoutInflater.from(this@OrderSummaryActivity).inflate(R.layout.layout_summary_item,null) as LinearLayout;
                    view.name.text = item.name
                    view.quantity.text = "Qty: " + item.quantity
                    container.addView(view)
                }
            }

        })
        viewModel.isReceive().observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                Trace.show("IS RECEIVE??? " + t)
                receive_btn.text = "Order Received!"
            }

        })

    }
}
