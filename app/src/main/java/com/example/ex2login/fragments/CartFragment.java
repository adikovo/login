package com.example.ex2login.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ex2login.CustomeAdapter;
import com.example.ex2login.R;
import com.example.ex2login.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class CartFragment extends Fragment implements CustomeAdapter.OnProductListener {
    private RecyclerView recyclerView;
    private CustomeAdapter adapter;
    private ArrayList<Product> products;
    private TextView welcomeText;
    private Button addProductButton;
    private Button removeProductButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userCartRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        String username = mAuth.getCurrentUser().getEmail().replace("@yourdomain.com", "");
        userCartRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("cart");

        // Initialize views
        welcomeText = view.findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, " + username);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        products = new ArrayList<>();
        adapter = new CustomeAdapter(products, this);
        recyclerView.setAdapter(adapter);

        addProductButton = view.findViewById(R.id.buttonAddProduct);
        removeProductButton = view.findViewById(R.id.buttonRemoveProduct);

        addProductButton.setOnClickListener(v -> showAddProductDialog());
        removeProductButton.setOnClickListener(v -> removeAllProducts());

        // Load products from Firebase
        loadProducts();

        return view;
    }

    private void showAddProductDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_product);

        EditText productNameInput = dialog.findViewById(R.id.editTextProductName);
        EditText quantityInput = dialog.findViewById(R.id.editTextQuantity);
        Button cancelButton = dialog.findViewById(R.id.buttonCancel);
        Button addButton = dialog.findViewById(R.id.buttonAdd);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        addButton.setOnClickListener(v -> {
            String productName = productNameInput.getText().toString().trim();
            String quantityStr = quantityInput.getText().toString().trim();

            if (productName.isEmpty()) {
                productNameInput.setError("Product name is required");
                return;
            }

            if (quantityStr.isEmpty()) {
                quantityInput.setError("Quantity is required");
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                quantityInput.setError("Quantity must be greater than 0");
                return;
            }

            addProduct(new Product(productName, quantity));
            dialog.dismiss();
        });

        dialog.show();
    }

    private void loadProducts() {
        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        products.add(product);
                    }
                }
                adapter.updateProducts(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load products: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProduct(Product product) {
        userCartRef.child(product.getName()).setValue(product)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), 
                    "Product added successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), 
                    "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void removeAllProducts() {
        userCartRef.removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), 
                    "All products removed", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), 
                    "Failed to remove products: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onIncreaseClick(int position) {
        Product product = products.get(position);
        product.setQuantity(product.getQuantity() + 1);
        userCartRef.child(product.getName()).setValue(product);
    }

    @Override
    public void onDecreaseClick(int position) {
        Product product = products.get(position);
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
            userCartRef.child(product.getName()).setValue(product);
        } else {
            userCartRef.child(product.getName()).removeValue();
        }
    }

    @Override
    public void onDeleteClick(int position) {
        Product product = products.get(position);
        userCartRef.child(product.getName()).removeValue();
    }
} 