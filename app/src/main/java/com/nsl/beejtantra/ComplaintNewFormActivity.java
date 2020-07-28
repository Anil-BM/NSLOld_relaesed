package com.nsl.beejtantra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.adapters.ComplaintImagesAdapter;
import com.nsl.beejtantra.commonutils.CircleImageView;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.complaints.Complaints;
import com.nsl.beejtantra.complaints.ComplaintsprodallActivity;
import com.nsl.beejtantra.marketintelligence.ProductSurveyAllActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComplaintNewFormActivity extends AppCompatActivity {

    @BindView(R.id.spin_company)
    Spinner spin_company;
    @BindView(R.id.spin_crop)
    Spinner spin_crop;
    @BindView(R.id.spin_variety)
    Spinner spin_variety;
    @BindView(R.id.spin_product)
    Spinner spin_product;
    @BindView(R.id.marketing_lot_number)
    EditText marketing_lot_number;
    @BindView(R.id.spin_complaint_type)
    Spinner spin_complaint_type;
    @BindView(R.id.complaint_per)
    EditText complaint_per;
    @BindView(R.id.complaint_remarks)
    EditText complaint_remarks;
    @BindView(R.id.date_of_complaint)
    TextView date_of_complaint;
    @BindView(R.id.dealer)
    EditText dealer;
    @BindView(R.id.spin_distributor)
    Spinner spin_distributor;
    @BindView(R.id.village)
    TextView village;
    @BindView(R.id.mandal_taluka)
    EditText mandal_taluka;
    @BindView(R.id.district)
    TextView district;
    @BindView(R.id.region)
    TextView region;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.zone)
    TextView zone;
    @BindView(R.id.spin_irrigatin_type)
    Spinner spin_irrigatin_type;
    @BindView(R.id.complaint_area_acers)
    EditText complaint_area_acersl;
    @BindView(R.id.name_of_farmer)
    EditText name_of_farmer;
    @BindView(R.id.date_of_purchase)
    TextView date_of_purchase;
    @BindView(R.id.no_of_pkt_pch)
    EditText no_of_pkt_pch;
    @BindView(R.id.cmp_no_of_pkt)
    EditText cmp_no_of_pkt;
    @BindView(R.id.bil_rec_refno)
    EditText bil_rec_refno;
    @BindView(R.id.farmer_contact_no)
    EditText farmer_contact_no;
    @BindView(R.id.date_of_sowing)
    TextView date_of_sowing;
    @BindView(R.id.performance_of_same_lot_in_others_fields)
    Spinner performance_of_same_lot_in_others_fields;
    @BindView(R.id.remarks)
    EditText remarks;
    @BindView(R.id.inspected_date)
    TextView inspected_date;
    @BindView(R.id.inspected_by_staff)
    TextView inspected_by_staff;
    @BindView(R.id.inspected_by_designation)
    TextView inspected_by_designation;
    @BindView(R.id.inspected_by_email)
    TextView inspected_by_email;
    @BindView(R.id.inspected_by_contact_no)
    TextView inspected_by_contact_no;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    //  private ImageView ivImage, ivImage1, ivImage2, ivImage4, ivImage5, ivImage6;
    private int imageViewId;
    private String userChoosenTask, result = "NO IMAGE";
    private String base64String1;
    private String base64String2;
    private String base64String3;
    private String base64String4;
    private String base64String5;
    private String base64String6;
    private Button submit;
    private Button cancel;
    ProgressDialog progressDialog;
    private CircleImageView imageView;
    @BindView(R.id.photos_layout1)
    LinearLayout photos_layout1;
    @BindView(R.id.photos_layout2)
    LinearLayout photos_layout2;
    List<File> files = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_new_form);
        ButterKnife.bind(this);
        cancel = (Button) findViewById(R.id.btncancel);
        submit = (Button) findViewById(R.id.btnSubmit);

//        ivImage = (ImageView) findViewById(R.id.ivImage);
//        ivImage1 = (ImageView) findViewById(R.id.ivImage1);
//        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
//        ivImage4 = (ImageView) findViewById(R.id.ivImage4);
//        ivImage5 = (ImageView) findViewById(R.id.ivImage5);
//        ivImage6 = (ImageView) findViewById(R.id.ivImage6);

