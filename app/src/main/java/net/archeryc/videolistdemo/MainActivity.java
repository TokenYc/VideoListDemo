package net.archeryc.videolistdemo;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.waynell.videolist.visibility.calculator.ListItemsVisibilityCalculator;
import com.waynell.videolist.visibility.calculator.SingleListViewItemActiveCalculator;
import com.waynell.videolist.visibility.items.ListItem;
import com.waynell.videolist.visibility.scroll.ItemsProvider;
import com.waynell.videolist.visibility.scroll.RecyclerViewItemPositionGetter;
import com.waynell.videolist.widget.TextureVideoView;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListItemsVisibilityCalculator mCalculator;
    private int mScrollState;
    String videoPath = "http://7xpp4m.com1.z0.glb.clouddn.com/22221470383977487.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final ListAdapter adapter = new ListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCalculator = new SingleListViewItemActiveCalculator(adapter,
                new RecyclerViewItemPositionGetter(linearLayoutManager, recyclerView));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mScrollState = newState;
                if(newState == RecyclerView.SCROLL_STATE_IDLE && adapter.listItemSize()>0){
                    mCalculator.onScrollStateIdle();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mCalculator.onScrolled(mScrollState);
            }
        });
    }

    /**
     * 需要实现ItemsProvider来提供数据
     */
    class ListAdapter extends RecyclerView.Adapter implements ItemsProvider {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_video, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof VideoViewHolder) {
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;

            }
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        @Override
        public ListItem getListItem(int position) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            if (holder instanceof ListItem) {
                return (ListItem) holder;
            }
            return null;
        }

        @Override
        public int listItemSize() {
            return getItemCount();
        }


        /**
         * 需要实现ListItem来接收item是否可见的回调
         */
        class VideoViewHolder extends RecyclerView.ViewHolder implements ListItem {
            private ImageView imageView;
            private TextureVideoView videoView;

            public VideoViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                videoView = (TextureVideoView) itemView.findViewById(R.id.videoview);
            }

            //item被激活
            @Override
            public void setActive(View newActiveView, int newActiveViewPosition) {
                Log.d("active", "active");
                videoView.setVideoPath(videoPath);
                videoView.start();
                videoView.setMediaPlayerCallback(new TextureVideoView.MediaPlayerCallback() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                    }

                    @Override
                    public void onCompletion(MediaPlayer mp) {

                    }

                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {

                    }

                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                    }

                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        return false;
                    }

                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return false;
                    }
                });
            }

            //item被取消
            @Override
            public void deactivate(View currentView, int position) {
                Log.d("active", "deactive");
                videoView.stop();
            }
        }
    }
}
