package uz.lazycoder.ussdnew.ui.minute

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
import uz.lazycoder.ussdnew.adapters.RvMinuteAdapter
import uz.lazycoder.ussdnew.database.entity.MinuteEntity
import uz.lazycoder.ussdnew.databinding.FragmentVpMinuteBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class VpMinuteFragment : Fragment() {

    private var vpPosition = 0
    private val argParam = "vp_position"

    private lateinit var minuteList: List<MinuteEntity>
    private lateinit var binding: FragmentVpMinuteBinding

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
        binding = FragmentVpMinuteBinding.inflate(inflater, container, false)

        loadList()

        binding.apply {

            btnCheckMinute.setOnClickListener {
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

            rvVpMinute.adapter =
                RvMinuteAdapter(minuteList, object : RvMinuteAdapter.MinuteOnClickListener {
                    override fun onItemClick(minuteM: MinuteEntity) {
                        val builder = AlertDialog.Builder(requireContext())

                        builder.setTitle(minuteM.name)
                        builder.setMessage(minuteM.desc)

                        builder.setNegativeButton(resources.getString(R.string.tv_back)) { dialog, _ ->
                            dialog.dismiss()
                        }

                        builder.setPositiveButton(resources.getString(R.string.tv_connect)) { dialog, _ ->
                            askPermission(Manifest.permission.CALL_PHONE) {
                                dialog.dismiss()
                                Utils.callTo(requireContext(), minuteM.code ?: "")
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
        minuteList = ArrayList()
        var list: List<MinuteEntity> = emptyList()

        when (MySharedPrefs.getInstance(requireContext()).getLang()) {
            Consts.LANG_UZ -> {
                list =
                    AppDbUz.getInstance(requireContext()).minuteDao().getAllMinutes() ?: emptyList()
            }
            Consts.LANG_RU -> {
                list = AppDbRu.getInstance(requireContext()).minuteDao().getAllMinutes()
                    ?: emptyList()
            }
            Consts.LANG_UZ_KRILL -> {
                list = AppDbUzKr.getInstance(requireContext()).minuteDao().getAllMinutes()
                    ?: emptyList()
            }
        }

        minuteList = if (vpPosition == 0) {
            list.filter { it.type == Consts.TYPE_MINUTE }
        } else {
            list.filter { it.type == Consts.TYPE_MINUTE_EXCHANGE }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            VpMinuteFragment().apply {
                arguments = Bundle().apply {
                    putInt(argParam, position)
                }
            }
    }
}