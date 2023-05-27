package com.example.githubuser.ui.setting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SettingsViewModel
    private val Context.dataStore : DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "setting")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingsPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingsViewModel::class.java]

        viewModel.getThemeSetting().observe(viewLifecycleOwner) {isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}