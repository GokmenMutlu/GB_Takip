package com.gokmenmutlu.knightrisegoldbartakip.view

import android.R
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
import com.gokmenmutlu.knightrisegoldbartakip.adapter.RiseOnlineGBAdapter
import com.gokmenmutlu.knightrisegoldbartakip.databinding.FragmentRiseOnlineBinding
import com.gokmenmutlu.knightrisegoldbartakip.model.RiseOnlinePriceData
import com.gokmenmutlu.knightrisegoldbartakip.viewModels.RiseOnlineViewModel


class RiseOnlineFragment : Fragment() {

    private var _binding: FragmentRiseOnlineBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RiseOnlineViewModel
    private lateinit var adapter: RiseOnlineGBAdapter
    private var fullPriceList: List<RiseOnlinePriceData> = listOf()

    private val servers = listOf("Galia", "Mantis", "Aarvad")
    private var selectedServer = "Galia"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRiseOnlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RiseOnlineViewModel::class.java]

        setupRecyclerView()
        setupSpinner()
        setupSwipeToRefresh()

        observeViewModel()
        viewModel.getRiseOnlinePriceData()
    }

    private fun setupRecyclerView() {
        adapter = RiseOnlineGBAdapter(fullPriceList, selectedServer)
        binding.recyclerViewRiseOnline.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRiseOnline.adapter = adapter
    }

    private fun setupSpinner() {
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, servers)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerServerRiseOnline.adapter = spinnerAdapter

        binding.spinnerServerRiseOnline.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedServer = servers[position]
                filterByServer(selectedServer)
                adapter.updateSelectedServer(selectedServer)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayoutRiseOnline.setOnRefreshListener {
            binding.swipeRefreshLayoutRiseOnline.isRefreshing = false
            viewModel.getRiseOnlinePriceData()
        }
    }

    private fun observeViewModel() {
        viewModel.riseOnlinePriceData.observe(viewLifecycleOwner, Observer { priceList ->
            if (priceList != null) {
                fullPriceList = priceList
                filterByServer(selectedServer)
            }
        })
    }

    private fun filterByServer(serverName: String) {
        val filteredList = fullPriceList.filter { it.serverName == serverName }
        adapter.updateData(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
