package com.example.letsconnectfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class ImageUpload extends AppCompatActivity {


    private ImageView pic;
    public Uri imageURI;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference riversRef;

    private Button btn;

    String domain_name;

    EditText caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);


        caption = (EditText) findViewById(R.id.cap);

        Intent i = getIntent();
        //Bundle bundle = i.getExtras();
        domain_name = i.getStringExtra("Domain");

        pic = findViewById(R.id.pic);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
            }
        });

        btn=(Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_page();
                //uploadPicture();
            }
        });
    }

    private void choosePic()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageURI = data.getData();
            pic.setImageURI(imageURI);
            //uploadPicture();
        }
    }

    private void uploadPicture()
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        riversRef = storageReference.child("images/" + randomKey);

        //String ss = storageReference.child("images/" + randomKey).getPath();
        //System.out.println(ss); //https://firebasestorage.googleapis.com/v0/b/imgtut-877c6.appspot.com/o/images%2F99fa7ab7-e36a-4f73-9c0c-b4fa7c648e79?alt=media&token=f6b491c3-e0ae-47ef-bc82-a17755bb3a7c

        riversRef.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();
//                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//                        System.out.println(riversRef.getDownloadUrl());
//                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference imageStore;
//                                if(domain_name.equals("IOT"))
//                                {
//                                    imageStore = FirebaseDatabase.getInstance().getReference().child("IOTImage");
//                                }
//                                else if(domain_name.equals("WebDev"))
//                                {
//                                    imageStore = FirebaseDatabase.getInstance().getReference().child("WebDevImage");
//                                }
//                                else if(domain_name.equals("ML_AI"))
//                                {
//                                    imageStore = FirebaseDatabase.getInstance().getReference().child("MLImage");
//                                }
//                                else
//                                {
//                                    imageStore = FirebaseDatabase.getInstance().getReference().child("NewImage");
//                                }

                                String descr = caption.getText().toString();


                                imageStore = FirebaseDatabase.getInstance().getReference().child(domain_name);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("image", String.valueOf(uri));
                                String no = imageStore.push().getKey();
                                imageStore.child(no).child("image").setValue(String.valueOf(uri));
                                imageStore.child(no).child("title").setValue(descr).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                                        if(domain_name.equals("IOT"))
                                        {
                                            next_page_iot();
                                        }
                                        else if(domain_name.equals("WebDev"))
                                        {
                                            next_page_wd();
                                        }
                                        else if(domain_name.equals("ML_AI"))
                                        {
                                            next_page_ml();
                                        }
                                    }
                                });
//                                imageStore.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
//                                    }
//                                });
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }

    public void next_page_iot()
    {
        Intent intent= new Intent(this, IOTPage.class);
        startActivity(intent);
    }
    public void next_page_wd()
    {
        Intent intent= new Intent(this, WebDev.class);
        startActivity(intent);
    }
    public void next_page_ml()
    {
        Intent intent= new Intent(this, ML_AI.class);
        startActivity(intent);
    }

    public void next_page()
    {
        uploadPicture();
    }
}
