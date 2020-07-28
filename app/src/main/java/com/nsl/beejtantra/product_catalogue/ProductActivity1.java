package com.nsl.beejtantra.product_catalogue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.adapters.InfoAdapter;
import com.nsl.beejtantra.commonutils.Common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductActivity1 extends AppCompatActivity {

    @BindView(R.id.info_recyclerview)
    RecyclerView info_recyclerview;
    @BindView(R.id.descrtiption)
    TextView description;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private DatabaseHandler db;
    private List<CatalogueCropsPojo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        toolbar_title.setText("Product Catalogue");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        db=new DatabaseHandler(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        info_recyclerview.setLayoutManager(gridLayoutManager);
        Log.d("device width: ", ""+Common.getScreenWidth());
        //Html.escapeHtml("&lt;p&gt;&lt;span [removed]=&quot;width:500px&quot;&gt;\\r\\n &lt;tbody&gt;\\r\\n  &lt;tr&gt;\\r\\n   &lt;td&gt;\\u00a0&lt;\\/td&gt;\\r\\n   &lt;td&gt;\\u00a0&lt;\\/td&gt;\\r\\n  &lt;\\/tr&gt;\\r\\n  &lt;tr&gt;\\r\\n   &lt;td&gt;\\u00a0&lt;\\/td&gt;\\r\\n   &lt;td&gt;\\u00a0&lt;\\/td&gt;\\r\\n  &lt;\\/tr&gt;\\r\\n  &lt;tr&gt;\\r\\n   &lt;td&gt;\\u00a0&lt;\\/td&gt;\\r\\n   &lt;td&gt;\\u00a0&lt;\\/td&gt;\\r\\n  &lt;\\/tr&gt;\\r\\n &lt;\\/tbody&gt;\\r\\n&lt;\\/table&gt;\\r\\n\\r\\n&lt;p&gt;\\u00a0&lt;\\/p&gt;\\r\\n");
/*
        description.setText(Html.fromHtml("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n" +
                "    <title>NSL</title>\n" +
                "    <link href=\"file:///android_asset/css/base.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <link href=\"file:///android_asset/css/bootstrap.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <link href=\"file:///android_asset/css/bootstrap-select.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <link href=\"file:///android_asset/css/font-awesome.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i\" rel=\"stylesheet\"> \n" +
                "  </head>\n" +
                "  <body>\n" +
                "<div class=\"main_wrapper\">\n" +
                "    <!-- Content Wrapper. Contains page content -->\n" +
                "  <div class=\"container\">\n" +
                "    <!-- Main content -->\n" +
                "    <section class=\"content\">\n" +
                "    <div class=\"col-sm-12\">\n" +
                "    <div class=\"row catelogo_header\">\n" +
                "    <div class=\"col-sm-12  text-center\">\n" +
                "    <h2 class=\"title_cat\">Mallika - <span>NCS 207 BG II</span></h2>\n" +
                "\t<a href=\"#\" onclick=\"window.history.go(-1)\"><i class=\"fa fa-angle-left\"></i></a>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "    <!--<div class=\"col-sm-4  text-center\">\n" +
                "    <img src=\"images/mallikansc207bg11.png\" class=\"img-responsive\" alt=\"mallikansc207bg11\">\n" +
                "    </div>\n" +
                "\t-->\n" +
                "        <div class=\"col-sm-8\">\n" +
                "\t <div class=\"table-responsive\">\n" +
                "    <table class=\"table table-bordered\">\n" +
                "    <thead>\n" +
                "      <tr>\n" +
                "        <th  colspan=\"2\">Morphological Characters</th>\n" +
                "      </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td>Hybrid Character:</td>\n" +
                "        <td>Mallika BG II(NCS-207 BG II)</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>Plant Height:</td>\n" +
                "        <td>Very tall</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>Reaction to Major Pests:</td>\n" +
                "        <td>Resistant to American\n" +
                "Bollworm,<br/> Pink Bollworm,<br/>\n" +
                "Spotted Bollworm and\n" +
                "Spodoptera.<br/> Tolerant to\n" +
                "sucking pests,<br/> Jassids and\n" +
                "Whitefly</td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>Boll Shape & Size:</td>\n" +
                "        <td>Ovate and very large</td>\n" +
                "      </tr>  \n" +
                "       <tr>\n" +
                "        <td>Quality of Produce: </td>\n" +
                "        <td>Ginning%: 35 - 36<br/>\n" +
                "Staple length: > 32.5 mm<br/>\n" +
                "Strength: 21 - 24 g/tex<br/>\n" +
                "Micronaire: 3.0 - 3.9</td>\n" +
                "      </tr>\n" +
                "       <tr>\n" +
                "        <td  colspan=\"2\">\n" +
                "        <h2>Special Features/USPs:</h2>\n" +
                "        <ul>\n" +
                "        <li>Best Hybrid suitable for various soil types</li>\n" +
                "        <li>Big bolls and higher boll weight</li>\n" +
                "        <li>Sucking pests tolerant</li>\n" +
                "        <li>Easy picking and Good rejuvenation ability.</li>\n" +
                "        </ul>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table></div></div>\n" +
                "    </section>\n" +
                "  </div>\n" +
                "  <!-- End- Content Wrapper. Contains page content -->\n" +
                "</div>\n" +
                "  </body>\n" +
                "  <script src=\"file:///android_asset/js/jquery.js\" type=\"text/javascript\"></script>\n" +
                "  <script src=\"file:///android_asset/js/bootstrap.js\" type=\"text/javascript\"></script>\n" +
                "  <script src=\"file:///android_asset/js/bootstrap-select.js\" type=\"text/javascript\"></script>\n" +
                "</html>"));
*/
       list= db.getCatalogueCrops();
        InfoAdapter infoAdapter = new InfoAdapter(this,list );
        info_recyclerview.setAdapter(infoAdapter);

        infoAdapter.setOnItemClickListener(new InfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                Intent intent=new Intent(getApplicationContext(), CatalogueCorpPActivity.class);
                intent.putExtra("crop_id",list.get(position).serverPk);
                intent.putExtra("crop_name",list.get(position).cropName);
                startActivity(intent);
               // Toast.makeText(getApplicationContext(),"clickec at position : "+(position+1),Toast.LENGTH_SHORT).show();

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
