package uz.lazycoder.ussdnew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.lazycoder.ussdnew.ui.minute.VpMinuteFragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpMinuteAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return VpMinuteFragment.newInstance(position)
    }

}