package ca.philrousse.android02.labo1.data

import ca.philrousse.android02.labo1.R

data class TotalInventaire(val total: Double?, private val isCategory:Boolean = false){
    val uiLabelRessourceId:Int
        get() {
            return if (isCategory){
                R.string.prompt_categ
            } else {
                R.string.prompt_inv_total
            }
        }
}