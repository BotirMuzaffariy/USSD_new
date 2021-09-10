package uz.lazycoder.ussdnew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.lazycoder.ussdnew.ui.banner.VpBannerFragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpBannerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return VpBannerFragment.newInstance(position)
    }

}