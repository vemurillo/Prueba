package com.example.lec04x;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class StorageFragment extends Fragment {

    private ImageButton btCargar;
    private ImageButton btBuscar;
    private ImageButton btDescargar;
    private ImageView imagen;
    private static final int GALLERYINTENT = 1;
    private StorageReference mStorageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_storage, container, false);
        btDescargar = view.findViewById(R.id.btDescargar);
        btBuscar = view.findViewById(R.id.btBuscar);
        btCargar = view.findViewById(R.id.btCargar);
        imagen = view.findViewById(R.id.imagen);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        btDescargar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                descargaImagen();
            }
        });

        btCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERYINTENT);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == GALLERYINTENT && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();

            StorageReference filepath = mStorageRef.child("fotos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }

    private void descargaImagen() {

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageReference riversRef = mStorageRef.child("imagen1.jpg");

        final File finalLocalFile = localFile;
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        imagen.setImageBitmap(BitmapFactory.decodeFile(finalLocalFile.getPath()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ... prueba
            }
        });
    }



    /*

    startActivity
    startActivityForResult ....
     */
}
