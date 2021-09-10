package uz.lazycoder.ussdnew.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.ussdnew.database.entity.TariffEntity
import uz.lazycoder.ussdnew.databinding.ItemRvTariffBinding

class RvTariffAdapter(private val list: List<TariffEntity>, val listener: TariffOnClickListener) :
    RecyclerView.Adapter<RvTariffAdapter.TariffVh>() {

    inner class TariffVh(var itemBinding: ItemRvTariffBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(tariffM: TariffEntity) {
            itemBinding.apply {
                tvName.text = tariffM.name
                tvDesc.text = tariffM.shortDesc

                root.setOnClickListener { listener.onItemClick(tariffM) }
            }
        }
    }

    interface TariffOnClickListener {
        fun onItemClick(tariffM: TariffEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TariffVh {
        return TariffVh(
            ItemRvTariffBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TariffVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size

}