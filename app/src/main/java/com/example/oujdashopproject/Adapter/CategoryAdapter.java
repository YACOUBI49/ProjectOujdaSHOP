package com.example.oujdashopproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.oujdashopproject.R;
import com.example.oujdashopproject.Users.Category;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categoryList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        }

        Category category = categoryList.get(position);
        ShapeableImageView imageView = convertView.findViewById(R.id.listImage);
        TextView nameTextView = convertView.findViewById(R.id.nom);
        TextView descriptionTextView = convertView.findViewById(R.id.description);
        nameTextView.setText(category.getNom());
        descriptionTextView.setText(category.getDescription());
        imageView.setImageResource(R.mipmap.category);

        return convertView;
    }
}
