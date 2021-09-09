package com.lingmiao.distribution.util

import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation

/**
Create Date : 2021/9/911:58 下午
Auther      : Fox
Desc        :
 **/

fun showYTranslateAnim(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 1f,
        Animation.RELATIVE_TO_PARENT, 0f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}

fun hideYTranslateAnim(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 1f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}

fun showYTranslateAnimOfTopToBottom(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, -1f,
        Animation.RELATIVE_TO_PARENT, 0f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}

fun hideYTranslateAnimOfBottomToTop(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, -1f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}
