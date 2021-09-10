package uz.lazycoder.ussdnew.ui.internet

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
import uz.lazycoder.ussdnew.adapters.RvInternetAdapter
import uz.lazycoder.ussdnew.database.entity.InternetEntity
import uz.lazycoder.ussdnew.databinding.FragmentVpInternetBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class VpInternetFragment : Fragment() {

    private var vpPosition = 0
    private val argParam = "vp_position"

    private lateinit var internetList: ArrayList<InternetEntity>
    private lateinit var binding: FragmentVpInternetBinding

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
        binding = FragmentVpInternetBinding.inflate(inflater, container, false)

        loadList()

        binding.apply {

            btnCheckTraffic.setOnClickListener {
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

            rvVpInternet.adapter =
                RvInternetAdapter(internetList, object : RvInternetAdapter.InternetOnClickListener {
                    override fun onItemClick(internetM: InternetEntity) {
                        val builder = AlertDialog.Builder(requireContext())

                        builder.setTitle(internetM.name)
                        builder.setMessage(internetM.desc)

                        builder.setNegativeButton(resources.getString(R.string.tv_back)) { dialog, _ ->
                            dialog.dismiss()
                        }

                        builder.setPositiveButton(resources.getString(R.string.tv_connect)) { dialog, _ ->
                            askPermission(Manifest.permission.CALL_PHONE) {
                                dialog.dismiss()
                                Utils.callTo(requireContext(), internetM.code?:"")
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
        internetList = ArrayList()
        var list: List<InternetEntity> = emptyList()
        val lang = MySharedPrefs.getInstance(requireContext()).getLang()

        when (lang) {
            Consts.LANG_UZ -> {
                list = AppDbUz.getInstance(requireContext()).internetDao().getAllInternet()
                    ?: emptyList()
            }
            Consts.LANG_RU -> {
                list = AppDbRu.getInstance(requireContext()).internetDao().getAllInternet()
                    ?: emptyList()
            }
            Consts.LANG_UZ_KRILL -> {
                list = AppDbUzKr.getInstance(requireContext()).internetDao().getAllInternet()
                    ?: emptyList()
            }
        }

        when (vpPosition) {
            0 -> {
                internetList.addAll(list.filter { it.type == Consts.TYPE_INTERNET_DAY_NON_STOP })
            }
            1 -> {
                internetList.addAll(list.filter { it.type == Consts.TYPE_INTERNET_DAILY })
            }
            2 -> {
                internetList.addAll(list.filter { it.type == Consts.TYPE_INTERNET_MONTHLY })
            }
            3 -> {
                internetList.addAll(list.filter { it.type == Consts.TYPE_INTERNET_FOR_TAS_IX })
            }
            4 -> {
                internetList.addAll(list.filter { it.type == Consts.TYPE_INTERNET_INTERNET_NON_STOP })
            }
            else -> emptyList<InternetEntity>()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            VpInternetFragment().apply {
                arguments = Bundle().apply {
                    putInt(argParam, position)
                }
            }
    }
}