package com.example.crypto.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.crypto.adapter.TopLossGainPagerAdapter
import com.example.crypto.adapter.TopMarketAdapter
import com.example.crypto.apis.ApiInterface
import com.example.crypto.apis.ApiUtilities
import com.example.crypto.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {



     private  lateinit var  binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)


        getTopCurrencyList()

        setTabLayout()

        return binding.root

    }

    private fun setTabLayout() {
       val adapter =TopLossGainPagerAdapter(this)
        binding.contentViewPager.adapter = adapter
        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            if(position == 0){
                binding.topGainIndicator.visibility = VISIBLE
                binding.topGainIndicator.visibility = GONE
            }else {
                binding.topGainIndicator.visibility = GONE
                binding.topGainIndicator.visibility = VISIBLE

            }
            }
        })

        TabLayoutMediator(binding.tabLayout,binding.contentViewPager){
            tab,position ->
            var title = if(position == 0 ){
                "Top Gainers"
            }else{
                "Top Losers"
            }
            tab.text = title
        }.attach()
    }

    private fun getTopCurrencyList(){
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            withContext(Dispatchers.Main){
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(),res.body()!!.data.cryptoCurrencyList)
            }

        Log.d("THIAGO","getTopCurrencyList: ${res.body()!!.data.cryptoCurrencyList}")
        }
    }


}