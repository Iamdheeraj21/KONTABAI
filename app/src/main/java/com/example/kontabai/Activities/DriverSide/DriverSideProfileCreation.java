package com.example.kontabai.Activities.DriverSide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kontabai.Activities.MainActivity;
import com.example.kontabai.Activities.UserSide.UserSideProfileCreation;
import com.example.kontabai.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverSideProfileCreation extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST =121 ;
    private static final int PERMISSION_CAMERA_CODE = 111;
    EditText fullname,carnumber,mobilenumber;
    TextView submit;
    ImageView driverImage,carimage;
    CircleImageView circleImageView;
    FloatingActionButton addCarImageButton;

    Uri imageUri,imageUriCar,imageUriDriver;
    String whichImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_side_profile_creation);
        initViews();

        addCarImageButton.setOnClickListener(view -> {
            whichImage="car";
            popMenu(addCarImageButton);
        });
        driverImage.setOnClickListener(view -> {
            whichImage="driver";
            popMenu(driverImage);
        });
        circleImageView.setOnClickListener(v -> {
            whichImage="driver";
            popMenu(driverImage);
        });
        submit.setOnClickListener(view -> {
            String fullName=fullname.getText().toString();
            String phonenumber=mobilenumber.getText().toString();
            String carNumber=carnumber.getText().toString();
//            if(fullName.equals("")) {
//                fullname.setError("write your name!");}
////            }else if(carNumber.length()!=10){
////                carnumber.setError("Invalid car number");
////            }
//            else if(carNumber.equals("")){
//                carnumber.setError("Enter the car number!");
//            }else if(phonenumber.equals("")){
//                mobilenumber.setEnabled(true);
//                mobilenumber.setError("Enter the number!");
//                mobilenumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
//            }else {
//                Intent intent=new Intent(DriverSideProfileCreation.this, UserSideProfileCreation.class);
//                intent.putExtra("type","driver");
//                intent.putExtra("name",fullName);
//                intent.putExtra("number",phonenumber);
//                intent.putExtra("carnumber",carNumber);
//                startActivity(intent);
//            }
            submit.setBackgroundResource(R.drawable.screen_background);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(DriverSideProfileCreation.this, DriverDashBoard.class);
                    intent.putExtra("type","driver");
                    intent.putExtra("name",fullName);
                    intent.putExtra("number",phonenumber);
                    intent.putExtra("carnumber",carNumber);
                    startActivity(intent);
                }
            },500);

        });
    }
    private void initViews() {
        fullname=findViewById(R.id.driverFullName);
        carnumber=findViewById(R.id.carNumber);
        mobilenumber=findViewById(R.id.driverPhoneNumber);
        submit=findViewById(R.id.createDriverProfileButton);
        addCarImageButton=findViewById(R.id.carimageButton);
        carimage=findViewById(R.id.carImage);
        driverImage=findViewById(R.id.driverSideImageview);
        circleImageView=findViewById(R.id.driverSideImageview2);
    }

    private void popMenu(ImageView imageView) {
        PopupMenu menu=new PopupMenu(DriverSideProfileCreation.this,imageView);
        menu.getMenuInflater().inflate(R.menu.popmenu_imageview,menu.getMenu());
        menu.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId()==R.id.fromCamera){
                openCamera();
                return true;
            }else if(menuItem.getItemId()==R.id.fromGallery){
                SelectImage();
                return true;
            }
            return false;
        });
        menu.show();
    }
    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    private void openCamera() {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent1, PERMISSION_CAMERA_CODE);
        intent1.setType("image/*");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PERMISSION_CAMERA_CODE && resultCode== Activity.RESULT_OK ){
//            imageUri=data.getData();
//            Bitmap mImageUri = null;
//            try {
//                mImageUri = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if(mImageUri==null){
//                Toast.makeText(DriverSideActivity.this, "Please Select the image", Toast.LENGTH_SHORT).show();
//            }else {
//                if(whichImage.equals("driver")){
//                    imageUriDriver=imageUri;
//                    driverImage.setImageBitmap(mImageUri);
//                    whichImage="";}
//                if(whichImage.equals("car")){
//                    imageUriCar=imageUri;
//                    addCarImage.setImageBitmap(mImageUri);
//                    whichImage="";
//                }
//            }
            if(whichImage.equals("driver")){
                //imageUriDriver=imageUri;
                assert data != null;
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                driverImage.setImageBitmap(photo);
                whichImage="";}
            if(whichImage.equals("car")){
                assert data != null;
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                addCarImageButton.setVisibility(View.GONE);
                carimage.setVisibility(View.VISIBLE);
                carimage.setImageBitmap(photo);
                whichImage="";
            }else{
                addCarImageButton.setVisibility(View.VISIBLE);
                carimage.setVisibility(View.GONE);
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            try {// Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                if(imageUri==null){
                    Toast.makeText(DriverSideProfileCreation.this, "Please Select the image", Toast.LENGTH_SHORT).show();
                }else {
                    if(whichImage.equals("driver")){
                        imageUriDriver=imageUri;
                        driverImage.setImageBitmap(bitmap);
                        whichImage="";}
                    if(whichImage.equals("car")){
                        imageUriCar=imageUri;
                        addCarImageButton.setVisibility(View.GONE);
                        carimage.setVisibility(View.VISIBLE);
                        carimage.setImageBitmap(bitmap);
                        whichImage="";
                    }else{
                        addCarImageButton.setVisibility(View.VISIBLE);
                        carimage.setVisibility(View.GONE);
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //String number= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        submit.setBackgroundResource(R.drawable.black_corners);
    }
}