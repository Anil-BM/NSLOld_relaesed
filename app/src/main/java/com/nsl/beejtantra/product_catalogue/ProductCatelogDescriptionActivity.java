package com.nsl.beejtantra.product_catalogue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsl.beejtantra.R;
import com.nsl.beejtantra.products.ProductCatelogAdapter;
import com.nsl.beejtantra.products.ProductCatelogPojo;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sowmy on 6/14/2018.
 */

public class ProductCatelogDescriptionActivity extends AppCompatActivity {
    @BindView(R.id.titlename)
    TextView titlename;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.morohological)
    TextView morohological;
    @BindView(R.id.recyclercatelog)
    RecyclerView recyclercatelog;
    @BindView(R.id.spl_layout)
    LinearLayout spl_layout;
    @BindView(R.id.specialfeature)
    TextView specialfeature;
    @BindView(R.id.list_layout)
    LinearLayout list_layout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.webview_html)
    WebView webview_html;
    ArrayList<ProductCatelogPojo> productCatelogPojos;
    private int key;
    private CatalogueCropsProductsPojo catalogueCropsProductsPojo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_catelog_description);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
      /*  back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        key = getIntent().getIntExtra("key", 0);
        catalogueCropsProductsPojo = (CatalogueCropsProductsPojo) getIntent().getSerializableExtra("object");
        toolbar_title.setText(catalogueCropsProductsPojo.productName);
        if(catalogueCropsProductsPojo.imgURI!=null){
            Picasso.with(this).load(new File(catalogueCropsProductsPojo.imgURI)).into(iv_image);
        }

        webview_html.loadDataWithBaseURL("file:///android_asset/", catalogueCropsProductsPojo.description, "text/html", "utf-8", null);

        productCatelogPojos = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclercatelog.setLayoutManager(linearLayoutManager);
        setArrayList();
        ProductCatelogAdapter productCatelogAdapter = new ProductCatelogAdapter(this, productCatelogPojos);
        recyclercatelog.setAdapter(productCatelogAdapter);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 10; i++) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            ImageView image = new ImageView(this);
            image.setPadding(10, 13, 5, 5);
            image.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
            image.setImageResource(R.drawable.ic_dotgray);
            layout.addView(image);
//        linearLayout.addView(image);

            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setText("Best Hybrid suitable for various soil types");
            text.setTextSize(13);
            text.setPadding(20, 5, 5, 5);
            layout.addView(text);
            linearLayout.addView(layout);


        }
        list_layout.addView(linearLayout);

    }

    private void setArrayList() {
        productCatelogPojos.add(new ProductCatelogPojo("Hybrid Character:", "Mallika BG II(NCS-207 BG II)"));
        productCatelogPojos.add(new ProductCatelogPojo("Plant Height:", "Very tall"));
        productCatelogPojos.add(new ProductCatelogPojo("Reaction to Major Pests:", "Resistant to American Bollworm,Pink Bollworm,Spotted Bollworm and Spodoptera.Tolerant to sucking pests,Jassids and Whitefly"));
        productCatelogPojos.add(new ProductCatelogPojo("Boll Shape & Size:", "Ovate and very large"));
        productCatelogPojos.add(new ProductCatelogPojo("Quality of Produce: ", "Ginning%: 35 - 36 Staple length: > 32.5 mm Strength: 21 - 24 g/tex Micronaire: 3.0 - 3.9"));
        productCatelogPojos.add(new ProductCatelogPojo("Hybrid Character:", "Mallika BG II(NCS-207 BG II)"));


    }
}
