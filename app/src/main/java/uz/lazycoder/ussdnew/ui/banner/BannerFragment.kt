package uz.lazycoder.ussdnew.ui.banner

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.utils.Utils
import uz.lazycoder.ussdnew.database.entity.TariffEntity
import uz.lazycoder.ussdnew.databinding.FragmentBannerBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class BannerFragment : Fragment() {

    private lateinit var tariffM: TariffEntity
    private lateinit var binding: FragmentBannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tariffM = it.getSerializable("tariff") as TariffEntity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBannerBinding.inflate(inflater, container, false)

        binding.apply {
            tvName.text = tariffM.name
            tvAbout.text = tariffM.shortDesc
            tvTariffName.text = tariffM.name
            tvMinute.text = tariffM.time
            tvMb.text = tariffM.mb
            tvSms.text = tariffM.sms
            tvCost.text =
                "${tariffM.cost} ${resources.getString(R.string.currency)}/${resources.getString(R.string.month)}"
            tvTariffCost.text = "${tariffM.cost} ${resources.getString(R.string.currency)}"

            btnMoreInfo.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(resources.getString(R.string.tv_more_info))
                builder.setMessage(tariffM.longDesc)
                builder.setNegativeButton(resources.getString(R.string.tv_back)) { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            }

            btnConnect.setOnClickListener {
                askPermission(Manifest.permission.CALL_PHONE) {
                    Utils.callTo(requireContext(), tariffM.code ?: "")
                }.onDeclined {
                    val builder2 = AlertDialog.Builder(requireContext())
                    builder2.setTitle(getString(R.string.tv_info))

                    if (it.hasDenied()) {
                        builder2.setMessage(getString(R.string.tv_per_denied))
                        builder2.setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            it.askAgain()
                        }
                    }

                    if (it.hasForeverDenied()) {
                        builder2.setMessage(getString(R.string.tv_per_forever_denied))
                        builder2.setPositiveButton(getString(R.string.tv_settings)) { dialog, _ ->
                            dialog.dismiss()
                            it.goToSettings()
                        }
                    }

                    builder2.show()
                }
            }
        }

        return binding.root
    }

}