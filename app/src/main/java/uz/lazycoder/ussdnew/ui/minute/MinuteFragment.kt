package uz.lazycoder.ussdnew.ui.minute

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.adapters.VpMinuteAdapter
import com.google.android.material.tabs.TabLayoutMediator
import uz.lazycoder.ussdnew.databinding.ItemCustomTabBinding
import uz.lazycoder.ussdnew.databinding.FragmentMinuteBinding

class MinuteFragment : Fragment() {

    private lateinit var binding: FragmentMinuteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMinuteBinding.inflate(inflater, container, false)

        binding.apply {
            vp2Minute.adapter = VpMinuteAdapter(requireActivity())

            TabLayoutMediator(tlMinute, vp2Minute) { tab, position ->
                val tabBinding = ItemCustomTabBinding.inflate(layoutInflater)
                tab.customView = tabBinding.root
                tabBinding.tvTabName.text =
                    if (position == 0) resources.getString(R.string.tv_minute_packages)
                    else resources.getString(R.string.tv_useful_exchange)
            }.attach()
        }

        return binding.root
    }

}