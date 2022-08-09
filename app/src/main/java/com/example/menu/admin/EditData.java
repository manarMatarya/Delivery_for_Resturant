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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.menu.R;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditData extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "param2";
    EditText name, desc, price, calory;
    RadioGroup group;
    RadioButton sweet, juice, salad, sandwish, meals, apatizer;
    CheckBox vegetarian;
    boolean isVegetarian = false;
    ImageView image;
    Button add;
    String category = "";
    // Folder path for Firebase Storage.
    String Storage_Path = "food_images/";
    // Creating URI.
    Uri FilePathUri;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    // Image request code for onActivityResult() .
    int Image_Request_Code = 8;
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String id;
    private String mParam2;

    public EditData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditData.
     */
    // TODO: Rename and change types and number of parameters
    public static EditData newInstance(String param1, String param2) {
        EditData fragment = new EditData();
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
            id = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_data, container, false);

        image = v.findViewById(R.id.editimg);
        name = v.findViewById(R.id.editname);
        desc = v.findViewById(R.id.editdescription);
        price = v.findViewById(R.id.editprice);
        calory = v.findViewById(R.id.editcalories);
        group = v.findViewById(R.id.editcategory);
        sweet = v.findViewById(R.id.editsweet);
        juice = v.findViewById(R.id.editjuices);
        salad = v.findViewById(R.id.editsalad);
        sandwish = v.findViewById(R.id.editsanswishes);
        meals = v.findViewById(R.id.editmeals);
        apatizer = v.findViewById(R.id.editapatizer);
        vegetarian = v.findViewById(R.id.editvegetarian);
        add = v.findViewById(R.id.editbtn);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Food").whereEqualTo("id", id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Dish upload = q.toObject(Dish.class);
                    name.setText(upload.getName());
                    Picasso.get()
                            .load(upload.getImage())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(image);
                    price.setText(upload.getPrice() + "");
                    desc.setText(upload.getDescription());
                    calory.setText(upload.getCalories() + "");
                    isVegetarian = upload.getIsVegetarian();
                    vegetarian.setChecked(isVegetarian);
                    category = upload.getCategory();
                    if (category.equals("Dessert"))
                        sweet.setChecked(true);
                    else if (category.equals("Juices"))
                        juice.setChecked(true);
                    else if (category.equals("Salads"))
                        salad.setChecked(true);
                    else if (category.equals("Sandwiches"))
                        sandwish.setChecked(true);
                    else if (category.equals("Meals"))
                        meals.setChecked(true);
                    else if (category.equals("Appetizer"))
                        apatizer.setChecked(true);
                }

            }

        });

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
            progressDialog.setTitle("Updating...");

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
                                    Dish dish = new Dish(id, TempName, TempDesc, uri.toString(), category,isVegetarian, TempCalory,TempRating,TempPrice);
                                    firestore.document(id).set(dish).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
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
                            progressDialog.setTitle("Updating...");

                        }
                    });
        }
        else {

            Toast.makeText(getContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
}