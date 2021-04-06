package com.example.digitalbusinesscard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

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
    Uri ImageUri;

    String userId;
    StorageReference storageReference;
    String myUri = "";
    StorageTask uploadTask;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    DatabaseReference DRef;

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
        DRef = FirebaseDatabase.getInstance().getReference().child("users");

        /**StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/Pictures.jpg");
         profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
        Picasso.get().load(uri).into(EditProfileImage);
        }
        });**/





        EditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity().setAspectRatio(1,1).start(EditProfile.this);



                //Open Studio in Phone
                // Intent OpenStudioIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(OpenStudioIntent, OPENSTUDIO_REQUEST_CODE);
                //askCameraPermissions();
                //  Toast.makeText(EditProfile.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        getUserinfo();

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
                /** if(EditProfullName.getText().toString().isEmpty() || EditDescription.getText().toString().isEmpty() || EditPhoneNumber.getText().toString().isEmpty() || EditEmail.getText().toString().isEmpty() || EditWhatsapp.getText().toString().isEmpty() || EditAddress.getText().toString().isEmpty()){
                 Toast.makeText(EditProfile.this, "One or many fields is Empty.", Toast.LENGTH_SHORT).show();
                 return;
                 }**/
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

                HashMap RealHashMap = new HashMap();
                RealHashMap.put("fname",EditProfullName.getText().toString());
                RealHashMap.put("description", EditDescription.getText().toString());
                RealHashMap.put("phone", EditPhoneNumber.getText().toString());
                RealHashMap.put("email", EditEmail.getText().toString());
                RealHashMap.put("whatsapp", EditWhatsapp.getText().toString());
                RealHashMap.put("address", EditAddress.getText().toString());
                //RealHashMap.put("users",uri.toString());


                DRef.child(user.getUid()).updateChildren(RealHashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(EditProfile.this, "Setup profile Completed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                /**storageReference.child("users/"+user.getUid()+"/Pictures.jpg").putFile(contentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){

                storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {



                }
                });
                }
                }
                });**/


                uploadProfileImage();
            }
        });

        //------------------------
        EditProfullName.setText(profullName);
        EditDescription.setText(description);
        EditPhoneNumber.setText(phoneNumber);
        EditEmail.setText(email);
        EditWhatsapp.setText(whatsapp);
        EditAddress.setText(address);

        Log.d(TAG, "onCreate" + profullName + " " + description + " " + phoneNumber + " " + email + " " + whatsapp + " " + address );

    }

    private void getUserinfo() {
        DRef.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0)
                {
                    if (snapshot.hasChild("image"))
                    {
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(EditProfileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK  && data !=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImageUri = result.getUri();

            EditProfileImage.setImageURI(ImageUri);
        }
        else
        {
            Toast.makeText(this, "Erorr, Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your data ");
        progressDialog.show();

        if (ImageUri != null)
        {
            final StorageReference SRef = storageReference
                    .child("user"+fAuth.getCurrentUser().getUid()+ ".jpg");

            uploadTask = SRef.putFile(ImageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return SRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = (Uri) task.getResult();
                        myUri = downloadUri.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image",myUri);

                        DRef.child(fAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        progressDialog.dismiss();
                    }

                }
            });
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }
}