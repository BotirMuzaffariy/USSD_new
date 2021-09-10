package uz.lazycoder.ussdnew.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.ussdnew.database.entity.SmsEntity
import uz.lazycoder.ussdnew.databinding.ItemRvServiceBinding

class RvSmsAdapter(private val list: List<SmsEntity>, val listener: SmsOnClickListener) :
    RecyclerView.Adapter<RvSmsAdapter.SmsVh>() {

    inner class SmsVh(var itemBinding: ItemRvServiceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(smsM: SmsEntity) {
            itemBinding.apply {
                tvName.text = smsM.name
                tvServiceCode.text = smsM.preview
                tvDescription.text = smsM.desc

                cvDesc.setOnClickListener { listener.onItemClick(smsM) }
            }
        }
    }

    interface SmsOnClickListener {
        fun onItemClick(smsM: SmsEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsVh {
        return SmsVh(
            ItemRvServiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SmsVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size

}