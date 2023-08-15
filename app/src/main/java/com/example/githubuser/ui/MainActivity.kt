package com.example.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.User
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.detail.DetailUserActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter

    private var etQuery: String = "andre"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }

        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        binding.apply {
            rvSearchUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvSearchUser.setHasFixedSize(true)
            rvSearchUser.adapter = userAdapter
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        searchUser()

        viewModel.getSearchUser().observe(this) {
            if (it != null) {
                userAdapter.updateList(it)
                showLoading(false)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Masukkan Username"
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                etQuery = query
                searchUser()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })
        return true
    }

    private fun searchUser() {
        binding.apply {
            rvSearchUser.adapter = userAdapter
            showLoading(true)
            viewModel.setSearchUsers(etQuery)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}