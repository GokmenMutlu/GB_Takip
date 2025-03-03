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
import com.gokmenmutlu.knightrisegoldbartakip.databinding.ItemKnightOnlineRowBinding
import com.gokmenmutlu.knightrisegoldbartakip.model.KnightOnlinePriceData
import com.gokmenmutlu.knightrisegoldbartakip.utils.Utils
import java.util.Locale


class KnightOnlineGBAdapter(
    var gbPriceList: List<KnightOnlinePriceData>,
    private val selectedServer: String) : RecyclerView.Adapter<KnightOnlineGBAdapter.GBPriceViewHolder>() {

    class GBPriceViewHolder(val binding : ItemKnightOnlineRowBinding) : RecyclerView.ViewHolder(binding.root)

    private var currentSelectedServer = selectedServer


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GBPriceViewHolder {
        val binding = ItemKnightOnlineRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GBPriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GBPriceViewHolder, position: Int) {
        val knightOnlinePriceData = gbPriceList[position]
        var sellPrice = knightOnlinePriceData.sellPrice
        var buyPrice = knightOnlinePriceData.buyPrice

        Glide.with(holder.itemView.context)
            .load(knightOnlinePriceData.logoUrl)
            .placeholder(R.drawable.logo_gbtakip)
            .error(R.drawable.logo_gbtakip)
            .into(holder.binding.imgSiteLogo)
        // holder.binding.imgSiteLogo.setImageResource(Utils.getSiteLogo(knightOnlinePriceData.site))

        // Sunucu adı seçilen server'a eşitse fiyat bilgilerini ekrana yazıyoruz
        if (knightOnlinePriceData.serverName == currentSelectedServer) {
            // Satış ve alış fiyatlarını yazıyoruz

            sellPrice = convertPrice(sellPrice)
            buyPrice = convertPrice(buyPrice)

            holder.binding.txtSellPrice.text = sellPrice?.toString() ?: "Yok"
            holder.binding.txtBuyPrice.text = buyPrice?.toString() ?: "Yok"

            // En yüksek alış fiyatı kontrolü
            if (buyPrice == getMaxBuyPrice()) {
                holder.binding.txtBuyPrice.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_green)
            } else {
                holder.binding.txtBuyPrice.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_gray)
            }

            // En düşük satış fiyatı kontrolü
            if (sellPrice == getMinSellPrice()) {
                holder.binding.txtSellPrice.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_red)
            } else {
                holder.binding.txtSellPrice.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_gray)
            }
            knightOnlinePriceData.lastUpdated?.let {
                // TimeStamp varsa tarihi formatla ve göster
                val formattedDate = Utils.convertTimestampToDate(it.seconds, it.nanoseconds)
                holder.binding.txtLastUpdated.text = "Last Updated: $formattedDate"
            }

            // Tıklama Olayı Ekliyoruz
            holder.itemView.setOnClickListener {
                val siteName = knightOnlinePriceData.site
                val siteUrl = knightOnlinePriceData.siteUrl

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



    override fun getItemCount(): Int {
        return gbPriceList.size
    }

    // En düşük satış fiyatını hesaplar
    private fun getMinSellPrice(): Double {
        val filteredList = gbPriceList.filter { it.serverName == currentSelectedServer }
        return filteredList.minOfOrNull { convertPrice(it.sellPrice) ?: Double.MAX_VALUE } ?: Double.MAX_VALUE
    }
    // En yüksek alış fiyatını hesaplar
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


    // Adapter'daki veri setini güncelleme fonksiyonu
    fun updateData(newData: List<KnightOnlinePriceData>) {
        val diffCallback = KnightOnlinePriceDiffCallback(gbPriceList, newData) // DiffCallback oluştur
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        gbPriceList = newData // Yeni listeyi adapter'a ata

        diffResult.dispatchUpdatesTo(this) // RecyclerView'ı güncelle
    }

    fun updateSelectedServer(newSelectedServer: String) {
        currentSelectedServer = newSelectedServer
        notifyDataSetChanged() // Verileri güncelle
    }

    class KnightOnlinePriceDiffCallback(
        private val oldList: List<KnightOnlinePriceData>,
        private val newList: List<KnightOnlinePriceData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.site == newItem.site && oldItem.serverName == newItem.serverName // Aynı site ve sunucuya ait mi?
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem // İçerik aynı mı? (data class'lar için yeterli)
        }
    }


}
