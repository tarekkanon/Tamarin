package com.kanon.tamarin.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.kanon.tamarin.AcademyCategoryBranchEditActivity;
import com.kanon.tamarin.EditProfileActivity;
import com.kanon.tamarin.R;
import com.kanon.tamarin.models.BranchAcademy;
import com.kanon.tamarin.models.Categories;
import com.kanon.tamarin.models.Locations;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<Categories> {

    private List<Categories> categoriesList;
    private Context context;

    public CategorySpinnerAdapter(Context context, int resourceId, List<Categories> categoriesList) {
        super(context,resourceId, categoriesList);
        this.categoriesList = categoriesList;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );

        View row=inflater.inflate(R.layout.category_custom_spinner_item, parent, false);

        TextView label = row.findViewById(R.id.category_spinner_text);
        ImageView thumb = row.findViewById(R.id.category_spinner_img);

        label.setText(categoriesList.get(position).getCategoryName());

        Picasso.get().load(categoriesList.get(position).getThumbnailImage()).into(thumb);

        return row;
    }
}
