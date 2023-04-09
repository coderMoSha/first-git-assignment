package com.example.test1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.activity.ProductPage;
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
     View v = LayoutInflater.from(mContext).inflate(R.layout.activity_main_dashboard, parent,false);

       return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {



        EnterProductDetails uploadDetails = mUploads.get(position);
        holder.productNameTxt.setText(uploadDetails.getTitleDB());
        holder.priceTxt.setText("£"+uploadDetails.getPriceDB());
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



    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView productNameTxt, priceTxt;

        public TextView showTitle,showDescripition,showPrice,showCondition,showLocation;
        public ImageView productImage,showProductImage;



        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTxt = itemView.findViewById(R.id.productNameTxt);
            priceTxt =  itemView.findViewById((R.id.priceTxt));
            productImage =itemView.findViewById(R.id.productImage);
            // set OnClickListener for the entire view



            showTitle =itemView.findViewById(R.id.showTitle);
            showDescripition = itemView.findViewById(R.id.showDescription);
            showPrice = itemView.findViewById(R.id.showPrice);
            showCondition = itemView.findViewById(R.id.showCondition);
            showLocation = itemView.findViewById(R.id.showLocation);
            showProductImage = itemView.findViewById(R.id.showProductImage);
            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            // get the position of the clicked item
            int position = getAdapterPosition();

            // check if the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get the clicked product
                EnterProductDetails uploadDetails = mUploads.get(position);

                // create an intent to start the ProductDetailActivity
                Intent intent = new Intent(v.getContext(), ProductPage.class);

                // pass the product details to the intent as extra data
                intent.putExtra("title", uploadDetails.getTitleDB());
                intent.putExtra("imageUrl", uploadDetails.getImageurlDB());
                intent.putExtra("price",uploadDetails.getPriceDB());
                intent.putExtra("location",uploadDetails.getLocationDB());
                intent.putExtra("description",uploadDetails.getDescriptionDB());
                intent.putExtra("condition",uploadDetails.getConditionDB());

                // start the activity
                v.getContext().startActivity(intent);

            }
            }
        public void bind(EnterProductDetails uploadDetails) {
            showTitle.setText(uploadDetails.getTitleDB());
            showDescripition.setText(uploadDetails.getDescriptionDB());
            showPrice.setText(uploadDetails.getPriceDB());
            showCondition.setText(uploadDetails.getConditionDB());
            showLocation.setText(uploadDetails.getLocationDB());
            Picasso.get().load(uploadDetails.getImageurlDB()).into(showProductImage);

        }
    }

}





