package uz.lazycoder.ussdnew.ui.service

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
import uz.lazycoder.ussdnew.adapters.RvServiceAdapter
import uz.lazycoder.ussdnew.database.entity.ServiceEntity
import uz.lazycoder.ussdnew.databinding.FragmentServiceBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class ServiceFragment : Fragment() {

    private lateinit var binding: FragmentServiceBinding
    private lateinit var serviceList: List<ServiceEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServiceBinding.inflate(inflater, container, false)

        loadList()

        binding.apply {
            rvService.adapter = RvServiceAdapter(serviceList, object: RvServiceAdapter.ServiceOnClickListener{
                override fun onItemClick(serviceM: ServiceEntity) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(serviceM.name)
                    builder.setMessage(serviceM.desc)
                    builder.setNegativeButton(resources.getString(R.string.tv_back)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    if (serviceM.code != "-") {
                        builder.setPositiveButton(resources.getString(R.string.tv_connect)) { dialog, _ ->
                            askPermission(Manifest.permission.CALL_PHONE) {
                                dialog.dismiss()
                                Utils.callTo(requireContext(), serviceM.code?:"")
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
                    builder.show()
                }
            })
        }

        return binding.root
    }

    private fun loadList() {
        serviceList = ArrayList()

        when (MySharedPrefs.getInstance(requireContext()).getLang()) {
            Consts.LANG_UZ -> {
                serviceList = AppDbUz.getInstance(requireContext()).serviceDao().getAllServices()
                    ?: emptyList()
            }
            Consts.LANG_RU -> {
                serviceList = AppDbRu.getInstance(requireContext()).serviceDao().getAllServices()
                    ?: emptyList()
            }
            Consts.LANG_UZ_KRILL -> {
                serviceList = AppDbUzKr.getInstance(requireContext()).serviceDao().getAllServices()
                        ?: emptyList()
            }
        }
    }

}