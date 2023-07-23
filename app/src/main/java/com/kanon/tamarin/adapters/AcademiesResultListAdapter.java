package com.kanon.tamarin.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.kanon.tamarin.AcademyDetailsActivity;
import com.kanon.tamarin.R;
import com.kanon.tamarin.models.AcademyCategoryBranch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AcademiesResultListAdapter extends RecyclerView.Adapter<AcademiesResultListAdapter.AcademyResultListViewHolder> {

    public static class AcademyResultListViewHolder extends RecyclerView.ViewHolder {

        TextView rv_item_academy_name;
        TextView rv_item_mobile;
        TextView rv_item_rate_per_session;
        ShapeableImageView rv_item_profile_pic;

        public AcademyResultListViewHolder(View itemView) {
            super(itemView);
            this.rv_item_academy_name = itemView.findViewById(R.id.rv_item_academy_name);
            this.rv_item_mobile = itemView.findViewById(R.id.rv_item_mobile);
            this.rv_item_rate_per_session = itemView.findViewById(R.id.rv_item_rate_per_session);
            this.rv_item_profile_pic = itemView.findViewById(R.id.rv_item_profile_pic);
        }
    }

    private List<AcademyCategoryBranch> academiesArrayList;

    public AcademiesResultListAdapter(List<AcademyCategoryBranch> academiesArrayList) {
        this.academiesArrayList = academiesArrayList;
    }

    @NonNull
    @Override
    public AcademyResultListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.academy_result_list_item, parent, false);

        AcademyResultListViewHolder myViewHolder = new AcademyResultListViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcademyResultListViewHolder holder, int position) {

        TextView rv_item_academy_name = holder.rv_item_academy_name;
        TextView rv_item_mobile = holder.rv_item_mobile;
        TextView rv_item_rate_per_session = holder.rv_item_rate_per_session;
        ImageView rv_item_profile_pic = holder.rv_item_profile_pic;

        rv_item_academy_name.setText(academiesArrayList.get(position).getAcademy().getAcademyName());
        rv_item_mobile.setText(academiesArrayList.get(position).getLocation().getLocationName());

        if (academiesArrayList.get(position).getRatePerSession() > 1)
            if (academiesArrayList.get(position).getSessions() != null && academiesArrayList.get(position).getSessions() > 0)
                rv_item_rate_per_session.setText("EGP " + academiesArrayList.get(position).getRatePerSession().toString() + "\n Per " + academiesArrayList.get(position).getSessions() +" Session");
            else
                rv_item_rate_per_session.setText("EGP " + academiesArrayList.get(position).getRatePerSession().toString());
        else
            rv_item_rate_per_session.setText("");

        if (!academiesArrayList.get(position).getAcademy().getProfilePic().isEmpty())
            Picasso.get().load(academiesArrayList.get(position).getAcademy().getProfilePic()).into(rv_item_profile_pic);
        else
            rv_item_profile_pic.setImageResource(R.mipmap.ic_launcher);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcademyCategoryBranch academyCategoryBranch = academiesArrayList.get(holder.getAdapterPosition());
                /*if(academyCategoryBranch.getAcademy().getAcademyName().contentEquals("Salah Academy 2"))
                {
                    AcademyCategoryBranchFirestoreManager academyCategoryBranchFirestoreManager = AcademyCategoryBranchFirestoreManager.academyCategoryBranchNewInstance();
                    academyCategoryBranch.setRatePerSession(350);
                    academyCategoryBranchFirestoreManager.createDocument(academyCategoryBranch);
                }*/

                Intent intent = new Intent(holder.itemView.getContext(), AcademyDetailsActivity.class);
                /*Bundle b = new Bundle();
                b.putSerializable("currentAcademy", academyCategoryBranch);
                intent.putExtras(b);*/
                intent.putExtra("currentAcademy", academyCategoryBranch);

                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return academiesArrayList.size();
    }




}
