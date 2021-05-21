package com.example.project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.db.DBHelper;
import com.example.project.model.Phone;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MyPhoneDetail extends AppCompatActivity {
    private TextView txtPhoneID;
    private ImageView imageView;
    private EditText etTitle;
    private EditText etPrice;
    private EditText etDescription;
    private EditText etModel;
    private EditText etColor;
    private EditText etMemory;
    private EditText etStatus;
    DBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_phone_detail);
        imageView = findViewById(R.id.viewPhone2);
        etTitle = findViewById(R.id.txtTilte2);
        etPrice = findViewById(R.id.txtPrice2);
        etDescription = findViewById(R.id.txtDescription2);
        etModel = findViewById(R.id.txtModel2);
        etColor = findViewById(R.id.txtColor2);
        etMemory = findViewById(R.id.txtMemory2);
        etStatus = findViewById(R.id.txtStatus2);
        txtPhoneID = findViewById(R.id.txtPhoneID);

        Intent intent = getIntent();
        Phone phone = (Phone) intent.getSerializableExtra("phone");
        txtPhoneID.setText(phone.getPhoneID() + "");
        etTitle.setText(phone.getTitle());
        etPrice.setText(phone.getPrice());
        etDescription.setText(phone.getDescription());
        etMemory.setText(phone.getMemory());
        etModel.setText(phone.getModel());
        etStatus.setText(phone.getStatus());
        etColor.setText(phone.getColor());

        byte[] image = phone.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        imageView.setImageBitmap(bitmap);
        dbHelper = new DBHelper(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.update:
                        int phoneID = Integer.parseInt(txtPhoneID.getText().toString());
                        String title = etTitle.getText().toString();
                        String price = etPrice.getText().toString();
                        String description = etDescription.getText().toString();
                        String model = etModel.getText().toString();
                        String color = etColor.getText().toString();
                        String memory = etMemory.getText().toString();
                        String status = etStatus.getText().toString();

                        SharedPreferences sharedPreferences = getSharedPreferences("MyReferences", MODE_PRIVATE);
                        int id = sharedPreferences.getInt("id", 0);
                        dbHelper.updatePhone(phoneID, title, model, status, color, memory, description, price, id);
                        Toast.makeText(getApplicationContext(), "Thay đổi thông tin thành công!!!", Toast.LENGTH_SHORT).show();
                        setResult(200);
                        finish();
                        break;
                    case R.id.delete:
                        showAlertDialog();
                        break;
                }
                return true;
            }
        });
    }

    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa bài đăng không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper = new DBHelper(MyPhoneDetail.this);
                int phoneID = Integer.parseInt(txtPhoneID.getText().toString());
                SharedPreferences sharedPreferences = getSharedPreferences("MyReferences", MODE_PRIVATE);
                int id = sharedPreferences.getInt("id", 0);
                dbHelper.deletePhone(phoneID, id);
                Toast.makeText(getApplicationContext(), "Xóa bài đăng thành công!!!", Toast.LENGTH_SHORT).show();
                setResult(200);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}