package uz.lazycoder.ussdnew.ui.banner

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import androidx.core.os.bundleOf
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.MainActivity
import uz.lazycoder.ussdnew.utils.Consts
import uz.lazycoder.ussdnew.database.AppDbRu
import uz.lazycoder.ussdnew.database.AppDbUz
import uz.lazycoder.ussdnew.database.AppDbUzKr
import uz.lazycoder.ussdnew.utils.MySharedPrefs
import androidx.navigation.Navigation.findNavController
import uz.lazycoder.ussdnew.database.entity.TariffEntity
import uz.lazycoder.ussdnew.databinding.FragmentVpBannerBinding

class VpBannerFragment : Fragment() {

    private var position: Int = 0
    private val argParam = "vp_position"

    private lateinit var tariffM: TariffEntity
    private lateinit var binding: FragmentVpBannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(argParam)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVpBannerBinding.inflate(inflater, container, false)

        val mainActivity = requireActivity() as MainActivity

        loadTariffM()

        binding.apply {
            tvName.text = tariffM.name
            tvMinute.text = tariffM.time
            tvMb.text = tariffM.mb
            tvSms.text = tariffM.sms
            tvCost.text =
                "${tariffM.cost} ${resources.getString(R.string.currency)}/${resources.getString(R.string.month)}"

            cvMain.setOnClickListener {
                findNavController(
                    requireActivity(),
                    R.id.my_nav_host_fragment
                ).navigate(R.id.bannerFragment, bundleOf("tariff" to tariffM))
                mainActivity.changeToolbarState(tariffM.name ?: "")
            }
        }

        return binding.root
    }

    private fun loadTariffM() {
        var tariffList: List<TariffEntity> = emptyList()

        when (MySharedPrefs.getInstance(requireContext()).getLang()) {
            Consts.LANG_UZ -> {
                tariffList =
                    AppDbUz.getInstance(requireContext()).tariffDao().getAllTariff() ?: emptyList()
            }
            Consts.LANG_RU -> {
                tariffList =
                    AppDbRu.getInstance(requireContext()).tariffDao().getAllTariff() ?: emptyList()
            }
            Consts.LANG_UZ_KRILL -> {
                tariffList = AppDbUzKr.getInstance(requireContext()).tariffDao().getAllTariff()
                    ?: emptyList()
            }
        }

        tariffM = tariffList[position]
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            VpBannerFragment().apply {
                arguments = Bundle().apply {
                    putInt(argParam, position)
                }
            }
    }
}