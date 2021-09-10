package uz.lazycoder.ussdnew.ui.tariff

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.utils.Utils
import uz.lazycoder.ussdnew.utils.Consts
import uz.lazycoder.ussdnew.database.AppDbRu
import uz.lazycoder.ussdnew.database.AppDbUz
import uz.lazycoder.ussdnew.database.AppDbUzKr
import uz.lazycoder.ussdnew.utils.MySharedPrefs
import uz.lazycoder.ussdnew.adapters.RvTariffAdapter
import uz.lazycoder.ussdnew.database.entity.TariffEntity
import uz.lazycoder.ussdnew.databinding.FragmentTariffBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class TariffFragment : Fragment() {

    private lateinit var tariffList: List<TariffEntity>
    private lateinit var binding: FragmentTariffBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTariffBinding.inflate(inflater, container, false)

        loadList()

        binding.apply {

            btnCheckTariff.setOnClickListener {
                askPermission(Manifest.permission.CALL_PHONE) {
                    Utils.callTo(requireContext(), "*105#")
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

            rvTariff.adapter =
                RvTariffAdapter(tariffList, object : RvTariffAdapter.TariffOnClickListener {
                    override fun onItemClick(tariffM: TariffEntity) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle(tariffM.name)
                        builder.setMessage(tariffM.shortDesc)
                        builder.setNegativeButton(resources.getString(R.string.tv_back)) { dialog, _ -> dialog.dismiss() }
                        builder.setNeutralButton(resources.getString(R.string.tv_more_info)) { dialog, _ ->
                            dialog.dismiss()
                            val builder2 = AlertDialog.Builder(requireContext())
                            builder2.setTitle(tariffM.name)
                            builder2.setMessage(tariffM.longDesc)
                            builder2.setNegativeButton(resources.getString(R.string.tv_back)) { dialog2, _ -> dialog2.dismiss() }
                            builder2.show()
                        }
                        if (tariffM.code != "-") builder.setPositiveButton(resources.getString(R.string.tv_connect)) { dialog, _ ->
                            askPermission(Manifest.permission.CALL_PHONE) {
                                dialog.dismiss()
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
                        builder.show()
                    }
                })
        }

        return binding.root
    }

    private fun loadList() {
        tariffList = ArrayList()

        when (MySharedPrefs.getInstance(requireContext()).getLang()) {
            Consts.LANG_UZ -> {
                tariffList = AppDbUz.getInstance(requireContext()).tariffDao().getAllTariff()
                    ?: emptyList()
            }
            Consts.LANG_RU -> {
                tariffList = AppDbRu.getInstance(requireContext()).tariffDao().getAllTariff()
                    ?: emptyList()
            }
            Consts.LANG_UZ_KRILL -> {
                tariffList = AppDbUzKr.getInstance(requireContext()).tariffDao().getAllTariff()
                    ?: emptyList()
            }
        }
    }

}