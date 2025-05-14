package com.example.ex2login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2login.models.DataModel;

import java.util.ArrayList;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataset;
    public CustomeAdapter(ArrayList<DataModel> dataset){ this.dataset = dataset; }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView){
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewRes);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewVersion = itemView.findViewById(R.id.textViewVersion);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v){
                        int position = getAdapterPosition();
                        DataModel clickedItem = dataset.get(position);
                        Toast.makeText(v.getContext(), clickedItem.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public CustomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter.MyViewHolder holder, int position){
        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataset.get(position).getName());
        textViewVersion.setText(dataset.get(position).getVersion());
        imageView.setImageResource(dataset.get(position).getImage());
    }

    @Override
    public int getItemCount() {return dataset.size(); }
}
