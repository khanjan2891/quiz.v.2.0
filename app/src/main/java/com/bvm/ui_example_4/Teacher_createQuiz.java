package com.bvm.ui_example_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class Teacher_createQuiz extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton button;
    Button cancel, cancel1;
    View add, del, quiz;
    private ViewGroup rootView;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_createquiz);
        button = findViewById(R.id.more);
        add = findViewById(R.id.add_quiz);
        del = findViewById(R.id.del_layout);
        rootView = findViewById(R.id.quiz);
        quiz= findViewById(R.id.quiz);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupmenu = new PopupMenu(Teacher_createQuiz.this, button);
                popupmenu.getMenuInflater().inflate(R.menu.sub_menu2, popupmenu.getMenu());

                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(Teacher_createQuiz.this, "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        if (menuItem.getItemId() == R.id.create_quiz){
                            add.setVisibility(View.VISIBLE);
                           quiz.setVisibility(View.INVISIBLE);

                        } else if (menuItem.getItemId() == R.id.del_quiz) {
                            del.setVisibility(View.VISIBLE);

                        }
                        return true;

                    }
                });
                popupmenu.show();

            }
        });



        /*-----hooks-----*/
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.quiz_toolbar);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_assignment:
                Intent intent = new Intent(Teacher_createQuiz.this, Teacher_assignment.class);
                startActivity(intent);
                break;
            case R.id.nav_subject:
                Intent intent2 = new Intent(Teacher_createQuiz.this, Teacher_Subjects.class);
                startActivity(intent2);
                break;
            case R.id.nav_quiz:
                Intent intent3 = new Intent(Teacher_createQuiz.this, quiz_main.class);
                startActivity(intent3);
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(Teacher_createQuiz.this, Profile.class);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                Intent intent5 = new Intent(Teacher_createQuiz.this, MainActivity.class);
                startActivity(intent5);
                break;


        }

        return true;
    }

    public void cancel(View view) {
        cancel = findViewById(R.id.cancel);
        cancel1 = findViewById(R.id.cancel1);
        add = findViewById(R.id.add_quiz);
        add.setVisibility(View.INVISIBLE);
        quiz.setVisibility(View.VISIBLE);


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


    public void addbutton(View view){
       quiz.setVisibility(View.VISIBLE);
        EditText quizname= findViewById(R.id.quizname);
        Button button1 = new Button(Teacher_createQuiz.this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int[] images = {R.drawable.orange_tree,R.drawable.pink_tree,R.drawable.colorful_tree,R.drawable.purple_tree,R.drawable.brown_tree};
        Random rand = new Random();
        button1.setBackgroundResource(images[rand.nextInt(images.length)]);
        button1.setText(quizname.getText().toString());
        button1.setTextSize(TypedValue.COMPLEX_UNIT_PX, 70);
        button1.setGravity(Gravity.CENTER_VERTICAL);
        button1.setTextColor(Color.BLACK);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Teacher_createQuiz.this, Teacher_Second.class);
                startActivity(intent);
            }
        });

        params.leftMargin= 0;
        params.rightMargin= 0;
        params.topMargin=10;
        params.height=450;

        button1.setLayoutParams(params);


        rootView.addView(button1, params);
        cancel(view);

    }
}
