package ca.philrousse.android02.labo1.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("currencyAmount")
fun setCurrencyAmount(textView: TextView, amount: Double) {
    textView.text = String.format("%.2f$", amount)

       /* NumberFormatter.with()
        .unit(Currency.getInstance("CAD"))
        .precision(Precision.currency(Currency.CurrencyUsage.STANDARD))
        .locale(ULocale.CANADA_FRENCH)
        .format(amount)
        .toString()*/
}