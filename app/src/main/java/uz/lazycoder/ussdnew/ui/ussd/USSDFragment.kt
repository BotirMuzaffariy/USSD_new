package uz.lazycoder.ussdnew.ui.ussd

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import uz.lazycoder.ussdnew.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lazycoder.ussdnew.utils.Utils
import uz.lazycoder.ussdnew.utils.Consts
import androidx.appcompat.app.AlertDialog
import uz.lazycoder.ussdnew.database.AppDbUz
import uz.lazycoder.ussdnew.database.AppDbRu
import uz.lazycoder.ussdnew.database.AppDbUzKr
import uz.lazycoder.ussdnew.utils.MySharedPrefs
import uz.lazycoder.ussdnew.adapters.RvUssdAdapter
import uz.lazycoder.ussdnew.database.entity.UssdEntity
import uz.lazycoder.ussdnew.databinding.FragmentUSSDBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class USSDFragment : Fragment() {

    private lateinit var ussdList: List<UssdEntity>
    private lateinit var binding: FragmentUSSDBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUSSDBinding.inflate(inflater, container, false)

        loadList()

        binding.apply {
            rvUssd.adapter = RvUssdAdapter(
                ussdList,
                object : RvUssdAdapter.UssdOnClickListener {
                    override fun onBtnClick(code: String) {
                        askPermission(Manifest.permission.CALL_PHONE) {
                            Utils.callTo(requireContext(), code)
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
                })
        }

        return binding.root
    }

    private fun loadList() {
        ussdList = ArrayList()

        when (MySharedPrefs.getInstance(requireContext()).getLang()) {
            Consts.LANG_UZ -> {
                ussdList = AppDbUz.getInstance(requireContext()).ussdDao().getAllUssd()
                    ?: emptyList()
            }
            Consts.LANG_RU -> {
                ussdList = AppDbRu.getInstance(requireContext()).ussdDao().getAllUssd()
                    ?: emptyList()
            }
            Consts.LANG_UZ_KRILL -> {
                ussdList = AppDbUzKr.getInstance(requireContext()).ussdDao().getAllUssd()
                    ?: emptyList()
            }
        }
    }

}