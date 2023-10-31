package com.example.geetsunam.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.geetsunam.R

class BindingAdapter {
    companion object {
//        @BindingAdapter("onRecipeClickedListener")
//        @JvmStatic
//        fun onRecipeClickedListener(recipeCard: ConstraintLayout, id: Int) {
//            recipeCard.setOnClickListener {
//                try {
//                    val action = RecipesFragmentDirections
//                        .actionRecipesFragmentToRecipeDetailActivity(id)
//                    recipeCard.findNavController()
//                        .navigate(action)
//                } catch (ex: Exception) {
//                    Log.d("setOnClickListener", "${ex.message}")
//                }
//            }
//        }

        @BindingAdapter("setText")
        @JvmStatic
        fun setText(textView: TextView, value: String) {
            textView.text = value
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun setNetworkImage(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                placeholder(R.drawable.placeholder)
                error(R.drawable.error)
            }
        }
    }
}