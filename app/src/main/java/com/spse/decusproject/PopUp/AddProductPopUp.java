package com.spse.decusproject.PopUp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spse.decusproject.Objects.Product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddProductPopUp extends Activity implements DatePickerDialog.OnDateSetListener {
    private EditText productName,productBrand;
    private Button addProduct;
    private Spinner spinner;
    private TextView dateText;
    private String date;
    private ImageView goback;

    private DatabaseReference databaseProducts;

    private FirebaseAuth fAuth;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_product_pop);

            productName = findViewById(R.id.productName);
            productBrand = findViewById(R.id.productBrand);
            addProduct = findViewById(R.id.btnAddProduct);
            spinner = findViewById(R.id.spinner);
            dateText = findViewById(R.id.date_text);
            goback = findViewById(R.id.goBack);

            fAuth = FirebaseAuth.getInstance();


            databaseProducts = FirebaseDatabase.getInstance().getReference("productsDatabase").child(fAuth.getCurrentUser().getUid());

            DisplayMetrics dm= new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);

            int width= dm.widthPixels;
            int height =dm.heightPixels;

            getWindow().setLayout((int)(width*.8),(int)(height*.8));

            WindowManager.LayoutParams params=getWindow().getAttributes();
            params.gravity= Gravity.CENTER;
            params.x=0;
            params.y=-20;

            getWindow().setAttributes(params);
            getWindow().setAttributes(params);
            List<String> list= new ArrayList<>();
            list.add("Acid");
            list.add("Mask");
            list.add("Cleanser");
            list.add("Moisturizer");
            list.add("Oil");
            list.add("Make Up");
            list.add("Fragrance");
            list.add("Nails care");


        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);

        findViewById(R.id.date_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
    }
});

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }


        });

    }

    private void addProduct() {
        String name=productName.getText().toString().trim();
        String brand=productBrand.getText().toString().trim();
        String category = spinner.getSelectedItem().toString();
        String productDate=dateText.getText().toString().trim();


        if (!TextUtils.isEmpty(name)){
            if (!TextUtils.isEmpty(brand)){
                if (!productDate.contains("Choose expiration date.")){
                        String id=databaseProducts.push().getKey();
                        Product product = new Product(name,brand,category,date,id);
                        databaseProducts.child(id).setValue(product);

                    Toast.makeText(AddProductPopUp.this,"Product added.",Toast.LENGTH_LONG).show();
                   finish();
                }
                else Toast.makeText(AddProductPopUp.this,"Choose product expiration date.",Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(AddProductPopUp.this,"Enter product brand.",Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(AddProductPopUp.this,"Enter product name.",Toast.LENGTH_LONG).show();

    }

    public void showDatePickerDialog(){
               DatePickerDialog datePickerDialog = new DatePickerDialog(this,  this,Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) , Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
               datePickerDialog.show();

         }


       public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = month + "/" + dayOfMonth + "/" + year;
        dateText.setText("   Expiration date: " +date);
    }
   }