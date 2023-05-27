package com.example.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.local.FavoriteEntity
import com.example.githubuser.data.remote.User
import com.example.githubuser.databinding.FragmentFavoriteBinding
import com.example.githubuser.ui.UserAdapter
import com.example.githubuser.ui.detail.DetailUserActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setUpAction()

    }

    private fun setUpAction() {
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(requireContext(), DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_HTML, data.htmlUrl)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvSearch.setHasFixedSize(true)
            rvSearch.layoutManager = LinearLayoutManager(requireContext())
            rvSearch.adapter = userAdapter
        }

        setUpViewModel()
    }

    private fun setUpViewModel() {
        favoriteViewModel.getFavoriteUser()?.observe(requireActivity()) {
            if (it!=null) {
                val list = mapList(it)
                userAdapter.setList(list)
            }
        }
    }

    private fun setUpAdapter() {
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()
    }

    private fun mapList(users: List<FavoriteEntity>): ArrayList<User> {
        val listUser = ArrayList<User>()
        for (user in users) {
            val userMapped = User (
                user.avatarUrl,
                user.htmlUrl,
                user.id,
                user.login
            )
            listUser.add(userMapped)
        }
        return listUser
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}