package rose.wojcikmg.comicviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GetComicTask extends AsyncTask<String, Void, Comic> {
    private ComicConsumer mComicConsumer;

    public GetComicTask(ComicConsumer activity) {
        mComicConsumer = activity;
    }

    @Override
    protected Comic doInBackground(String... urlStrings) {
        String urlString = urlStrings[0];
        Comic comic = null;
        try {
            comic = new ObjectMapper().readValue(new URL(urlString), Comic.class);
        } catch (IOException e) {
            Log.d(MainActivity.ComicFragment.ARG_FILEERROR, "ERROR: " + e.toString());
        }
        return comic;
    }

    @Override
    protected void onPostExecute(Comic comic) {
        super.onPostExecute(comic);
        mComicConsumer.onComicLoaded(comic);
    }

    public interface ComicConsumer {
        public void onComicLoaded(Comic comic);

        public void onComicImageLoaded(Comic comic);
    }
}


