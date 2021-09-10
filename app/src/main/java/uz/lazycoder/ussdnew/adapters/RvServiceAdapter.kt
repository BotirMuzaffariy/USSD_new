package uz.lazycoder.ussdnew.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.ussdnew.database.entity.ServiceEntity
import uz.lazycoder.ussdnew.databinding.ItemRvServiceBinding

class RvServiceAdapter(
    private val list: List<ServiceEntity>,
    val listener: ServiceOnClickListener
) :
    RecyclerView.Adapter<RvServiceAdapter.ServiceVh>() {

    inner class ServiceVh(var itemBinding: ItemRvServiceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(serviceM: ServiceEntity) {
            itemBinding.apply {
                tvName.text = serviceM.name
                tvServiceCode.text = serviceM.code
                tvDescription.text = serviceM.desc

                cvDesc.setOnClickListener { listener.onItemClick(serviceM) }
            }
        }
    }

    interface ServiceOnClickListener {
        fun onItemClick(serviceM: ServiceEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceVh {
        return ServiceVh(
            ItemRvServiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ServiceVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size

}