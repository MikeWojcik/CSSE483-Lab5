package rose.wojcikmg.comicviewer;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerTitleStrip;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ComicsPagerAdapter mComicsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mComicsPagerAdapter = new ComicsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mComicsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComicsPagerAdapter.getItem(mComicsPagerAdapter.size + 1);
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        //PagerTitleStrip p = findViewById(R.id.pager_title_strip);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ComicFragment extends Fragment implements GetComicTask.ComicConsumer {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private ComicWrapper mComicWrapper;

        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ARG_COMICWRAPPER = "comic_wrapper";
        public static final String ARG_FILEERROR = "File Error";

        public ComicFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ComicFragment newInstance(ComicWrapper comicWrapper) {
            ComicFragment fragment = new ComicFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, comicWrapper.getIssueNum());
            args.putParcelable(ARG_COMICWRAPPER, comicWrapper);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onCreate(Bundle savedInstanceStart) {
            super.onCreate(savedInstanceStart);

            if (getArguments() != null) {
                mComicWrapper = getArguments().getParcelable(ARG_COMICWRAPPER);
                String urlString = String.format("https://xkcd.com/%d/info.0.json", mComicWrapper.getIssueNum());
                new GetComicTask(this).execute(urlString);
            }
        }

        @Override
        public void onComicLoaded(Comic comic) {
            Log.d("COMIC", "Comic Object\n" + comic);
            mComicWrapper.setComic(comic);
            TextView v = getView().findViewById(R.id.section_label);
            v.setText(comic.getTitle());

            new GetComicImageTask(this, comic).execute(comic.getImg());
        }

        @Override
        public void onComicImageLoaded(Comic comic) {
            ImageView view = getView().findViewById(R.id.comicImage);
            view.setImageBitmap(comic.getBmp());
            getView().setBackgroundColor(getResources().getColor(mComicWrapper.getColor()));



        }

        @Override
        public void onResume() {
            super.onResume();
            String urlString = String.format("https://xkcd.com/%d/info.0.json", mComicWrapper.getIssueNum());
            new GetComicTask(this).execute(urlString);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class ComicsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<ComicWrapper> listWrap;
        private int size;

        public ComicsPagerAdapter(FragmentManager fm) {
            super(fm);
            listWrap = new ArrayList<>(6);
            //size =5;
        }

        @Override
        public Fragment getItem(int position) {
            if(position> size){
             listWrap.ensureCapacity(position);
            }
            ComicWrapper c = new ComicWrapper();
            listWrap.add(position,c);
            // getItem is called to instantiate the fragment for the given page.
            // Return a ComicFragment (defined as a static inner class below).
            return ComicFragment.newInstance(c);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
           // return listWrap.size();
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            if (5 >= position) {
                return "issue " + listWrap.get(position).getIssueNum();
            } else {
                return "issue";
           }
        }
    }
}