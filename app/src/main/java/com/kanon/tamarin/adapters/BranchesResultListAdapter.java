package com.kanon.tamarin.adapters;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kanon.tamarin.AcademyCategoryBranchEditActivity;
import com.kanon.tamarin.AcademyDetailsActivity;
import com.kanon.tamarin.EditProfileActivity;
import com.kanon.tamarin.R;
import com.kanon.tamarin.models.AcademyCategoryBranch;
import com.kanon.tamarin.models.BranchAcademy;
import com.kanon.tamarin.models.Categories;
import com.kanon.tamarin.models.Locations;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BranchesResultListAdapter extends RecyclerView.Adapter<BranchesResultListAdapter.BranchResultListViewHolder> {

    public static class BranchResultListViewHolder extends RecyclerView.ViewHolder {

        TextView rv_item_branch_address;
        TextView rv_item_branch_mobile;
        Button btnEditBranch, btnDeleteBranch;

        public BranchResultListViewHolder(View itemView) {
            super(itemView);
            this.rv_item_branch_address = itemView.findViewById(R.id.rv_item_branch_address);
            this.rv_item_branch_mobile = itemView.findViewById(R.id.rv_item_branch_mobile);
            this.btnEditBranch = itemView.findViewById(R.id.branchEditBTN);
            this.btnDeleteBranch = itemView.findViewById(R.id.branchDeleteBTN);
        }
    }

    private List<BranchAcademy> branchAcademyList;
    private List<Locations> locationsList;
    private List<Categories> categoriesList;

    public BranchesResultListAdapter(List<BranchAcademy> branchAcademyList, List<Locations> locationsList, List<Categories> categoriesList) {
        this.branchAcademyList = branchAcademyList;
        this.locationsList = locationsList;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public BranchResultListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.branch_result_list_item, parent, false);

        BranchResultListViewHolder myViewHolder = new BranchResultListViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BranchResultListViewHolder holder, int position) {

        TextView rv_item_academy_name = holder.rv_item_branch_address;
        TextView rv_item_mobile = holder.rv_item_branch_mobile;

        rv_item_academy_name.setText(branchAcademyList.get(position).getAddress());
        rv_item_mobile.setText(branchAcademyList.get(position).getMobile());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BranchAcademy branchAcademy = branchAcademyList.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.itemView.getContext(), AcademyCategoryBranchEditActivity.class);
                intent.putExtra("currentBranch", branchAcademy);
                intent.putParcelableArrayListExtra("allLocations", (ArrayList<? extends Parcelable>) locationsList);
                intent.putParcelableArrayListExtra("allCategories", (ArrayList<? extends Parcelable>) categoriesList);

                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.btnEditBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditProfileActivity) holder.itemView.getContext()).OpenEditDialogForBranch(branchAcademyList.get(holder.getAdapterPosition()));
            }
        });

        holder.btnDeleteBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditProfileActivity) holder.itemView.getContext()).OpenDeleteDialogForBranch(branchAcademyList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return branchAcademyList.size();
    }
}
