package android.example.com.userleaderboard.util

import android.content.res.Resources
import android.graphics.*

class BitmapExtension {

    companion object
    {
        private fun getCircularBitmap(srcBitmap: Bitmap?): Bitmap {
            val squareBitmapWidth = Integer.min(srcBitmap!!.width, srcBitmap.height)
            val dstBitmap = Bitmap.createBitmap(
                squareBitmapWidth,
                squareBitmapWidth,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(dstBitmap)
            val paint = Paint()
            paint.isAntiAlias = true
            val rect = Rect(0, 0, squareBitmapWidth, squareBitmapWidth)
            val rectF = RectF(rect)
            canvas.drawOval(rectF, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            val left = ((squareBitmapWidth - srcBitmap.width) / 2).toFloat()
            val top = ((squareBitmapWidth - srcBitmap.height) / 2).toFloat()
            canvas.drawBitmap(srcBitmap, left, top, paint)
            srcBitmap.recycle()
            return dstBitmap

        }

        fun getImageBitmap(id: Int, resources: Resources): Bitmap {
            val bitmap = BitmapFactory.decodeResource(resources, id)
            return getCircularBitmap(bitmap)
        }
    }



}