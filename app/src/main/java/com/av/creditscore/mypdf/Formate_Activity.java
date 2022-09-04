package com.av.creditscore.mypdf;

import static com.av.creditscore.mypdf.MainActivity.sp;
import static com.av.creditscore.mypdf.R.layout.activity_formate;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Formate_Activity extends AppCompatActivity {

    ImageView imageView;
    TextView t1, t2, t3, t4, t5;
    RelativeLayout relativeLayout;
    ImageButton save;
    Bitmap bitmap;
    String pdfname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_formate);

        imageView = findViewById(R.id.fdp);
        relativeLayout = findViewById(R.id.listdata);
        save = findViewById(R.id.done);
        t1 = findViewById(R.id.fname);
        t2 = findViewById(R.id.fdob);
        t3 = findViewById(R.id.fqual);
        t4 = findViewById(R.id.fnum);
        t5 = findViewById(R.id.femail);

//        byte[] data = sp.getString("Image", "null").getBytes();
//        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//        imageView.setImageBitmap(bmp);

        byte[] image=getIntent().getByteArrayExtra("Image");
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        imageView.setImageBitmap(bmp);

        String iname=getIntent().getStringExtra("Name");
        String iemail=getIntent().getStringExtra("Email");
        String inum=getIntent().getStringExtra("Number");
        String idob=getIntent().getStringExtra("Dob");
        String iqul=getIntent().getStringExtra("Qualification");

        t1.setText("Name : " + iname);
        t2.setText("Dob : " + idob);
        t3.setText("Qualification : " +iqul);
        t4.setText("Number : " + inum);
        t5.setText("Email : " + iemail);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("size", relativeLayout.getWidth() + " " + relativeLayout.getHeight());
                bitmap = loadBitmapFromView(relativeLayout, relativeLayout.getWidth(), relativeLayout.getHeight());
                cretePdf();

            }
        });
    }

    private void cretePdf() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;

        int convertHeight = (int) height,
                convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);


        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Resume");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        int cnt = new Random().nextInt(100000);
        pdfname = "Doc" + cnt + ".pdf";

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Resume/" + pdfname);

//        // Write Document content
//        String targatePdf="internalStorage/Download/page.pdf";
//        File filepath=new File(targatePdf);
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("eeeee", "cretePdf: " + e.toString());
            Toast.makeText(this, "Something Went Wrong Try Again" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        //Close Document
        document.close();
        Toast.makeText(this, "PDF Created Successfully", Toast.LENGTH_SHORT).show();
//        openPdf();

        startActivity(new Intent(Formate_Activity.this,MainActivity.class));
        finish();

    }

    private void openPdf() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Resume/" + pdfname);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No Application Found For PDF Reader", Toast.LENGTH_SHORT).show();
        }

    }

    private Bitmap loadBitmapFromView(RelativeLayout relativeLayout, int width, int height) {

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        relativeLayout.draw(canvas);
        return bitmap;
    }

}