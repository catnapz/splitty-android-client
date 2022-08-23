package dev.anshshukla.splitty

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class SplittyGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.apply {
            builder.setDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
        }
    }

}