package com.bvm.ui_example_4;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExampleDialog extends AppCompatDialogFragment {
    private static Boolean decide;
    private static EditText current_password, email_here, confrim_email;
    private  static TextView update, confrim_email_text;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        decide = Profile.getButtonpressed();



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater  = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);



        builder.setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {



            @Override
            public void onClick(DialogInterface dialog, int which) {

                String psd = MainActivity.getPassword1();
                if((confrim_email.getText().toString()).equals((email_here.getText().toString()))) {
                    if ((current_password.getText().toString()).equals(psd)) {
                        String email = email_here.getText().toString();
                        String usr = MainActivity.getUsername1();
                        DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
                        String var = "contact";
                        if (decide == Boolean.FALSE) {
                            var = "mail";
                        }
                        myref.child("Login Info").child(usr).child(var).setValue(email);

                    }
                }






            }


        });
        current_password = view.findViewById(R.id.current_password);
        email_here = view.findViewById(R.id.email_here);
        update = view.findViewById(R.id.update);
        confrim_email = view.findViewById(R.id.email_here2);
        confrim_email_text = view.findViewById(R.id.update2);



        if(decide == Boolean.TRUE){
            update.setText("New Contact info");
            //update.setText("New Email");
            confrim_email_text.setText("Confirm Contact Number");

        }else{
            update.setText("New Email");
            confrim_email_text.setText("Confirm Email");
        }

        return builder.create();

    }

}
