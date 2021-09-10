package uz.lazycoder.ussdnew.ui.news

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.adapters.VpNewsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import uz.lazycoder.ussdnew.databinding.FragmentNewsBinding
import uz.lazycoder.ussdnew.databinding.ItemCustomTabBinding

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        binding.apply {
            vp2News.adapter = VpNewsAdapter(requireActivity())

            TabLayoutMediator(tlNews, vp2News) { tab, position ->
                val tabBinding = ItemCustomTabBinding.inflate(layoutInflater)
                tab.customView = tabBinding.root
                tabBinding.tvTabName.text =
                    if (position == 0) resources.getString(R.string.tv_news) else resources.getString(
                        R.string.tv_sale
                    )
            }.attach()
        }

        return binding.root
    }

}