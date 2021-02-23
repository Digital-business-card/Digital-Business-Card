package com.example.digitalbusinesscard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.JetPlayer;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int OPENSTUDIO_REQUEST_CODE = 103;
    public static final String TAG = "TAG";
    EditText EditProfullName,EditDescription,EditPhoneNumber,EditEmail,EditWhatsapp,EditAddress;
    ImageView EditProfileImage;
    Button EditSaveButton,EditCancel;
    String currentPhotoPath;

    String userId;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Intent
        Intent data = getIntent();
        String profullName = data.getStringExtra("ProfullName");
        String description = data.getStringExtra("Description");
        String phoneNumber = data.getStringExtra("phoneNumber");
        String email = data.getStringExtra("Email");
        String whatsapp = data.getStringExtra("Whatsapp");
        String address = data.getStringExtra("Address");



        EditProfullName = findViewById(R.id.EditProName);
        EditDescription = findViewById(R.id.EditProDescription);
        EditPhoneNumber = findViewById(R.id.EditProPhone);
        EditEmail = findViewById(R.id.EditProEmail);
        EditWhatsapp = findViewById(R.id.EditProWhats);
        EditAddress = findViewById(R.id.EditProAddress);



        EditProfileImage = findViewById(R.id.EditProImage);
        EditCancel = findViewById(R.id.EditCancel);
        EditSaveButton = findViewById(R.id.EditSaveBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/Pictures.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(EditProfileImage);
            }
        });





        EditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Open Studio in Phone
                Intent OpenStudioIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenStudioIntent, OPENSTUDIO_REQUEST_CODE);
                askCameraPermissions();
                Toast.makeText(EditProfile.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        EditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
                finish();
            }
        });

        EditSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EditProfullName.getText().toString().isEmpty() || EditDescription.getText().toString().isEmpty() || EditPhoneNumber.getText().toString().isEmpty() || EditEmail.getText().toString().isEmpty() || EditWhatsapp.getText().toString().isEmpty() || EditAddress.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "One or many fields is Empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Toast.makeText(EditProfile.this, "Clicked", Toast.LENGTH_SHORT).show();

                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("fName",EditProfullName.getText().toString());
                        edited.put("description", EditDescription.getText().toString());
                        edited.put("phone", EditPhoneNumber.getText().toString());
                        edited.put("email", EditEmail.getText().toString());
                        edited.put("whatsapp", EditWhatsapp.getText().toString());
                        edited.put("address", EditAddress.getText().toString());
                        docRef.set(edited);
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "Profile Updated.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Profile.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfile.this, "failed.", Toast.LENGTH_SHORT).show();
                            }
                        });




            }
        });
        EditProfullName.setText(profullName);
        EditDescription.setText(description);
        EditPhoneNumber.setText(phoneNumber);
        EditEmail.setText(email);
        EditWhatsapp.setText(whatsapp);
        EditAddress.setText(address);

        Log.d(TAG, "onCreate" + profullName + " " + description + " " + phoneNumber + " " + email + " " + whatsapp + " " + address );

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == CAMERA_REQUEST_CODE) {
        //    if (resultCode == Activity.RESULT_OK) {
         //       File f = new File(currentPhotoPath);
                //EditProfileImage.setImageURI(Uri.fromFile(f));
           //     Log.d("tag","Absolute Url of Image is "+ Uri.fromFile(f));

              //  Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
               // Uri contentUri = Uri.fromFile(f);
              //  mediaScanIntent.setData(contentUri);
              //  this.sendBroadcast(mediaScanIntent);

               // uploadImageToFirebase(f.getName(),contentUri);
            //}

       // }

        if (requestCode == OPENSTUDIO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " +  imageFileName);
                //EditProfileImage.setImageURI(contentUri);

                EditProfileImage.setImageURI(contentUri);

                uploadImageToFirebase(imageFileName,contentUri);
            }

        }

    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/Pictures.jpg");
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //-----------------------> if i want to take the picture from the firebase.
                        //Picasso.get().load(uri).into(EditProfileImage);
                    }
                });
                Toast.makeText(EditProfile.this, "Image is Uploaded Successfully.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
               File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.digitalbusinesscard.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


}