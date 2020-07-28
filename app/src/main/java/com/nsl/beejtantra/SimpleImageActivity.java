package com.nsl.beejtantra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoView;
import com.nsl.beejtantra.view.HackyViewPager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleImageActivity extends AppCompatActivity {
    @BindView(R.id.pager)
    HackyViewPager pager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ImageAdapter imageAdapter;

    int pos = 0;
    List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_image);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pos = getIntent().getExtras().getInt("index", 0);
        String imagesPath = getIntent().getExtras().getString("images", "");
        if (imagesPath.length() > 0) {
            String[] split = imagesPath.split(",");
            for (int i = 0; i < split.length; i++) {
                images.add(split[i]);
            }
        }
        imageAdapter = new ImageAdapter(this, images);
        pager.setAdapter(imageAdapter);
        pager.setCurrentItem(pos);
    }

    private static class ImageAdapter extends PagerAdapter implements View.OnClickListener {
        private List<String> images;

//		private static final String[] IMAGE_URLS = taskStartImages;

        private LayoutInflater inflater;
        Context context;
        private OnItemClickListener billlistner;


        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            billlistner = onItemClickListener;
        }


        public interface OnItemClickListener {
            void onItemClick(View view);
        }


        ImageAdapter(Context context, List<String> images) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.images = images;

        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            LayoutInflater factory = LayoutInflater.from(context);
            View imageLayout = factory.inflate(R.layout.item_pager_image, null);

            float angle = 90;
            //View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            assert imageLayout != null;
//            ImageView rotate = (ImageView) imageLayout.findViewById(R.id.rotate);
//            ImageView close = (ImageView) imageLayout.findViewById(R.id.finish);
//            ImageView share = (ImageView) imageLayout.findViewById(R.id.share);
            final PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

            //rotate.setOnClickListener(this);
           /* close.setOnClickListener(this);

            rotate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageView.setRotation(imageView.getRotation() + 90);
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri bmpUri = getLocalBitmapUri(imageView);
                    if (bmpUri != null) {
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image*//*");
                        // Launch sharing dialog for image
                        context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    } else {
                        // ...sharing failed, handle error	`
                    }
                }
            });*/


            Picasso.with(context).load(Constants.IMAGE_URL + images.get(position)).into(imageView);
            view.addView(imageLayout, 0);
            return imageLayout;
        }


        @Override
        public void onClick(View v) {
            if (billlistner != null) {
                billlistner.onItemClick(v);
            }


        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
