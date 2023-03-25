package mk.finki.mpip.weatherlens.viewmodels

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
  this.postValue(this.value)
}