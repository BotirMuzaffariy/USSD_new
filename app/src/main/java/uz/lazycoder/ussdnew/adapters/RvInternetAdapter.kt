package uz.lazycoder.ussdnew.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.ussdnew.database.entity.InternetEntity
import uz.lazycoder.ussdnew.databinding.ItemRvServiceBinding

class RvInternetAdapter(private val list: List<InternetEntity>, val listener: InternetOnClickListener) :
    RecyclerView.Adapter<RvInternetAdapter.InternetVh>() {

    inner class InternetVh(var itemBinding: ItemRvServiceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(internetM: InternetEntity) {
            itemBinding.apply {
                tvName.text = internetM.name
                tvServiceCode.text = internetM.preview
                tvDescription.text = internetM.desc

                cvDesc.setOnClickListener { listener.onItemClick(internetM) }
            }
        }
    }

    interface InternetOnClickListener {
        fun onItemClick(internetM: InternetEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InternetVh {
        return InternetVh(
            ItemRvServiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InternetVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size

}