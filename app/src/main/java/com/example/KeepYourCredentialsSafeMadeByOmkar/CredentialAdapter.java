package com.example.KeepYourCredentialsSafeMadeByOmkar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.zip.Inflater;

public class CredentialAdapter extends ArrayAdapter<Credential> {
    public CredentialAdapter(@NonNull Context context, @NonNull List<Credential> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        Credential currentCredential = getItem(position);
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.credential_item_view, parent, false);
        }

        TextView view_title = listItemView.findViewById(R.id.view_title);
        view_title.setText(currentCredential.getTitle());

        TextView view_user_email_id = listItemView.findViewById(R.id.view_user_email_id);
        view_user_email_id.setText(currentCredential.getUserEmailId());

        TextView view_user_password = listItemView.findViewById(R.id.view_user_password);
        view_user_password.setText(currentCredential.getPassword());

        TextView view_date = listItemView.findViewById(R.id.view_date);
        view_date.setText(currentCredential.getDate());

        return listItemView;
    }
}
