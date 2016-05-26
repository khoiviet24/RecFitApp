package khoiviet24.recfit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by khoiviet24 on 3/7/2016.
 */
public class DownloadTask extends AsyncTask<String, Void, Boolean> {

    ImageView v;
    String url;
    Bitmap bm;
    protected RoundImage mRoundedImage;

    public DownloadTask(ImageView v) {
        this.v = v;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        url = params[0];
        bm = loadBitmap(url);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result){
        super.onPostExecute(result);
        mRoundedImage = new RoundImage(cropToSquare(bm));
        //v.setImageBitmap(bm);
        v.setImageDrawable(mRoundedImage);
    }

    public static Bitmap loadBitmap(String url){
        try{
            URL newurl = new URL(url);
            Bitmap b = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            return b;
        }catch(MalformedURLException e){
            e.printStackTrace();;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }
}
