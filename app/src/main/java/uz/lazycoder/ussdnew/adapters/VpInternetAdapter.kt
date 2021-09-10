package uz.lazycoder.ussdnew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.lazycoder.ussdnew.ui.internet.VpInternetFragment

class VpInternetAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return VpInternetFragment.newInstance(position)
    }

}