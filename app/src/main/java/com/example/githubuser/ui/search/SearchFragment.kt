package com.example.githubuser.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.User
import com.example.githubuser.databinding.FragmentSearchBinding
import com.example.githubuser.ui.UserAdapter
import com.example.githubuser.ui.detail.DetailUserActivity

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserAdapter

    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setUpAction()

    }

    private fun setUpAdapter() {
        userAdapter = UserAdapter()
    }

    private fun setUpAction() {
        binding.apply {
            rvSearch.layoutManager = LinearLayoutManager(requireContext())
            rvSearch.setHasFixedSize(true)
            rvSearch.adapter = userAdapter

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
        }

        setUpViewModel()
    }

    private fun setUpViewModel() {
        binding.apply {
            showLoading(false)
            searchViewModel.isLoading.observe(requireActivity()) {
                showLoading(it)
            }

            btnSearch.setOnClickListener {
                searchUser()
            }

            etQuery.setOnKeyListener {v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        searchViewModel.getSearchUser().observe(requireActivity()) {
            if (it != null) {
                userAdapter.setList(it)
                showLoading(false)
            }
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

    private fun searchUser() {
        binding.apply {
            val query = etQuery.text.toString()
            rvSearch.adapter = userAdapter
            userAdapter.clearList()
            showLoading(true)
            searchViewModel.setSearchUsers(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}