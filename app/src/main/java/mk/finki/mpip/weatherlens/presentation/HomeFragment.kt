package mk.finki.mpip.weatherlens.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mk.finki.mpip.weatherlens.R
import mk.finki.mpip.weatherlens.databinding.FragmentHomeBinding
import mk.finki.mpip.weatherlens.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

  companion object {
    fun createInstance() = HomeFragment()
  }

  private var binding: FragmentHomeBinding? = null
  private var viewModel: HomeViewModel? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentHomeBinding.inflate(layoutInflater)
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}