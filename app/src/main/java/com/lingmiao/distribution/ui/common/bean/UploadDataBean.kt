package com.lingmiao.distribution.ui.common.bean

import com.fisheagle.mkt.business.photo.getImagePath
import com.james.common.utils.exts.checkNotBlank
import com.luck.picture.lib.entity.LocalMedia

/**
Create Date : 2021/1/811:24 AM
Auther      : Fox
Desc        :
 **/
class UploadDataBean (
    var id: String? = "",
    var url: String? = "",
    var path: String? = ""
) {
    companion object {

        const val MAX_SIZE_3 = 3;

        const val COLUMN_COUNT_3 = 3;

        const val MAX_SIZE_5 = 5;

        const val COLUMN_COUNT_5 = 5;

        const val MAX_ONCE_PICKER_COUNT = 1;

        fun convert(localMedia: LocalMedia): UploadDataBean {
            val imagePath = localMedia.compressPath.checkNotBlank(localMedia.getImagePath())
            return UploadDataBean(null, imagePath, imagePath)
        }
    }


    fun convert2LocalMedia() : LocalMedia {
        return LocalMedia().apply {
            path = url
            originalPath = path
            androidQToPath = path
        }
    }
}
