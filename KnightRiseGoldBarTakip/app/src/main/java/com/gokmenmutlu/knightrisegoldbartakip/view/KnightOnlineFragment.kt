package com.gokmenmutlu.knightrisegoldbartakip.view

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gokmenmutlu.knightrisegoldbartakip.adapter.KnightOnlineGBAdapter
import com.gokmenmutlu.knightrisegoldbartakip.databinding.FragmentKnightOnlineBinding
import com.gokmenmutlu.knightrisegoldbartakip.model.KnightOnlinePriceData
import com.gokmenmutlu.knightrisegoldbartakip.viewModels.KnightOnlineViewModel


class KnightOnlineFragment : Fragment() {

    private var _binding: FragmentKnightOnlineBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: KnightOnlineViewModel

    private lateinit var adapter: KnightOnlineGBAdapter
    private var fullPriceList: List<KnightOnlinePriceData> = listOf()

    private val servers = listOf("Zero", "Felis", "Agartha", "Pandora", "Dryads", "Destan", "Minark", "Oreads", "Zion")
    private var selectedServer = "Zero"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentKnightOnlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[KnightOnlineViewModel::class.java]

        setupRecyclerView()
        setupSpinner()
        setupSwipeToRefresh()


        // Verileri Firestore'dan çekme
        observeViewModel()
        viewModel.getKnightOnlinePriceData()

    }

    private fun setupRecyclerView() {

        adapter = KnightOnlineGBAdapter(fullPriceList,selectedServer)
        binding.recyclerViewKnightOnline.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewKnightOnline.adapter = adapter
    }

    private fun setupSpinner() {
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, servers)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerServer.adapter = spinnerAdapter

        binding.spinnerServer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedServer = servers[position]
                filterByServer(selectedServer)
                println("KnightOnlineFragment Seçilen Sunucu: $selectedServer")
                adapter.updateSelectedServer(selectedServer)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.getKnightOnlinePriceData() // Swipe ile veriyi tekrar çek
        }
    }

    // Firestore'dan veriyi çekme fonksiyonu
    // ViewModel'den gelen verileri gözlemleyip RecyclerView'u güncelle
    private fun observeViewModel() {
        viewModel.knightOnlinePriceData.observe(viewLifecycleOwner, Observer { priceList ->
            if (priceList != null) {
         //       println("KnightOnlineFragment Veriler çekildi: $priceList") // Burada verilerin doğru çekilip çekilmediğini görebilirsiniz.
                fullPriceList = priceList
                filterByServer(selectedServer) // Sunucu seçimine göre filtreleme
            } else {
                println("KnightOnlineFragment Veri gelmedi!") // Veri gelmediği durumu logluyoruz.
            }
        })
    }

    // Seçilen sunucuya göre fiyatları filtreleyip güncelleme
    private fun filterByServer(serverName: String) {

        val filteredList = fullPriceList.filter { it.serverName == serverName }
        println("KnightOnlineFragment Filtrelenmiş Liste: $filteredList")
        adapter.updateData(filteredList) // Adapter'ı güncelle
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




