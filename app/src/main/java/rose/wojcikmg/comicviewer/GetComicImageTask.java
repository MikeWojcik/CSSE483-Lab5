package rose.wojcikmg.comicviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class GetComicImageTask extends AsyncTask<String, Void, Comic> {
    private GetComicTask.ComicConsumer mComicConsumer;
    private Comic comic;

    public GetComicImageTask(GetComicTask.ComicConsumer activity, Comic comic) {
        mComicConsumer = activity;
        this.comic = comic;
    }

    @Override
    protected Comic doInBackground(String... urlStrings) {
        String urlString = urlStrings[0];
        Bitmap mBitmap;


        try {
            InputStream in = new java.net.URL(urlString).openStream();
            mBitmap = BitmapFactory.decodeStream(in);
            comic.setBmp(mBitmap);
            return comic;
        } catch (IOException e) {
            Log.d(MainActivity.ComicFragment.ARG_FILEERROR, "ERROR: " + e.toString());
        }
        return null;

    }

    @Override
    protected void onPostExecute(Comic c) {
        super.onPostExecute(c);
        mComicConsumer.onComicImageLoaded(c);
    }
}
