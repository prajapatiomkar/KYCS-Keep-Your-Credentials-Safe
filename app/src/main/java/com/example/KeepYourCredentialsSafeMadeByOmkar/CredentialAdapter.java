package com.example.KeepYourCredentialsSafeMadeByOmkar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CredentialAdapter extends FirebaseRecyclerAdapter<Credential, CredentialAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CredentialAdapter(@NonNull FirebaseRecyclerOptions<Credential> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Credential credential) {
        holder.view_title.setText(credential.getTitle());
        holder.view_user_email_id.setText(credential.getAccount());
        holder.view_user_password.setText(credential.getPassword());
        holder.view_date.setText(credential.getDate());
        holder.credential_item_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.view_title.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 800)
                        .create();

//
                View view1 = dialogPlus.getHolderView();
                EditText title = view1.findViewById(R.id.update_title_field);
                EditText account = view1.findViewById(R.id.update_account_field);
                EditText password = view1.findViewById(R.id.update_password_field);
                Button btnUpdate = view1.findViewById(R.id.updateBtn);

                title.setText(credential.getTitle());
                account.setText(credential.getAccount());
                password.setText(credential.getPassword());
                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String date = dtf.format(now);
                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title.getText().toString());
                        map.put("account", account.getText().toString());
                        map.put("password", password.getText().toString());
                        map.put("date", date);
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.view_title.getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.view_title.getContext(), "Error", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });


                return true;
            }
        });
       holder.delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.view_title.getContext());
                builder.setTitle("Are you sure?");
                builder.setCancelable(false);
                builder.setMessage("Deleted data can't be Undo");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.view_title.getContext(), "Cancel", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credential_item_view, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView view_title, view_user_email_id, view_user_password, view_date;
        CardView credential_item_card;
        ImageView delete_card;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            view_title = itemView.findViewById(R.id.view_title);
            view_user_email_id = itemView.findViewById(R.id.view_user_email_id);
            view_user_password = itemView.findViewById(R.id.view_user_password);
            view_date = itemView.findViewById(R.id.view_date);
            credential_item_card = itemView.findViewById(R.id.credential_item_card);
            delete_card = itemView.findViewById(R.id.delete_card);
        }
    }
}
