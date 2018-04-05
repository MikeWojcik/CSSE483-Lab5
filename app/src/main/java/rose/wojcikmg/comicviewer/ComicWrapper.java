package rose.wojcikmg.comicviewer;

import android.os.Parcel;
import android.os.Parcelable;

public class ComicWrapper implements Parcelable {

    private int issue;
    private int color;
    public Comic comic;

    public ComicWrapper(){
        issue = Utils.getRandomCleanIssue();
        color = Utils.getRandomColor();
    }

    public int getIssueNum() {
        return issue;
    }

    protected ComicWrapper(Parcel in) {
        issue = in.readInt();
        color = in.readInt();
    }

    public static final Creator<ComicWrapper> CREATOR = new Creator<ComicWrapper>() {
        @Override
        public ComicWrapper createFromParcel(Parcel in) {
            return new ComicWrapper(in);
        }

        @Override
        public ComicWrapper[] newArray(int size) {
            return new ComicWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getColor(){
        return color;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(issue);
        parcel.writeInt(color);
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }
}
