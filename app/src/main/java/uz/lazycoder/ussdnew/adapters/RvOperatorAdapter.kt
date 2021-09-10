package uz.lazycoder.ussdnew.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.ussdnew.database.entity.OperatorEntity
import uz.lazycoder.ussdnew.databinding.ItemRvOperatorBinding

class RvOperatorAdapter(private val list: List<OperatorEntity>, val listener: OperatorOnClickListener) :
    RecyclerView.Adapter<RvOperatorAdapter.OperatorVh>() {

    inner class OperatorVh(var itemBinding: ItemRvOperatorBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(operatorM: OperatorEntity) {
            itemBinding.apply {
                tvOperatorCode.text = operatorM.code
                tvDescription.text = operatorM.desc

                cvDesc.setOnClickListener { listener.onItemClick(operatorM) }
            }
        }
    }

    interface OperatorOnClickListener {
        fun onItemClick(operatorM: OperatorEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperatorVh {
        return OperatorVh(
            ItemRvOperatorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OperatorVh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size

}