//        ivImage.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                selectImage();
//                imageViewId = ivImage.getId();
//            }
//        });
//        ivImage1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                selectImage();
//                imageViewId = ivImage1.getId();
//            }
//        });
//        ivImage2.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                imageViewId = ivImage2.getId();
//                selectImage();
//            }
//        });
//        ivImage4.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                imageViewId = ivImage4.getId();
//                selectImage();
//            }
//        });
//        ivImage5.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                imageViewId = ivImage5.getId();
//                selectImage();
//            }
//        });
//        ivImage6.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                imageViewId = ivImage6.getId();
//                selectImage();
//            }
//        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewall = new Intent(ComplaintNewFormActivity.this, ComplaintsprodallActivity.class);
                startActivity(viewall);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ComplaintNewFormActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        byte[] byteArray = bytes.toByteArray();
        result = Base64.encodeToString(byteArray, Base64.DEFAULT);

        //Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        //  String result = Base64.encodeToString(destination, Base64.DEFAULT);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(thumbnail);
        addCameraView();
//        switch (imageViewId) {
//            case R.id.ivImage:
//                ivImage.setImageBitmap(thumbnail);
//                base64String1 = result;
//                break;
//            case R.id.ivImage1:
//                ivImage1.setImageBitmap(thumbnail);
//                base64String2 = result;
//
//                break;
//            case R.id.ivImage2:
//                ivImage2.setImageBitmap(thumbnail);
//                base64String3 = result;
//
//                break;
//
//            case R.id.ivImage4:
//                ivImage4.setImageBitmap(thumbnail);
//                base64String4 = result;
//
//                break;
//            case R.id.ivImage5:
//                ivImage5.setImageBitmap(thumbnail);
//                base64String5 = result;
//                break;
//            case R.id.ivImage6:
//                ivImage6.setImageBitmap(thumbnail);
//                base64String6 = result;
//                break;
//
//
//        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                //
                //   bm = (Bitmap) data.getExtras().get("data");
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                result = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        imageView.setImageBitmap(bm);
        addCameraView();
