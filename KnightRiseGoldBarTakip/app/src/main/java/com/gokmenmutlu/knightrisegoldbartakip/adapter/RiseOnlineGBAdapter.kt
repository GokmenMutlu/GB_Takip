package com.gokmenmutlu.knightrisegoldbartakip.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gokmenmutlu.knightrisegoldbartakip.R
import com.gokmenmutlu.knightrisegoldbartakip.databinding.ItemRiseOnlineRowBinding
import com.gokmenmutlu.knightrisegoldbartakip.model.RiseOnlinePriceData
import com.gokmenmutlu.knightrisegoldbartakip.utils.Utils
import java.util.Locale

class RiseOnlineGBAdapter(
    private var gbPriceList: List<RiseOnlinePriceData>,
    private val selectedServer: String
) : RecyclerView.Adapter<RiseOnlineGBAdapter.GBPriceViewHolder>() {

    class GBPriceViewHolder(val binding: ItemRiseOnlineRowBinding) : RecyclerView.ViewHolder(binding.root)

    private var currentSelectedServer = selectedServer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GBPriceViewHolder {
        val binding = ItemRiseOnlineRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GBPriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GBPriceViewHolder, position: Int) {
        val riseOnlinePriceData = gbPriceList[position]
        var sellPrice = riseOnlinePriceData.sellPrice
        var buyPrice = riseOnlinePriceData.buyPrice

        Glide.with(holder.itemView.context)
            .load(riseOnlinePriceData.logoUrl)
            .placeholder(R.drawable.logo_gbtakip)
            .error(R.drawable.logo_gbtakip)
            .into(holder.binding.imgSiteLogo)
        //holder.binding.imgSiteLogo.setImageResource(Utils.getSiteLogo(riseOnlinePriceData.site))

        if (riseOnlinePriceData.serverName == currentSelectedServer) {
            sellPrice = convertPrice(sellPrice)
            buyPrice = convertPrice(buyPrice)

            holder.binding.txtSellPrice.text = sellPrice?.toString() ?: "Yok"
            holder.binding.txtBuyPrice.text = buyPrice?.toString() ?: "Yok"

            if (buyPrice == getMaxBuyPrice()) {
                holder.binding.txtBuyPrice.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_green)
            } else {
                holder.binding.txtBuyPrice.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_gray)
            }

            if (sellPrice == getMinSellPrice()) {
                holder.binding.txtSellPrice.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_red)
            } else {
                holder.binding.txtSellPrice.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_gray)
            }
            riseOnlinePriceData.lastUpdated?.let {
                // TimeStamp varsa tarihi formatla ve göster
                val formattedDate = Utils.convertTimestampToDate(it.seconds, it.nanoseconds)
                holder.binding.txtLastUpdated.text = "Last Updated: $formattedDate"
            }

            // Tıklama Olayı Ekliyoruz
            holder.itemView.setOnClickListener {
                val siteName = riseOnlinePriceData.site
                val siteUrl = riseOnlinePriceData.siteUrl

                siteUrl?.let {
                    // AlertDialog ile onay sorusu
                    AlertDialog.Builder(holder.itemView.context)
                        .setTitle("Siteye Git")
                        .setMessage("$siteName sitesine gitmek istiyor musunuz?")
                        .setPositiveButton("Evet") { _, _ ->
                            // Eğer "Evet" seçilirse siteyi aç
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            holder.itemView.context.startActivity(intent)
                        }
                        .setNegativeButton("Hayır", null)
                        .show()
                }

            }

        }
    }

    override fun getItemCount(): Int = gbPriceList.size

    private fun getMinSellPrice(): Double {
        val filteredList = gbPriceList.filter { it.serverName == currentSelectedServer }
        return filteredList.minOfOrNull { convertPrice(it.sellPrice) ?: Double.MAX_VALUE } ?: Double.MAX_VALUE
    }

    private fun getMaxBuyPrice(): Double {
        val filteredList = gbPriceList.filter { it.serverName == currentSelectedServer }
        return filteredList.maxOfOrNull { convertPrice(it.buyPrice) ?: Double.MIN_VALUE } ?: Double.MIN_VALUE
    }

    private fun convertPrice(price: Double?): Double? {
        if (price == null) return null

        val convertedPrice = when {
            price < 10.0 -> price * 100
            price < 100.0 -> price * 10
            else -> price
        }

        return convertedPrice.let { String.format(Locale.US, "%.2f", it).toDouble() }
    }



    fun updateData(newData: List<RiseOnlinePriceData>) {
        val diffCallback = RiseOnlinePriceDiffCallback(gbPriceList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        gbPriceList = newData

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateSelectedServer(newSelectedServer: String) {
        currentSelectedServer = newSelectedServer
        notifyDataSetChanged()
    }



    class RiseOnlinePriceDiffCallback(
        private val oldList: List<RiseOnlinePriceData>,
        private val newList: List<RiseOnlinePriceData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.site == newItem.site && oldItem.serverName == newItem.serverName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}
