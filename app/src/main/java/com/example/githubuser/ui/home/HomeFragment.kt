package com.example.githubuser.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.User
import com.example.githubuser.data.remote.UserResponse
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.ui.UserAdapter
import com.example.githubuser.ui.detail.DetailUserActivity
import com.example.githubuser.ui.setting.SettingViewModelFactory
import com.example.githubuser.ui.setting.SettingsPreferences

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userAdapter: UserAdapter
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "setting")

    private var etQuery: String = "andre"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setUpAction()

    }

    private fun setUpAdapter() {
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()
    }

    private fun setUpAction() {
        setUpAdapter()
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(requireContext(), DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_HTML, data.htmlUrl)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvSearchUser.layoutManager = LinearLayoutManager(requireContext())
            rvSearchUser.setHasFixedSize(true)
            rvSearchUser.adapter = userAdapter
        }

        setUpViewModel()
    }

    private fun setUpViewModel() {
        val pref = SettingsPreferences.getInstance(requireContext().dataStore)
        homeViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[HomeViewModel::class.java]

        homeViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        searchUser()

        homeViewModel.getSearchUser().observe(requireActivity()) {
            if (it != null) {
                userAdapter.setList(it)
                showLoading(false)
            }
        }

        homeViewModel.getThemeSetting().observe(viewLifecycleOwner) {isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
    }

    private fun searchUser() {
        binding.apply {
            rvSearchUser.adapter = userAdapter
            userAdapter.clearList()
            showLoading(true)
            homeViewModel.setSearchUsers(etQuery)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}