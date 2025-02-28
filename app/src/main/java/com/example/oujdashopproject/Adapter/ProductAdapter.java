package com.example.oujdashopproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.oujdashopproject.R;
import com.example.oujdashopproject.Users.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cardview2, parent, false);
        }

        Product product = productList.get(position);
        TextView nameTextView = convertView.findViewById(R.id.nom);
        TextView descriptionTextView = convertView.findViewById(R.id.description);

        nameTextView.setText(product.getNom());
        descriptionTextView.setText(product.getDescription());

        return convertView;
    }
}