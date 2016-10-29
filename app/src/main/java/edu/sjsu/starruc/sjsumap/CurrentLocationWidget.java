
/**
 * Created by StarRUC on 10/27/16.
 */

package edu.sjsu.starruc.sjsumap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class CurrentLocationWidget extends View {
    Paint paint = new Paint();

    public CurrentLocationWidget(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cur_location);
        Matrix matrix = new Matrix();
        matrix.postScale(0.2f, 0.2f);
        Bitmap dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);
        canvas.drawBitmap(dstbmp, 0, 0, null);

    }

}
