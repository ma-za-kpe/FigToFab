package com.maku.figmaparser.service

import com.maku.figmaparser.model.finali.FigmaFile
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FigmaService {

    @GET("/v1/files/{key}")
    suspend fun getFile(
        @Path("key") key: String,
        @Query("version") version: String? = null,
        @Query("ids") ids: String? = null,
        @Query("depth") depth: Int? = null,
        @Query("geometry") geometry: String? = null,
        @Query("plugin_data") pluginData: String? = null,
        @Query("branch_data") branchData: Boolean? = null
    ): Response<FigmaFile>

//    @GET("/v1/files/{key}/nodes")
//    fun getFileNodes(
//        @Path("key") key: String,
//        @Query("ids") ids: String,
//        @Query("depth") depth: Int? = null,
//        @Query("geometry") geometry: String? = null,
//        @Query("version") version: String? = null,
//        @Query("plugin_data") pluginData: String? = null
//    ): Call<FigmaFileNodes>
//
//    @GET("/v1/images/{key}")
//    fun getImage(
//        @Path("key") key: String,
//        @Query("ids") ids: String,
//        @Query("scale") scale: Double? = null,
//        @Query("format") format: String? = null,
//        @Query("svg_outline_text") svgOutlineText: Boolean? = null,
//        @Query("svg_include_id") svgIncludeId: Boolean? = null,
//        @Query("svg_include_node_id") svgIncludeNodeId: Boolean? = null,
//        @Query("svg_simplify_stroke") svgSimplifyStroke: Boolean? = null,
//        @Query("contents_only") contentsOnly: Boolean? = null,
//        @Query("use_absolute_bounds") useAbsoluteBounds: Boolean? = null,
//        @Query("version") version: String? = null
//    ): Call<ResponseBody>
//
//    @GET("/v1/files/{key}/images")
//    fun getImageFills(
//        @Path("key") key: String
//    ): Call<ImageFills>
}
