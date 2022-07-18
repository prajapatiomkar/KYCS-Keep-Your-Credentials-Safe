package com.example.KeepYourCredentialsSafeMadeByOmkar;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class CredentialAdapter extends FirebaseRecyclerAdapter<Credential, CredentialAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    View mView;
    SecurityPipeline securityPipeline;
    private FirebaseAuth mAuth;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt biometricPrompt1;
    private BiometricPrompt.PromptInfo promptInfo;


    public CredentialAdapter(@NonNull FirebaseRecyclerOptions<Credential> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Credential credential) {
        securityPipeline = new SecurityPipeline();
        mAuth = FirebaseAuth.getInstance();
        holder.view_user_password.setText("Tap to see you password");
        // Biometric //

        executor = ContextCompat.getMainExecutor(holder.view_title.getContext());


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using finger Auth")
                .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL | BIOMETRIC_WEAK)
                .build();
        // Biometric //

        try {
            holder.view_title.setText(securityPipeline.decrypt(credential.getTitle(), mAuth.getUid()));
            holder.view_user_email_id.setText(securityPipeline.decrypt(credential.getAccount(), mAuth.getUid()));
            holder.view_date.setText(securityPipeline.decrypt(credential.getDate(), mAuth.getUid()));


        } catch (Exception e) {

            Log.d("ERROR", e.toString());
        }


        holder.credential_item_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                biometricPrompt1 = new BiometricPrompt((FragmentActivity) holder.view_title.getContext(), executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(holder.view_title.getContext(), errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        final DialogPlus dialogPlus = DialogPlus.newDialog(holder.view_title.getContext())
                                .setContentHolder(new ViewHolder(R.layout.update_popup))
                                .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT)
                                .create();


                        View view1 = dialogPlus.getHolderView();
                        EditText title = view1.findViewById(R.id.update_title_field);
                        EditText account = view1.findViewById(R.id.update_account_field);
                        TextInputEditText password = view1.findViewById(R.id.update_password_field);
                        Button btnUpdate = view1.findViewById(R.id.updateBtn);
                        Button deleteBtn = view1.findViewById(R.id.deleteBtn);

                        try {
                            title.setText(securityPipeline.decrypt(credential.title, mAuth.getUid()));
                            account.setText(securityPipeline.decrypt(credential.account, mAuth.getUid()));
                            password.setText(securityPipeline.decrypt(credential.password, mAuth.getUid()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dialogPlus.show();

                        btnUpdate.setOnClickListener(new View.OnClickListener() {

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(View view) {
                                try {
                                    update();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                InputMethodManager imm = (InputMethodManager) holder.view_title.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            private void update() throws Exception {
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                LocalDateTime now = LocalDateTime.now();
                                String date = dtf.format(now);
                                Map<String, Object> map = new HashMap<>();
                                map.put("title", securityPipeline.encrypt(title.getText().toString(), mAuth.getUid()));
                                map.put("account", securityPipeline.encrypt(account.getText().toString(), mAuth.getUid()));
                                map.put("password", securityPipeline.encrypt(password.getText().toString(), mAuth.getUid()));
                                map.put("date", securityPipeline.encrypt(date, mAuth.getUid()));
                                FirebaseDatabase.getInstance().getReference().child(holder.mAuth.getUid())
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

                        deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delete();
                                InputMethodManager imm = (InputMethodManager) holder.view_title.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            }

                            private void delete() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.view_title.getContext());
                                builder.setTitle("Are you sure?");
                                builder.setCancelable(false);
                                builder.setMessage("Deleted data can't be Undo");

                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseDatabase.getInstance().getReference().child(holder.mAuth.getUid())
                                                .child(Objects.requireNonNull(getRef(position).getKey())).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialogPlus.dismiss();
                                                        Toast.makeText(holder.view_title.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
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

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
                biometricPrompt1.authenticate(promptInfo);
                return true;
            }

        });

        holder.view_user_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    biometricPrompt = new BiometricPrompt((FragmentActivity) holder.view_title.getContext(), executor, new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            Toast.makeText(holder.view_title.getContext(), errString, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            try {
                                holder.view_user_password.setText(securityPipeline.decrypt(credential.getPassword(), mAuth.getUid()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            Toast.makeText(holder.view_title.getContext(), "Fail to Verify", Toast.LENGTH_SHORT).show();

                        }
                    });
                    biometricPrompt.authenticate(promptInfo);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = parent;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credential_item_view, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        private final FirebaseAuth mAuth;
        TextView view_title, view_user_email_id, view_user_password, view_date;
        CardView credential_item_card;
        Button deleteBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            view_title = itemView.findViewById(R.id.view_title);
            view_user_email_id = itemView.findViewById(R.id.view_user_email_id);
            view_user_password = itemView.findViewById(R.id.view_user_password);
            view_date = itemView.findViewById(R.id.view_date);
            credential_item_card = itemView.findViewById(R.id.credential_item_card);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            mAuth = FirebaseAuth.getInstance();
            mView = itemView;


        }
    }

}
