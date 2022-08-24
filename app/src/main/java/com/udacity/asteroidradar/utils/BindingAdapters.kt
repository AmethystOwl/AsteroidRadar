package com.udacity.asteroidradar.utils

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.model.PictureOfDay

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("progressBarState")
fun bindProgressBarState(progressBar: ProgressBar, itemsCount: Int) {
    if (itemsCount == 0) {
        progressBar.visibility = View.VISIBLE
    } else {
        progressBar.visibility = View.GONE
    }
}

@BindingAdapter("apodImage")
fun bindApodImageView(imageView: ImageView, apod: PictureOfDay?) {
    apod?.let {
        if (apod.mediaType == "image") {
            Picasso.with(imageView.context).load(apod.url).into(imageView)
        }
    }
}

@BindingAdapter("potentiallyHazardousContentDescription")
fun bindPotentiallyHazardousContentDescription(
    imageView: ImageView,
    potentiallyHazardous: Boolean
) {
    when (potentiallyHazardous) {
        true -> {
            imageView.contentDescription = "A potentially hazardous asteroid"
        }
        false -> {
            imageView.contentDescription = "Not a hazardous asteroid"
        }
    }
}