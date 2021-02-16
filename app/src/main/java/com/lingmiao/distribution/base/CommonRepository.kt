package com.fisheagle.mkt.base

import com.blankj.utilcode.util.Utils
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.fisheagle.mkt.business.common.bean.FileResponse
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.net.Fetch
import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object CommonRepository {

    suspend fun uploadImage(file : File, isImage:Boolean = true) : HiResponse<DataVO<UploadDataBean>> {
        return this.uploadImage(file, isImage, "uploadFile");
    }

    suspend fun uploadImage(file : File, isImage:Boolean = true, filename: String = "uploadFile") : HiResponse<DataVO<UploadDataBean>> {
        var file = file
        var imageType = "image/png"
        val videoType = "video/mp4"
        if (file.name.endsWith("jpg")) {
            imageType = "image/jpg"
        } else if (file.name.endsWith("jpeg")) {
            imageType = "image/jpeg"
        }
        if(isImage) file = Compressor.compress(Utils.getApp(), file)
        val currentType = if(isImage) imageType else videoType
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse(currentType),
            file
        )

        val multipartBody =
            MultipartBody.Part.createFormData(
                filename,
                file.name,
                requestFile
            )
        return Fetch.apiService().uploadFileOfCommon(multipartBody).awaitHiResponse()
    }
}