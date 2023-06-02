package com.kanon.tamarin.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kanon.tamarin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditProfileImagesAdapter extends RecyclerView.Adapter<EditProfileImagesAdapter.ViewHolder> {

    ArrayList<String> imagesList;

    public EditProfileImagesAdapter(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public EditProfileImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.edit_profile_image_select_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditProfileImagesAdapter.ViewHolder holder, int position) {

        if (imagesList.get(position).toString().startsWith("https://"))
            Picasso.get().load(imagesList.get(position).toString()).into(holder.img);
        else
            holder.img.setImageURI(Uri.parse(imagesList.get(position)));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagesList.remove(imagesList.get(holder.getAdapterPosition()));
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public ArrayList<String> GetImagesListForSave()
    {
        return imagesList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img, delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.edit_profile_img);
            delete = itemView.findViewById(R.id.edit_profile_img_delete);
        }
    }
}
