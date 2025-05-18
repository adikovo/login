package com.example.ex2login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ex2login.models.Product;
import java.util.ArrayList;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder> {

    private ArrayList<Product> products;
    private OnProductListener onProductListener;

    public interface OnProductListener {
        void onIncreaseClick(int position);
        void onDecreaseClick(int position);
        void onDeleteClick(int position);
    }

    public CustomeAdapter(ArrayList<Product> products, OnProductListener onProductListener) {
        this.products = products;
        this.onProductListener = onProductListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView quantity;
        ImageButton btnIncrease;
        ImageButton btnDecrease;
        ImageButton btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textProductName);
            quantity = itemView.findViewById(R.id.textQuantity);
            btnIncrease = itemView.findViewById(R.id.buttonIncrease);
            btnDecrease = itemView.findViewById(R.id.buttonDecrease);
            btnDelete = itemView.findViewById(R.id.buttonDelete);

            btnIncrease.setOnClickListener(v -> {
                if (onProductListener != null) {
                    onProductListener.onIncreaseClick(getAdapterPosition());
                }
            });

            btnDecrease.setOnClickListener(v -> {
                if (onProductListener != null) {
                    onProductListener.onDecreaseClick(getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (onProductListener != null) {
                    onProductListener.onDeleteClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.quantity.setText(String.valueOf(product.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(ArrayList<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }
}
