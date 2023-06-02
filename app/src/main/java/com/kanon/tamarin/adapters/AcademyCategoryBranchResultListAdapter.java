package com.kanon.tamarin.adapters;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kanon.tamarin.AcademyCategoryBranchEditActivity;
import com.kanon.tamarin.EditProfileActivity;
import com.kanon.tamarin.R;
import com.kanon.tamarin.models.AcademyCategoryBranch;
import com.kanon.tamarin.models.BranchAcademy;
import com.kanon.tamarin.models.Categories;
import com.kanon.tamarin.models.Locations;

import java.util.ArrayList;
import java.util.List;

public class AcademyCategoryBranchResultListAdapter extends RecyclerView.Adapter<AcademyCategoryBranchResultListAdapter.AcademyCategoryBranchResultListViewHolder> {

    public static class AcademyCategoryBranchResultListViewHolder extends RecyclerView.ViewHolder {

        TextView rv_item_branch_address;
        TextView rv_item_branch_mobile;
        Button btnEdit, btnDelete;

        public AcademyCategoryBranchResultListViewHolder(View itemView) {
            super(itemView);
            this.rv_item_branch_address = itemView.findViewById(R.id.rv_item_branch_address);
            this.rv_item_branch_mobile = itemView.findViewById(R.id.rv_item_branch_mobile);
            this.btnEdit = itemView.findViewById(R.id.branchEditBTN);
            this.btnDelete = itemView.findViewById(R.id.branchDeleteBTN);
        }
    }

    private List<AcademyCategoryBranch> academyCategoryBranchList;
    private List<Locations> locationsList;
    private List<Categories> categoriesList;

    public AcademyCategoryBranchResultListAdapter(List<AcademyCategoryBranch> academyCategoryBranchList, List<Locations> locationsList, List<Categories> categoriesList) {
        this.academyCategoryBranchList = academyCategoryBranchList;
        this.locationsList = locationsList;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public AcademyCategoryBranchResultListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.branch_result_list_item, parent, false);

        AcademyCategoryBranchResultListViewHolder myViewHolder = new AcademyCategoryBranchResultListViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcademyCategoryBranchResultListViewHolder holder, int position) {

        TextView rv_item_academy_name = holder.rv_item_branch_address;
        TextView rv_item_mobile = holder.rv_item_branch_mobile;

        rv_item_academy_name.setText(academyCategoryBranchList.get(position).getCategory().getCategoryName());
        rv_item_mobile.setText(academyCategoryBranchList.get(position).getRatePerSession().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcademyCategoryBranch academyCategoryBranch = academyCategoryBranchList.get(holder.getAdapterPosition());

            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AcademyCategoryBranchEditActivity) holder.itemView.getContext()).OpenEditDialogForCategory(academyCategoryBranchList.get(holder.getAdapterPosition()));
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AcademyCategoryBranchEditActivity) holder.itemView.getContext()).OpenDeleteDialogForCategory(academyCategoryBranchList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return academyCategoryBranchList.size();
    }
}
