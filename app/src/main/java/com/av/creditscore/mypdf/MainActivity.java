package com.av.creditscore.mypdf;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    EditText user, email, num, dob, qualification;
    MaterialButton next;
    CircleImageView userdp;

    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;

    public static final int REQUEST_STORAGE=101;
    String storagePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("Data", 0);
        editor = sp.edit();

        user = findViewById(R.id.name);
        email = findViewById(R.id.email);
        num = findViewById(R.id.contact);
        dob = findViewById(R.id.dob);
        qualification = findViewById(R.id.qualification);
        next = findViewById(R.id.next);
        userdp = findViewById(R.id.userdp);

        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        userdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.getText().toString().trim().equalsIgnoreCase("")) {
                    user.setError("Enter Name");
                } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Enter Email");
                } else if (qualification.getText().toString().trim().equalsIgnoreCase("")) {
                    num.setError("Enter Number");
                } else if (num.getText().toString().trim().equalsIgnoreCase("")) {
                    dob.setError("Enter Date");
                }else if (dob.getText().toString().trim().equalsIgnoreCase("")) {
                    qualification.setError("Enter qualification");
                }else {

                    String name = user.getText().toString();
                    String email1 = email.getText().toString();
                    String num1 = num.getText().toString();
                    String dob1 = dob.getText().toString();
                    String qul = qualification.getText().toString();

                    Bitmap bitmap = ((BitmapDrawable) userdp.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] imageInByte = baos.toByteArray();

//                    String imagedata = Base64.encodeToString(imageInByte, 0);

//                    editor.putString("Name",name);
//                    editor.putString("Email",email1);
//                    editor.putString("Number",num1);
//                    editor.putString("Dob",dob1);
//                    editor.putString("Qualification",qul);
//                    editor.putString("Image",imagedata);
//                    editor.apply();

                    Intent intent=new Intent(MainActivity.this,Formate_Activity.class);
                    intent.putExtra("Name",name);
                    intent.putExtra("Email",email1);
                    intent.putExtra("Number",num1);
                    intent.putExtra("Dob",dob1);
                    intent.putExtra("Qualification",qul);
                    intent.putExtra("Image",imageInByte);
                    startActivity(intent);


                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                userdp.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}