package com.example.menu.admin;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.menu.R;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFood extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText name, desc, price, calory;
    RadioGroup group;
    CheckBox vegetarian;
    boolean isVegetarian = false;
    ImageView image;
    Button add;
    String category = "";
    // Folder path for Firebase Storage.
    String Storage_Path = "food_images/";
    // Creating URI.
    Uri FilePathUri;
    CollectionReference firestore;
    StorageReference storageReference;
    // Image request code for onActivityResult() .
    int Image_Request_Code = 8;
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFood() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFood.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFood newInstance(String param1, String param2) {
        AddFood fragment = new AddFood();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_food, container, false);

        image = v.findViewById(R.id.addimg);
        name = v.findViewById(R.id.addname);
        desc = v.findViewById(R.id.adddescription);
        price = v.findViewById(R.id.addprice);
        calory = v.findViewById(R.id.addcalories);
        group = v.findViewById(R.id.addcategory);
        vegetarian = v.findViewById(R.id.addvegetarian);
        add = v.findViewById(R.id.addbtn);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.sweet:
                        category = "Dessert";
                        break;
                    case R.id.meals:
                        category = "Meals";
                        break;
                    case R.id.juices:
                        category = "Juices";
                        break;
                    case R.id.salad:
                        category = "Salads";
                        break;
                    case R.id.sanswishes:
                        category = "Sandwiches";
                        break;
                    case R.id.apatizer:
                        category = "Appetizer";
                        break;
                }
            }
        });
        storageReference = com.google.firebase.storage.FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        firestore = FirebaseFirestore.getInstance().collection("Food");
        progressDialog = new ProgressDialog(getContext());


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating intent.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, Image_Request_Code);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImageFileToFirebaseStorage();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            Picasso.get().load(FilePathUri).into(image);
        }
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContext().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            final String TempName = name.getText().toString().trim();
                            final String TempDesc = desc.getText().toString().trim();
                            final float TempPrice = Float.valueOf(price.getText().toString().trim());
                            final float TempCalory = Float.valueOf(calory.getText().toString().trim());
                            final float TempRating = 5;
                            if(vegetarian.isChecked()){
                                isVegetarian = true;

                            }
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Random rand = new Random(); //instance of random class
                                    int upperbound = 1000;
                                    int random = rand.nextInt(upperbound);
                                    Dish dish = new Dish(random+"", TempName, TempDesc, uri.toString(), category,isVegetarian, TempCalory,TempRating,TempPrice);
                                    firestore.document(random+"").set(dish).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });
                            // Getting image upload ID.

                            // Adding image upload id s child element into databaseReference.

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(getContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }




}