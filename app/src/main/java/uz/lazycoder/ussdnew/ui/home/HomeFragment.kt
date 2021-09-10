package uz.lazycoder.ussdnew.ui.home

import android.view.View
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.MainActivity
import uz.lazycoder.ussdnew.adapters.VpBannerAdapter
import androidx.navigation.Navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import uz.lazycoder.ussdnew.databinding.FragmentHomeBinding
import uz.lazycoder.ussdnew.databinding.ItemTabIndicatorBinding
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2

class HomeFragment : Fragment() {

    private val vpScrollDelayMills: Long = 3000

    private lateinit var vpHandler: Handler
    private lateinit var vpRunnable: Runnable
    private lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = requireActivity() as MainActivity
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        mainActivity.restoreToolbarState()
        vpHandler = Handler(Looper.getMainLooper())

        binding.apply {
            vp2.adapter = VpBannerAdapter(requireActivity())

            val pageTransformer = CardFlipPageTransformer2()
            pageTransformer.isScalable = false

            vp2.setPageTransformer(pageTransformer)

            TabLayoutMediator(tlIndicator, vp2) { tab, _ ->
                val tabBinding = ItemTabIndicatorBinding.inflate(layoutInflater)
                tab.customView = tabBinding.root
                tab.view.isClickable = false
            }.attach()

            vpRunnable = object : Runnable {
                override fun run() {
                    var currentItem = vp2.currentItem
                    if (currentItem == 4) currentItem = -1
                    vp2.setCurrentItem(currentItem + 1, true)
                    vpHandler.postDelayed(this, vpScrollDelayMills)
                }
            }

            vpHandler.postDelayed(vpRunnable, vpScrollDelayMills)

            cvUssd.setOnClickListener {
                findNavController(requireActivity(), R.id.my_nav_host_fragment).navigate(R.id.USSDFragment)
                mainActivity.changeToolbarState(getString(R.string.tv_title_ussd))
            }

            cvTariff.setOnClickListener {
                findNavController(requireActivity(), R.id.my_nav_host_fragment).navigate(R.id.tariffFragment)
                mainActivity.changeToolbarState(getString(R.string.tv_title_tariff))
            }

            cvService.setOnClickListener {
                findNavController(requireActivity(), R.id.my_nav_host_fragment).navigate(R.id.serviceFragment)
                mainActivity.changeToolbarState(getString(R.string.tv_title_service)) }

            cvMinute.setOnClickListener {
                findNavController(requireActivity(), R.id.my_nav_host_fragment).navigate(R.id.minuteFragment)
                mainActivity.changeToolbarState(getString(R.string.tv_title_minute)) }

            cvMb.setOnClickListener {
                findNavController(requireActivity(), R.id.my_nav_host_fragment).navigate(R.id.mbFragment)
                mainActivity.changeToolbarState(getString(R.string.tv_title_mb)) }

            cvSms.setOnClickListener {
                findNavController(requireActivity(), R.id.my_nav_host_fragment).navigate(R.id.smsFragment)
                mainActivity.changeToolbarState(getString(R.string.tv_title_sms)) }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::vpHandler.isInitialized) vpHandler.removeCallbacks(vpRunnable)
    }

}