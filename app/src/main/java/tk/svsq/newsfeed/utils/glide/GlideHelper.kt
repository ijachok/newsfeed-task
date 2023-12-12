package tk.svsq.newsfeed.utils.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import tk.svsq.newsfeed.R

object GlideHelper {
    fun loadImage(context: Context, imageUrl: String?, destination: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_error)
            .into(destination)
    }
}