
package com.bvm.ui_example_4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Assignment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static TextView Marks, Status, Assessed, Total_marks, Assignment_no;
    private static String assignment, marks_scored, flag, totalmarks;
    private static Button Upload, submit;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton button;
    int PICK_IMAGE_REQUEST = 2342;
    Uri filePath;
    ProgressDialog pd;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://newapp1-baff1.appspot.com");    //change the url according to your firebase app


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Status = findViewById(R.id.submitteds);
        Marks = findViewById(R.id.Score);
        Assessed = findViewById(R.id.assessed);
        Total_marks = findViewById(R.id.Marks);
        Assignment_no = findViewById(R.id.Ass);
        Upload = findViewById(R.id.Upload);
        submit = findViewById(R.id.Submit);
        button= findViewById(R.id.more);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupmenu= new PopupMenu(Assignment.this,button);
                popupmenu.getMenuInflater().inflate(R.menu.sub_menu,popupmenu.getMenu());

                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {



                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(Assignment.this,""+ menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;

                    }
                });
                popupmenu.show();

            }
        });



        /*-----hooks-----*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view1);
        toolbar = findViewById(R.id.assignment_toolbar);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);



        /****************PDF Upload code here*****************/

        DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String  flag1 = dataSnapshot.child("Subject").child("A-Flag").getValue().toString();
                if(flag1.equals("true")){
                    Status.setText(flag1);
                    assignment = dataSnapshot.child("Subject").child("Current-A").getValue().toString();
                    totalmarks = dataSnapshot.child("Subject").child("Mark").getValue().toString();
                    String status = dataSnapshot.child("Login Info").child("18EL003").child("Subject").child("flag").getValue().toString();
                    Assessed.setText(status);
                    Assignment_no.setText(assignment);
                    Total_marks.setText(totalmarks);

                    if(status.equals("true")){
                        String score = dataSnapshot.child("Login Info").child("18EL003").child("Subject").child("SA").getValue().toString();
                        Marks.setText(score);

                    }else{
                        pd = new ProgressDialog(Assignment.this);
                        pd.setMessage("Uploading....");

                        Upload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(filePath != null) {
                                    pd.show();

                                    StorageReference childRef = storageRef.child( assignment + "" + "18EL003");

                                    //uploading the image
                                    UploadTask uploadTask = childRef.putFile(filePath);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            pd.dismiss();
                                            Toast.makeText(Assignment.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(Assignment.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(Assignment.this, "Select File Please", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                }else{
                    Toast.makeText(Assignment.this, "There is no upcoming Assignment", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub_menu,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_assignment:
                Intent intent = new Intent(Assignment.this,Assignment.class);
                startActivity(intent);
                break;
            case R.id.nav_subject:
                Intent intent2 = new Intent(Assignment.this,Third.class);
                startActivity(intent2);
                break;
            case R.id.nav_quiz:
                Intent intent3 = new Intent(Assignment.this,Quiz.class);
                startActivity(intent3);
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(Assignment.this,Profile.class);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                Intent intent5 = new Intent(Assignment.this,MainActivity.class);
                startActivity(intent5);
                break;


        }

        return true;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

    }


    /*****************PDF upload functions*********************/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}



