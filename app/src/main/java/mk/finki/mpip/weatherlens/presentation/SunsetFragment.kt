package mk.finki.mpip.weatherlens.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mk.finki.mpip.weatherlens.databinding.FragmentSunsetBinding
import mk.finki.mpip.weatherlens.viewmodels.SunsetViewModel

class SunsetFragment : Fragment() {

  companion object {
    fun createInstance() = SunsetFragment()
  }

  private var binding: FragmentSunsetBinding? = null
  private var viewModel: SunsetViewModel? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSunsetBinding.inflate(layoutInflater)
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProvider(this)[SunsetViewModel::class.java]
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}