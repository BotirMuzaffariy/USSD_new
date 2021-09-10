package uz.lazycoder.ussdnew.ui.news

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
import uz.lazycoder.ussdnew.adapters.RvNewsAdapter
import uz.lazycoder.ussdnew.database.entity.NewsAndSaleEntity
import uz.lazycoder.ussdnew.databinding.FragmentVpNewsBinding

class VpNewsFragment : Fragment() {

    private lateinit var newsList: List<NewsAndSaleEntity>
    private lateinit var binding: FragmentVpNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVpNewsBinding.inflate(inflater, container, false)

        loadList()

        binding.apply {
            rvVpNews.adapter = RvNewsAdapter(newsList, object : RvNewsAdapter.NewsOnClickListener {
                override fun onItemClick(newsAndSaleM: NewsAndSaleEntity) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(newsAndSaleM.name)
                    builder.setMessage(newsAndSaleM.desc)
                    builder.setNegativeButton(resources.getString(R.string.tv_back)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    builder.setPositiveButton(resources.getString(R.string.tv_more_info)) { dialog, _ ->
                        dialog.dismiss()
                        Utils.goToLink(requireContext(), newsAndSaleM.link?:"https://uztelecom.uz")
                    }
                    builder.show()
                }
            })
        }

        return binding.root
    }

    private fun loadList() {
        newsList = ArrayList()

        when (MySharedPrefs.getInstance(requireContext()).getLang()) {
            Consts.LANG_UZ -> {
                newsList = AppDbUz.getInstance(requireContext()).newsAndSaleDao().getAllNewsAndSale()!!.filter { it.type == Consts.TYPE_NEWS }
            }
            Consts.LANG_RU -> {
                newsList = AppDbRu.getInstance(requireContext()).newsAndSaleDao().getAllNewsAndSale()!!.filter { it.type == Consts.TYPE_NEWS }
            }
            Consts.LANG_UZ_KRILL -> {
                newsList = AppDbUzKr.getInstance(requireContext()).newsAndSaleDao().getAllNewsAndSale()!!.filter { it.type == Consts.TYPE_NEWS }
            }
        }
    }

}