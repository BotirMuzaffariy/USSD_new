package uz.lazycoder.ussdnew.ui.sms

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
import uz.lazycoder.ussdnew.adapters.RvSmsAdapter
import uz.lazycoder.ussdnew.database.entity.SmsEntity
import uz.lazycoder.ussdnew.databinding.FragmentVpSmsBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class VpSmsFragment : Fragment() {

    private var vpPosition = 0
    private val argParam = "vp_position"

    private lateinit var smsList: List<SmsEntity>
    private lateinit var binding: FragmentVpSmsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vpPosition = it.getInt(argParam)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVpSmsBinding.inflate(inflater, container, false)

        loadList()

        binding.apply {

            btnCheckSms.setOnClickListener {
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

            rvVpSms.adapter = RvSmsAdapter(smsList, object : RvSmsAdapter.SmsOnClickListener {
                override fun onItemClick(smsM: SmsEntity) {
                    val builder = AlertDialog.Builder(requireContext())

                    builder.setTitle(smsM.name)
                    builder.setMessage(smsM.desc)

                    builder.setNegativeButton(resources.getString(R.string.tv_back)) { dialog, _ ->
                        dialog.dismiss()
                    }

                    builder.setPositiveButton(resources.getString(R.string.tv_connect)) { dialog, _ ->
                        askPermission(Manifest.permission.CALL_PHONE) {
                            dialog.dismiss()
                            Utils.callTo(requireContext(), smsM.code ?: "")
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
        smsList = ArrayList()
        var list: List<SmsEntity> = emptyList()

        when (MySharedPrefs.getInstance(requireContext()).getLang()) {
            Consts.LANG_UZ -> {
                list =
                    AppDbUz.getInstance(requireContext()).smsDao().getAllSms() ?: emptyList()
            }
            Consts.LANG_RU -> {
                list = AppDbRu.getInstance(requireContext()).smsDao().getAllSms()
                    ?: emptyList()
            }
            Consts.LANG_UZ_KRILL -> {
                list = AppDbUzKr.getInstance(requireContext()).smsDao().getAllSms()
                    ?: emptyList()
            }
        }

        when (vpPosition) {
            0 -> {
                smsList = list.filter { it.type == Consts.TYPE_SMS_DAY }
            }
            1 -> {
                smsList = list.filter { it.type == Consts.TYPE_SMS_MONTH }
            }
            2 -> {
                smsList = list.filter { it.type == Consts.TYPE_SMS_INTERNATIONAL }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            VpSmsFragment().apply {
                arguments = Bundle().apply {
                    putInt(argParam, position)
                }
            }
    }
}