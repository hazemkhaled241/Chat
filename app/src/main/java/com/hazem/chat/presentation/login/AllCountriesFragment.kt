package com.hazem.chat.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hazem.chat.R
import com.hazem.chat.data.local.Country
import com.hazem.chat.databinding.FragmentAllCountriesBinding
import com.hazem.chat.presentation.login.adapter.CountriesAdapter
import com.hazem.chat.presentation.login.adapter.OnItemClick
import com.hazem.chat.presentation.login.viewmodel.CountriesViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllCountriesFragment : Fragment(), OnItemClick<Country> {
    private var _binding: FragmentAllCountriesBinding? = null
    private val binding get() = _binding!!
    private var allCountries:ArrayList<Country> = arrayListOf()
    private val countriesAdapter: CountriesAdapter by lazy { CountriesAdapter() }
    private val countriesViewModel: CountriesViewModel by viewModels()
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllCountriesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCountries.adapter = countriesAdapter
        countriesAdapter.onItemClicked = this
        countriesViewModel.fetchAllCountries()
        observe()
        searchView()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            countriesViewModel.countryState.collect {
                if (it != null) {
                    allCountries=it as ArrayList<Country>
                    countriesAdapter.updateList(allCountries)
                }


            }

        }
    }

    private fun searchView() {
        try {
            binding.toolbar.inflateMenu(R.menu.search_item)
            val searchItem: MenuItem? = binding.toolbar.menu.findItem(R.id.item_search)
             searchView = searchItem?.actionView as SearchView
            searchView.apply {
                maxWidth=Integer.MAX_VALUE
                queryHint=getString(R.string.search)
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
               if(query!=null){
                   searchInCountries(query)}

                    return true
                }

                override fun onQueryTextChange(text: String?): Boolean {
                    if(text!=null)
                        searchInCountries(text)

                    return true
                }

            })
          //  searchView.isSubmitButtonEnabled=true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun searchInCountries(query:String){
        val result = allCountries.filter {
            it.name.contains(query, true)
        }
        if(result.isNotEmpty())
            countriesAdapter.updateList(result as ArrayList<Country>)



    }


    override fun onItemClicked(item: Country, position: Int) {
        val action = AllCountriesFragmentDirections.actionAllCountriesFragmentToLoginFragment2(item)
        findNavController().navigate(action)
    }


}