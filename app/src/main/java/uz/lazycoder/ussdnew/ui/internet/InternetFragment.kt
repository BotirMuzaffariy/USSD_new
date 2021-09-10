package uz.lazycoder.ussdnew.ui.internet

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.adapters.VpInternetAdapter
import com.google.android.material.tabs.TabLayoutMediator
import uz.lazycoder.ussdnew.databinding.ItemCustomTabBinding
import uz.lazycoder.ussdnew.databinding.FragmentInternetBinding

class InternetFragment : Fragment() {

    private lateinit var binding: FragmentInternetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInternetBinding.inflate(inflater, container, false)

        binding.apply {
            vp2Internet.adapter = VpInternetAdapter(requireActivity())

            TabLayoutMediator(tlInternet, vp2Internet) { tab, position ->
                val tabBinding = ItemCustomTabBinding.inflate(layoutInflater)
                tab.customView = tabBinding.root
                tabBinding.tvTabName.text = when(position) {
                    0 -> resources.getString(R.string.tv_internet_day_non_stop)
                    1 -> resources.getString(R.string.tv_internet_day)
                    2 -> resources.getString(R.string.tv_internet_month)
                    3 -> resources.getString(R.string.tv_internet_tas_ix)
                    4 -> resources.getString(R.string.tv_internet_non_stop)
                    else -> resources.getString(R.string.tv_internet_day_non_stop)
                }
            }.attach()
        }

        return binding.root
    }

}