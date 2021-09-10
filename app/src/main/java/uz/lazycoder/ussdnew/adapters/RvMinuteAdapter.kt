package uz.lazycoder.ussdnew.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.ussdnew.database.entity.MinuteEntity
import uz.lazycoder.ussdnew.databinding.ItemRvServiceBinding

class RvMinuteAdapter(private val list: List<MinuteEntity>, val listener: MinuteOnClickListener) :
    RecyclerView.Adapter<RvMinuteAdapter.MinuteVh>() {

    inner class MinuteVh(var itemBinding: ItemRvServiceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(minuteM: MinuteEntity) {
            itemBinding.apply {
                tvName.text = minuteM.name
                tvServiceCode.text = minuteM.preview
                tvDescription.text = minuteM.desc

                cvDesc.setOnClickListener { listener.onItemClick(minuteM) }
            }
        }
    }

    interface MinuteOnClickListener {
        fun onItemClick(minuteM: MinuteEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinuteVh {
        return MinuteVh(
            ItemRvServiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MinuteVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size

}