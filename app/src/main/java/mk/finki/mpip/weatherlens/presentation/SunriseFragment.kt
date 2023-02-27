package mk.finki.mpip.weatherlens.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mk.finki.mpip.weatherlens.databinding.FragmentSunriseBinding
import mk.finki.mpip.weatherlens.viewmodels.SunriseViewModel

class SunriseFragment : Fragment() {

  companion object {
    fun createInstance() = SunriseFragment()
  }

  private var binding: FragmentSunriseBinding? = null
  private var viewModel: SunriseViewModel? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSunriseBinding.inflate(layoutInflater)
    return binding?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProvider(this)[SunriseViewModel::class.java]
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}