package com.example.test1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.test1.R;
import com.example.test1.activity.ProductPage;
import com.example.test1.viewmodels.EnterProductDetails;
import com.example.test1.viewmodels.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


    public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ProductViewHolder> {
   private Context mContext;
    private List<EnterProductDetails> mUploads;

    public ProductDetailsAdapter(Context context, List<EnterProductDetails> uploads){

        mContext = context;
        mUploads =uploads;

    }

        public void setmUploads(List<EnterProductDetails> mUploads) {
            this.mUploads = mUploads;
            notifyDataSetChanged();
        }

        @NonNull
    @Override
    public ProductDetailsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View v = LayoutInflater.from(mContext).inflate(R.layout.activity_main_dashboard, parent,false);

       return new ProductViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        EnterProductDetails uploadDetails = mUploads.get(position);
        List<Image> list=new ArrayList<>();
        if (uploadDetails.getImages()!=null) {
            list.addAll(uploadDetails.getImages().values());
        }
        holder.productNameTxt.setText(uploadDetails.getTitleDB());
        holder.priceTxt.setText("£ "+uploadDetails.getPriceDB());
        holder.productImage.setAdapter(new FirebaseImagesAdapter((ArrayList<Image>) list,mContext));

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView productNameTxt, priceTxt;
        public TextView showTitle,showDescripition,showPrice,showCondition,showLocation;
        public ImageView showProductImage;
        public ViewPager2 productImage;
        public LinearLayout llMain;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTxt = itemView.findViewById(R.id.productNameTxt);
            priceTxt =  itemView.findViewById((R.id.priceTxt));
            productImage =itemView.findViewById(R.id.productImage);
            llMain =itemView.findViewById(R.id.llMain);
            // set OnClickListener for the entire view
            showTitle =itemView.findViewById(R.id.showTitle);
            showDescripition = itemView.findViewById(R.id.showDescription);
            showPrice = itemView.findViewById(R.id.showPrice);
            showCondition = itemView.findViewById(R.id.showCondition);
            showLocation = itemView.findViewById(R.id.showLocation);
            showProductImage = itemView.findViewById(R.id.showProductImage);
            llMain.setOnClickListener(this);
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
                intent.putExtra("data",uploadDetails);
                // start the activity
                v.getContext().startActivity(intent);

            }
            }
    }

}





