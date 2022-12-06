package com.test.numbers.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.numbers.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (savedInstanceState == null) {
            val fragment = FragmentDetail()
            val bundle = Bundle()
            bundle.putInt(
                FragmentDetail.item_position,
                getIntent().getIntExtra(FragmentDetail.item_position, 0)
            )
            fragment.setArguments(bundle)
            supportFragmentManager.beginTransaction().add(R.id.numbers_detail, fragment).commit()
        }
    }
}