package ca.philrousse.android02.labo1.adapter


import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat


@BindingAdapter("currencyAmount")
fun setCurrencyAmount(textView: TextView, amount: Double) {
    textView.text = NumberFormat.getCurrencyInstance().format(amount)
}