//        switch (imageViewId) {
//            case R.id.ivImage:
//                ivImage.setImageBitmap(bm);
//                base64String1 = result;
//                break;
//            case R.id.ivImage1:
//                ivImage1.setImageBitmap(bm);
//                base64String2 = result;
//
//                break;
//            case R.id.ivImage2:
//                ivImage2.setImageBitmap(bm);
//                base64String3 = result;
//
//                break;
//
//            case R.id.ivImage4:
//                ivImage4.setImageBitmap(bm);
//                base64String4 = result;
//
//                break;
//            case R.id.ivImage5:
//                ivImage5.setImageBitmap(bm);
//                base64String5 = result;
//                break;
//            case R.id.ivImage6:
//                ivImage6.setImageBitmap(bm);
//                base64String6 = result;
//                break;
//
//
//        }

    }


    private void validate() {
        if (spin_company.getSelectedItemPosition() == 0) {
            spin_company.requestFocus();
            Toast.makeText(this, "Please select company", Toast.LENGTH_SHORT).show();
            return;

        } else if (spin_crop.getSelectedItemPosition() == 0) {
            spin_crop.requestFocus();
            Toast.makeText(this, "Please select division", Toast.LENGTH_SHORT).show();
            return;
        } else if (spin_variety.getSelectedItemPosition() == 0) {
            spin_variety.requestFocus();
            Toast.makeText(this, "Please select crop select division", Toast.LENGTH_SHORT).show();
            return;
        } else if (spin_product.getSelectedItemPosition() == 0) {
            spin_product.requestFocus();
            Toast.makeText(this, "Please select product", Toast.LENGTH_SHORT).show();
            return;
        } else if (spin_complaint_type.getSelectedItemPosition() == 0) {
            spin_complaint_type.requestFocus();
            Toast.makeText(this, "Please select complaint type", Toast.LENGTH_SHORT).show();
            return;
        } else if (marketing_lot_number.getText().toString().trim().length() == 0) {
            marketing_lot_number.requestFocus();
            marketing_lot_number.setError("Please enter marketing lot number");
            return;
        } else if (complaint_per.getText().toString().trim().length() == 0) {
            complaint_per.requestFocus();
            complaint_per.setError("Please enter complaint percent");
            return;
        } else if (complaint_remarks.getText().toString().trim().length() == 0) {
            complaint_remarks.requestFocus();
            complaint_remarks.setError("Please enter complaint remarks");
            return;
        } /*else if (date_of_complaint.getText().toString().trim().length() == 0) {
            date_of_complaint.requestFocus();
            date_of_complaint.setError("Please enter date_of_complaint");
            return;
        } else if (dealer.getText().toString().trim().length() == 0) {
            dealer.requestFocus();
            dealer.setError("Please enter dealer name");
            return;
        } */ else if (spin_distributor.getSelectedItemPosition() == 0) {
            spin_distributor.requestFocus();
            Toast.makeText(this, "Please select distributor", Toast.LENGTH_SHORT).show();
            return;
        } else if (spin_distributor.getSelectedItemPosition() == 0) {
            spin_distributor.requestFocus();
            Toast.makeText(this, "Please select distributor", Toast.LENGTH_SHORT).show();
            return;
        } else if (spin_irrigatin_type.getSelectedItemPosition() == 0) {
            spin_irrigatin_type.requestFocus();
            Toast.makeText(this, "Please select irrigatin type", Toast.LENGTH_SHORT).show();
            return;
        } else if (complaint_area_acersl.getText().toString().trim().length() == 0) {
            complaint_area_acersl.requestFocus();
            Toast.makeText(this, "Please enter complaint area", Toast.LENGTH_SHORT).show();
            return;
        } else if (name_of_farmer.getText().toString().trim().length() == 0) {
            name_of_farmer.requestFocus();
            Toast.makeText(this, "Please enter name of farmer", Toast.LENGTH_SHORT).show();
            return;
        } else if (name_of_farmer.getText().toString().trim().length() == 0) {
            name_of_farmer.requestFocus();
            Toast.makeText(this, "Please enter name of farmer", Toast.LENGTH_SHORT).show();
            return;
        } else if (no_of_pkt_pch.getText().toString().trim().length() == 0) {
            no_of_pkt_pch.requestFocus();
            Toast.makeText(this, "Please enter pkts purchased", Toast.LENGTH_SHORT).show();
            return;
        } else if (cmp_no_of_pkt.getText().toString().trim().length() == 0) {
            cmp_no_of_pkt.requestFocus();
            Toast.makeText(this, "Please enter complaint no.of pkts", Toast.LENGTH_SHORT).show();
            return;
        } else if (bil_rec_refno.getText().toString().trim().length() == 0) {
            bil_rec_refno.requestFocus();
            Toast.makeText(this, "Please enter billing ref.no", Toast.LENGTH_SHORT).show();
            return;
        } else if (performance_of_same_lot_in_others_fields.getSelectedItemPosition() == 0) {
            performance_of_same_lot_in_others_fields.requestFocus();
            Toast.makeText(this, "Please enter lot other fields", Toast.LENGTH_SHORT).show();
            return;
        } else if (remarks.getText().toString().trim().length() == 0) {
            remarks.requestFocus();
            Toast.makeText(this, "Please enter remarks", Toast.LENGTH_SHORT).show();
            return;
        } else if (inspected_date.getText().toString().trim().length() == 0) {
            inspected_date.requestFocus();
            Toast.makeText(this, "Please enter inspected date", Toast.LENGTH_SHORT).show();
            return;
        }


        ArrayList<String> objects = new ArrayList<>();
        if (base64String1 != null) {
            objects.add(base64String1);
        }
        if (base64String2 != null) {
            objects.add(base64String2);
        }
        if (base64String3 != null) {
            objects.add(base64String3);
        }
        if (base64String4 != null) {
            objects.add(base64String4);
        }
        if (base64String5 != null) {
            objects.add(base64String5);
        }
        if (base64String6 != null) {
            objects.add(base64String6);
        }

//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < objects.size(); i++) {
//            try {
//                jsonArray.put(new JSONObject().put("image_64", objects.get(i)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//        if (Common.haveInternet(this)) {
//           // insertToService();
//        } else {
//            Common.dismissProgressDialog(progressDialog);
//            setResult(Activity.RESULT_OK);
//            Intent complaints = new Intent(this, ProductSurveyAllActivity.class);
//            startActivity(complaints);
//            finish();
//        }
    }

    private void addCameraView() {
        imageView = new CircleImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(125, 125);
        layoutParams.setMargins(15, 20, 10, 20);
        imageView.setPadding(0, 0, 0, 0);
        imageView.setBackgroundResource(R.drawable.camera);
        imageView.setLayoutParams(layoutParams);
        if (files.size() < 3) {
            photos_layout1.addView(imageView);
        }
        if (files.size() > 3 && files.size() < 6) {
            photos_layout1.addView(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
