package com.vfs.fingerprint.scanbotdocumentscan

import android.content.Context
import android.util.Log
import io.scanbot.sdk.ScanbotSDK
import io.scanbot.sdk.persistence.Page
import io.scanbot.sdk.process.ImageFilterType

open class PageRepository {

    companion object {

        private val pages = mutableListOf<Page>()

        fun getPages(): List<Page> = pages

        fun removePage(context: Context, pageToRemove: Page) {
            ScanbotSDK(context).pageFileStorage().remove(pageToRemove.pageId)
            pages.remove(pageToRemove)
        }

        fun addPages(newPages: List<Page>) {
            Log.d("tag","called ak check size:"+newPages)
            pages.addAll(newPages)
        }

        fun clearPages(context: Context) {
            ScanbotSDK(context).pageFileStorage().removeAll()

            pages.clear()
        }

        fun applyFilter(context: Context, imageFilterType: ImageFilterType) {
            pages.forEach {
                ScanbotSDK(context).pageProcessor().applyFilter(it, imageFilterType)
            }
            val list = pages.map {
                Page(pageId = it.pageId,
                        polygon = it.polygon,
                        detectionStatus = it.detectionStatus,
                        filter = imageFilterType)
            }.toMutableList()

            pages.clear()
            pages.addAll(list)
        }

        fun applyFilter(context: Context, imageFilterType: ImageFilterType, page: Page): Page {
            pages.forEach {
                if (it.pageId == page.pageId) {
                    ScanbotSDK(context).pageProcessor().applyFilter(it, imageFilterType)
                    ScanbotSDK(context).pageProcessor().generateFilteredPreview(it, imageFilterType)
                }
            }
            val result = Page(pageId = page.pageId,
                    polygon = page.polygon,
                    detectionStatus = page.detectionStatus,
                    filter = imageFilterType)
            val list = pages.map {
                if (it.pageId == page.pageId) {
                    result
                } else {
                    it
                }
            }.toMutableList()

            pages.clear()
            pages.addAll(list)
            return result
        }

        fun updatePage(page: Page): Page {
            pages.removeAll {
                it.pageId == page.pageId
            }
            pages.add(page)
            return page
        }

        fun addPage(page: Page) {
            pages.add(page)
        }
    }

}