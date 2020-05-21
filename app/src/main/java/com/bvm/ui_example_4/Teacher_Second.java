package com.bvm.ui_example_4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Teacher_Second extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static TextView OK, Cancel, Add;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ImageButton button;
    LinearLayout  rootView;
    View MainScreen;
    Boolean answered = false;
    EditText Question;
    int answerNr, biased1 = 0, QuestionCounter=0, id=100, AddCounter = 0, i =1, var = 0, j = 0,optioncounter = 0, counter  = 0, QuestionList = 0;
    //QuestionList is for Number of Question

    RadioGroup rg, rg1;
    RelativeLayout  Dialogbox, DisplayQuestion;
    RadioButton rb1, rb2;
    List<EditText> etList = new ArrayList<EditText>();
    List<Button> btList = new ArrayList<Button>();
    List<RadioButton> rbList = new ArrayList<RadioButton>();
    String[] Questions = new String[100];
    String[] options = new String[100];
    Integer[] counterarray = new Integer[100];
    Integer[] answer = new Integer[100];
    Button ForDatabase;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_add);
        button = findViewById(R.id.more);
        Dialogbox = findViewById(R.id.RelativeLayoutadd);
        MainScreen = findViewById(R.id.drawer_layout);
        OK = findViewById(R.id.OK);
        Cancel = findViewById(R.id.cancel);
        Question = findViewById(R.id.getQuestion);
        rg = findViewById(R.id.RadioGroup);
        rootView = findViewById(R.id.Questions);  //Elements are to be added here
        Add = findViewById(R.id.Addoptions);
        DisplayQuestion = findViewById(R.id.display_question);
        rg1 = findViewById(R.id.RadioGroup1);
        ForDatabase = findViewById(R.id.AddtoDatabase);

        MainScreen.setVisibility(View.VISIBLE);
        Dialogbox.setVisibility(View.GONE);
        DisplayQuestion.setVisibility(View.GONE);
        ForDatabase.setVisibility(View.VISIBLE);
        counterarray[counter] = 0;
        answer[0] = 0;
        counter++;

        //Adding first Radio button to dialog box

        Radiobuttonaddfunc();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupmenu = new PopupMenu(Teacher_Second.this, button);
                popupmenu.getMenuInflater().inflate(R.menu.sub_menu1, popupmenu.getMenu());

                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(Teacher_Second.this, "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        if (menuItem.getItemId() == R.id.add_que) {
                            Dialogbox.setVisibility(View.VISIBLE);
                            MainScreen.setVisibility(View.INVISIBLE);
                            ForDatabase.setVisibility(View.GONE);

                        } else if (menuItem.getItemId() == R.id.del_que) {
                            MainScreen.setVisibility(View.VISIBLE);
                            ForDatabase.setVisibility(View.VISIBLE);

                        }
                        return true;

                    }
                });
                popupmenu.show();
            }
        });

        //---------OK and Cancel clickable button

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCondition()) {
                    QuestionList++;
                    Questions[QuestionList] = Question.getText().toString();
                    RadioButton rbSelected = findViewById(rg.getCheckedRadioButtonId());
                    answerNr = rg.indexOfChild(rbSelected) + 1;
                    answer[QuestionList] = answerNr ;


                    Log.i("Optioncounter", Integer.toString(optioncounter));
                    counterarray[counter] = optioncounter;
                    counter++;
                    AddBlockstoLayout();
                    Refreshfornextform();
                    Question.setText(null);
                    biased1 = 0;
                    QuestionCounter = 0;
                    Radiobuttonaddfunc();
                    MainScreen.setVisibility(View.VISIBLE);
                    Dialogbox.setVisibility(View.GONE);
                    ForDatabase.setVisibility(View.VISIBLE);

                }
            }

        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Radiobuttonaddfunc();

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg.clearCheck();
                Refreshfornextform();
                Radiobuttonaddfunc();
                MainScreen.setVisibility(View.VISIBLE);
                Dialogbox.setVisibility(View.GONE);
                ForDatabase.setVisibility(View.VISIBLE);

            }
        });


        ForDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int k = 0;
                for(int a = counterarray[0]; a< counterarray[QuestionList] ;a++){
                    options[k] =  etList.get(a).getText().toString();
                    k++;
                }
                k = 0;
                for(int l = 0; l <QuestionList;l++)
                {
                    k++;

                    databaseaddFunc(k);
                }





            }

            private void databaseaddFunc(int question) {
                DatabaseReference myref = FirebaseDatabase.getInstance().getReference();

                int  k = 1;
                //myref.child("Subject").child("Quiz").child("Question " + Integer.toString(question)).child("Question").child("Que").setValue(Questions[0]);
                for(int l = counterarray[question - 1]; l<counterarray[question];l++){
                    myref.child("Subject").child("Quiz").child("Question " + question).child("option " + k).setValue(options[l]);
                    k++;
                }
                myref.child("Subject").child("Quiz").child("Question " + question).child("ans").setValue(answer[question]);

                myref.child("Subject").child("question").setValue(QuestionList);
                myref.child("Subject").child("Quiz").child("Question " + question).child("Que").setValue(Questions[question]);
            }
        });

















        //Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Teacher_Second.this, drawerLayout, toolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);




    }







    private void Refreshfornextform() {
        int a = 0;
        for (a=0; a< AddCounter ; a++){
            Dialogbox.removeViewAt(Dialogbox.getChildCount() - 1);
        }
        AddCounter = 0;
        rg.removeAllViews();

    }

    private void AddBlockstoLayout() {

        int[] images = {R.drawable.orange_tree,R.drawable.pink_tree,R.drawable.colorful_tree,R.drawable.purple_tree,R.drawable.brown_tree};
        Random rand = new Random();
        Button button = new Button(Teacher_Second.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setText("Question " + i);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 70);
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setTextColor(Color.BLACK);
        button.setBackgroundResource(images[rand.nextInt(images.length)]);
        i++;

        params.leftMargin= 0;
        params.rightMargin= 0;
        params.topMargin= 0;
        params.height=450;

        button.setLayoutParams(params);
        rootView.addView(button, params);
        btList.add(button);



    }

    private void Radiobuttonaddfunc() {
        AddCounter++;
        optioncounter++;
        rb1 = new RadioButton(this);
        rb1.setText(" ");
        rg.addView(rb1);
        rbList.add(rb1);
        rg.setOrientation(RadioGroup.VERTICAL);
        EditText editText = new EditText(Teacher_Second.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, rb1.getId() );
        params.addRule(RelativeLayout.BELOW, R.id.getQuestion);
        params.leftMargin = ((RadioGroup.LayoutParams) rb1.getLayoutParams()).leftMargin + 90;
        params.topMargin = ((RadioGroup.LayoutParams) rb1.getLayoutParams()).topMargin + biased1;
        QuestionCounter++;
        biased1 += 90;
        editText.setId(id);
        id++;



        Toast.makeText(Teacher_Second.this, Integer.toString(id), Toast.LENGTH_SHORT).show();
        editText.setTextSize(15);
        editText.setTextColor(Color.BLACK);
        editText.setHint("option "+ QuestionCounter);
        editText.setLayoutParams(params);
        Dialogbox.addView(editText);
        etList.add(editText);
    }

    private boolean checkCondition() {
        if(!((Question.getText().toString()).isEmpty())){
            return !(answerNr == -1);
        }else{
            return false;
        }

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_assignment:
                Intent intent = new Intent(Teacher_Second.this, Teacher_assignment.class);
                startActivity(intent);
                break;
            case R.id.nav_subject:
                Intent intent2 = new Intent(Teacher_Second.this, Teacher_Subjects.class);
                startActivity(intent2);
                break;
            case R.id.nav_quiz:
                Intent intent3 = new Intent(Teacher_Second.this, Teacher_Second.class);
                startActivity(intent3);
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(Teacher_Second.this, Profile.class);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                Intent intent5 = new Intent(Teacher_Second.this, MainActivity.class);
                startActivity(intent5);
                break;


        }

        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }



}

