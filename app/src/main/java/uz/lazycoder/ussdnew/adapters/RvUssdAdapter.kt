package uz.lazycoder.ussdnew.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.ussdnew.database.entity.UssdEntity
import uz.lazycoder.ussdnew.databinding.ItemRvUssdBinding
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection

class RvUssdAdapter(var list: List<UssdEntity>, var listener: UssdOnClickListener) :
    RecyclerView.Adapter<RvUssdAdapter.UssdVh>() {

    private val expansion = ExpansionLayoutCollection()

    init {
        expansion.openOnlyOne(true)
    }

    inner class UssdVh(var itemBinding: ItemRvUssdBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(ussdM: UssdEntity, position: Int) {
            itemBinding.apply {
                tvUssdCode.text = ussdM.code
                tvDesc.text = ussdM.name

                btnConnect.setOnClickListener { listener.onBtnClick(ussdM.code ?: "") }
            }
        }
    }

    interface UssdOnClickListener {
        fun onBtnClick(code: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UssdVh {
        return UssdVh(ItemRvUssdBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UssdVh, position: Int) {
        holder.onBind(list[position], position)
        expansion.add(holder.itemBinding.expansionLayout)
        holder.itemBinding.expansionLayout.collapse(false)
    }

    override fun getItemCount() = list.size

}