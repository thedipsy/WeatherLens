package mk.finki.mpip.weatherlens.presentation.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import mk.finki.mpip.weatherlens.R
import mk.finki.mpip.weatherlens.presentation.Home.HomeFragment

abstract class BaseActivity : AppCompatActivity() {

  fun navigateToFragment(fragment: Fragment, tag: String = fragment::class.java.simpleName) {
    supportFragmentManager.findFragmentByTag(tag)?.let {
      replaceFragment(it, tag)
    } ?: addFragment(fragment, tag)
  }

  override fun onBackPressed() {
    navigateToHomeOrFinish()
  }

  private fun navigateToHomeOrFinish() {
    if (supportFragmentManager.backStackEntryCount > 0) {
      supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
      replaceFragment(HomeFragment.createInstance())
    } else {
      finish()
    }
  }

  private fun replaceFragment(fragment: Fragment, tag: String = fragment::class.java.simpleName) =
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragmentContainer, fragment, tag)
      .commit()

  private fun addFragment(fragment: Fragment, tag: String = fragment::class.java.simpleName) =
    supportFragmentManager
      .beginTransaction()
      .add(R.id.fragmentContainer, fragment, tag)
      .addToBackStack(tag)
      .commit()

}

