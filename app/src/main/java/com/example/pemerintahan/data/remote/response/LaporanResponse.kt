package com.example.pemerintahan.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LaporanResponse(

	@field:SerializedName("listlaporan")
	val listLaporanSaya: List<ListLaporanSayaItem> = emptyList(),

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class ListLaporanSayaItem(

	@field:SerializedName("gambar")
	val gambar: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("tempat")
	val tempat: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("prioritas")
	val prioritas: String,
	): Parcelable
