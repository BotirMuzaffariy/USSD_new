package uz.lazycoder.ussdnew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.lazycoder.ussdnew.ui.sms.VpSmsFragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpSmsAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return VpSmsFragment.newInstance(position)
    }

}