package com.nsl.beejtantra.product_catalogue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.adapters.CatalogueCropPAdapter;
import com.nsl.beejtantra.commonutils.Common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogueCorpPActivity extends AppCompatActivity {

    @BindView(R.id.info_recyclerview)
    RecyclerView info_recyclerview;
    @BindView(R.id.descrtiption)
    TextView description;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private DatabaseHandler db;
    private List<CatalogueCropsProductsPojo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title.setText(getIntent().getStringExtra("crop_name"));
       db=new DatabaseHandler(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        info_recyclerview.setLayoutManager(gridLayoutManager);
       // description.setText(Html.fromHtml("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags --><title>NSL</title><link href=\"css/base.css\" type=\"text/css\" rel=\"stylesheet\"><link href=\"css/bootstrap.css\" type=\"text/css\" rel=\"stylesheet\"><link href=\"css/bootstrap-select.css\" type=\"text/css\" rel=\"stylesheet\"><link href=\"css/font-awesome.css\" type=\"text/css\" rel=\"stylesheet\"><link href=\"https://fonts.googleapis.com/css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i\" rel=\"stylesheet\"> </head><body><div class=\"main_wrapper\"><!-- Content Wrapper. Contains page content --><div class=\"container\"><!-- Main content --><section class=\"content\"><div class=\"col-sm-12\"><div class=\"row catelogo_header\"><div class=\"col-sm-12text-center\"><h2 class=\"title_cat\">Mallika - <span>NCS 207 BG II</span></h2>\t<a href=\"#\" onclick=\"window.history.go(-1)\"><i class=\"fa fa-angle-left\"></i></a></div></div></div><div class=\"col-sm-4text-center\"><img src=\"images/mallikansc207bg11.png\" class=\"img-responsive\" alt=\"mallikansc207bg11\"></div><div class=\"col-sm-8\">\t <div class=\"table-responsive\"><table class=\"table table-bordered\"><thead><tr><thcolspan=\"2\">Morphological Characters</th></tr></thead><tbody><tr><td>Hybrid Character:</td><td>Mallika BG II(NCS-207 BG II)</td></tr><tr><td>Plant Height:</td><td>Very tall</td></tr><tr><td>Reaction to Major Pests:</td><td>Resistant to AmericanBollworm,<br/> Pink Bollworm,<br/>Spotted Bollworm andSpodoptera.<br/> Tolerant tosucking pests,<br/> Jassids andWhitefly</td></tr><tr><td>Boll Shape & Size:</td><td>Ovate and very large</td></tr> <tr><td>Quality of Produce: </td><td>Ginning%: 35 - 36<br/>Staple length: > 32.5 mm<br/>Strength: 21 - 24 g/tex<br/>Micronaire: 3.0 - 3.9</td></tr> <tr><tdcolspan=\"2\"><h2>Special Features/USPs:</h2><ul><li>Best Hybrid suitable for various soil types</li><li>Big bolls and higher boll weight</li><li>Sucking pests tolerant</li><li>Easy picking and Good rejuvenation ability.</li></ul></td></tr></tbody></table></div></div></section></div><!-- End- Content Wrapper. Contains page content --></div></body><script src=\"js/jquery.js\" type=\"text/javascript\"></script><script src=\"js/bootstrap.js\" type=\"text/javascript\"></script><script src=\"js/bootstrap-select.js\" type=\"text/javascript\"></script></html>"));
        list=db.getCatalogueCropProducts(String.valueOf(getIntent().getIntExtra("crop_id",0)));
        Common.Log.i(list.toString());
        final CatalogueCropPAdapter infoAdapter = new CatalogueCropPAdapter(this, list);
        info_recyclerview.setAdapter(infoAdapter);

        infoAdapter.setOnItemClickListener(new CatalogueCropPAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                Intent intent=new Intent(getApplicationContext(),ProductCatelogDescriptionActivity.class);
                intent.putExtra("key",list.get(position).serverPk);
                intent.putExtra("object", list.get(position));
                startActivity(intent);
            //    Toast.makeText(getApplicationContext(),"clickec at position : "+(position+1),Toast.LENGTH_SHORT).show();

            }
        });
    }

    /*private void setArrayList() {

        infoPojos.add(new InfoPojo("Cotton",R.mipmap.ic_cotton));
        infoPojos.add(new InfoPojo("Maize",R.mipmap.ic_maize));
        infoPojos.add(new InfoPojo("CropShift",R.mipmap.ic_cropshift));
        infoPojos.add(new InfoPojo("Dailydairy",R.mipmap.ic_dailydairy));
        infoPojos.add(new InfoPojo("Distributors",R.mipmap.ic_distributors));
        infoPojos.add(new InfoPojo("Market",R.mipmap.ic_marketintelligence));
        infoPojos.add(new InfoPojo("Products",R.mipmap.ic_products));
        infoPojos.add(new InfoPojo("Yeild",R.mipmap.ic_yieldestimation));
    }*/
}
