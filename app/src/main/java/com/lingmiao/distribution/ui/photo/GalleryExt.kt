package com.lingmiao.distribution.ui.photo

import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import com.luck.picture.lib.entity.LocalMedia

/**
Create Date : 2021/1/84:07 PM
Auther      : Fox
Desc        :
 **/
fun convert2GalleryVO(list: List<LocalMedia>): List<UploadDataBean> {
    val galleryList = mutableListOf<UploadDataBean>()
    list.forEach {
        galleryList.add(UploadDataBean.convert(it))
    }
    return galleryList
}