package com.example.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var sectionPagerAdapter: SectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_user)

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        val id = intent.getIntExtra(EXTRA_ID, 0)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        bundle.putInt(EXTRA_ID, id)

        showLoading(true)

        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.setDetailUser(username)
        viewModel.getDetailUser().observe(this) {
            if (it != null) {
                binding.apply {
                    tvUsername.text = it.login
                    tvName.text = it.name
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivProfile)
                }
                showLoading(false)
            }
        }

        sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        binding.apply {
            vpFollow.adapter = sectionPagerAdapter

            TabLayoutMediator(tlFollow, vpFollow) { tabs, position ->
                tabs.text = tabTitle[position]
            }.attach()
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

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_AVATAR = "EXTRA_AVATAR"
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_HTML  = "EXTRA_HTML"

        private val tabTitle = listOf(
            "Followers",
            "Following"
        )
    }
}