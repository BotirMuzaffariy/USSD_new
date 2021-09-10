package uz.lazycoder.ussdnew.ui.operator

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
import uz.lazycoder.ussdnew.adapters.RvOperatorAdapter
import uz.lazycoder.ussdnew.database.entity.OperatorEntity
import uz.lazycoder.ussdnew.databinding.FragmentOperatorBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class OperatorFragment : Fragment() {

    private lateinit var binding: FragmentOperatorBinding
    private lateinit var operatorList: List<OperatorEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperatorBinding.inflate(inflater, container, false)

        loadList()

        binding.apply {
            rvOperator.adapter =
                RvOperatorAdapter(operatorList, object : RvOperatorAdapter.OperatorOnClickListener {
                    override fun onItemClick(operatorM: OperatorEntity) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle(operatorM.code)
                        builder.setMessage(operatorM.desc)
                        builder.setNegativeButton(resources.getString(R.string.tv_back)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        if (operatorM.code != "-") {
                            builder.setPositiveButton(resources.getString(R.string.tv_call)) { dialog, _ ->
                                askPermission(Manifest.permission.CALL_PHONE) {
                                    dialog.dismiss()
                                    Utils.callTo(requireContext(), operatorM.code ?: "")
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
        operatorList = ArrayList()

        when (MySharedPrefs.getInstance(requireContext()).getLang()) {
            Consts.LANG_UZ -> {
                operatorList = AppDbUz.getInstance(requireContext()).operatorDao().getAllOperators()
                    ?: emptyList()
            }
            Consts.LANG_RU -> {
                operatorList = AppDbRu.getInstance(requireContext()).operatorDao().getAllOperators()
                    ?: emptyList()
            }
            Consts.LANG_UZ_KRILL -> {
                operatorList =
                    AppDbUzKr.getInstance(requireContext()).operatorDao().getAllOperators()
                        ?: emptyList()
            }
        }

//        operatorList.add(
//            OperatorM(
//                "1099",
//                "1099 raqami orqali siz uzmobilening qo`llab-quvvatlash xizmatiga ulanasiz."
//            )
//        )
//        operatorList.add(
//            OperatorM(
//                "1011",
//                "1011 raqami orqali siz uzmobilening xizmatiga ulanasiz."
//            )
//        )
//        operatorList.add(
//            OperatorM(
//                "1008",
//                "1008 raqami orqali siz uzmobilening qandaydir bir xizmatiga ulanasiz. Lekin men buni bilmayman. Bunisini bosib ko`rsangiz bilib olasiz!"
//            )
//        )
//        operatorList.add(
//            OperatorM(
//                "-",
//                "- bu belgi orqali siz uzmobilening hech qanday xizmatiga ulanolmaysiz :)"
//            )
//        )
    }

}