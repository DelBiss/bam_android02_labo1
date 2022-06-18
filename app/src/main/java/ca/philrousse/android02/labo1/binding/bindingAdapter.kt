package ca.philrousse.android02.labo1.binding

import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.util.Currency
import android.icu.util.ULocale
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat


@BindingAdapter("currencyAmount")
fun setCurrencyAmount(textView: TextView, amount: Double) {
    Log.d("setCurrencyAmount",String.format("%.2f$", amount))
    //textView.text = String.format("%.2f $", amount)
    textView.text = NumberFormat.getCurrencyInstance().format(amount)
    /*textView.text =   NumberFormatter.with()
        .unit(Currency.getInstance("CAD"))
        .precision(Precision.currency(Currency.CurrencyUsage.STANDARD))
        .locale(ULocale.CANADA_FRENCH)
        .format(amount)
        .toString()*/
}