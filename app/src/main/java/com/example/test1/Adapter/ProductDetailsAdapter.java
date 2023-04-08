package com.example.test1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.viewmodels.EnterProductDetails;
import com.squareup.picasso.Picasso;

import java.util.List;


public class
ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ProductViewHolder> {
   private Context mContext;
    private List<EnterProductDetails> mUploads;

    public ProductDetailsAdapter(Context context, List<EnterProductDetails> uploads){

        mContext = context;
        mUploads =uploads;

    }
    @NonNull
    @Override
    public ProductDetailsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View v = LayoutInflater.from(mContext).inflate(R.layout.activity_main, parent,false);

       return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {



        EnterProductDetails uploadDetails = mUploads.get(position);
        holder.productNameTxt.setText(uploadDetails.getTitleDB());
        holder.priceTxt.setText(uploadDetails.getPriceDB());
       Picasso.get()
                .load(uploadDetails.getImageurlDB())
                .placeholder(R.drawable.ic_launcher_background)
                .fit().centerCrop()
                .into(holder.productImage);




    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        public TextView productNameTxt, priceTxt;
        public ImageView productImage;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTxt = itemView.findViewById(R.id.productNameTxt);
            priceTxt =  itemView.findViewById((R.id.priceTxt));
            productImage =itemView.findViewById(R.id.productImage);



        }
    }

}
