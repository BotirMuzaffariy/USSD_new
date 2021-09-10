package uz.lazycoder.ussdnew.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.ussdnew.databinding.ItemRvNewsBinding
import uz.lazycoder.ussdnew.database.entity.NewsAndSaleEntity

class RvNewsAdapter(private val list: List<NewsAndSaleEntity>, val listener: NewsOnClickListener) :
    RecyclerView.Adapter<RvNewsAdapter.RvNewsVh>() {

    inner class RvNewsVh(var itemBinding: ItemRvNewsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(newsAndSaleM: NewsAndSaleEntity) {
            itemBinding.apply {
                tvTime.text = newsAndSaleM.time
                tvName.text = newsAndSaleM.name
                tvDescription.text = newsAndSaleM.desc
                cvNews.setOnClickListener { listener.onItemClick(newsAndSaleM) }
            }
        }
    }

    interface NewsOnClickListener {
        fun onItemClick(newsAndSaleM: NewsAndSaleEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvNewsVh {
        return RvNewsVh(
            ItemRvNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RvNewsVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size

}