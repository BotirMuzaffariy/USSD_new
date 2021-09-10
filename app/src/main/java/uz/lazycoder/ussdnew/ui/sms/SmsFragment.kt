package uz.lazycoder.ussdnew.ui.sms

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.utils.Consts
import uz.lazycoder.ussdnew.utils.MySharedPrefs
import uz.lazycoder.ussdnew.adapters.VpSmsAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import uz.lazycoder.ussdnew.databinding.FragmentSmsBinding
import uz.lazycoder.ussdnew.databinding.ItemCustomTabBinding

class SmsFragment : Fragment() {

    private lateinit var binding: FragmentSmsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmsBinding.inflate(inflater, container, false)

        binding.apply {
            vp2Sms.adapter = VpSmsAdapter(requireActivity())

            if (MySharedPrefs.getInstance(requireContext()).getLang() == Consts.LANG_RU) {
                tlSms.tabMode = TabLayout.MODE_SCROLLABLE
            }

            TabLayoutMediator(tlSms, vp2Sms) { tab, position ->
                val tabBinding = ItemCustomTabBinding.inflate(layoutInflater)
                tab.customView = tabBinding.root
                tabBinding.tvTabName.text = when (position) {
                    0 -> resources.getString(R.string.tv_internet_day)
                    1 -> resources.getString(R.string.tv_internet_month)
                    2 -> resources.getString(R.string.tv_sms_national)
                    else -> ""
                }
            }.attach()
        }

        return binding.root
    }

}