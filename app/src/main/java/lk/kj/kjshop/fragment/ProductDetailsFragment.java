package lk.kj.kjshop.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import lk.kj.kjshop.R;
import lk.kj.kjshop.adapter.ProductSliderAdapter;
import lk.kj.kjshop.databinding.FragmentProductDetailsBinding;
import lk.kj.kjshop.model.Product;


public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;

    private String productId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString("productId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);

        // Load Product Details

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products")
                .whereEqualTo("productId", productId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot qds) {
                if (!qds.isEmpty()) {
                    Log.d("FIRESTORE_DEBUG", "Documents found: " + qds.size());

                    Product product = qds.getDocuments().get(0).toObject(Product.class);

                    ProductSliderAdapter adapter = new ProductSliderAdapter(product.getImages());
                    binding.productImageSlider.setAdapter(adapter);



                    binding.dotsIndicator.attachTo(binding.productImageSlider);

                    binding.productDetailsTitle.setText(product.getTitle());
                    binding.productDetailsRating.setRating(product.getRating());
                    binding.productDetailsPrice.setText("LKR " + product.getPrice());

                    binding.productDetailsAvbQty.setText(String.valueOf(product.getStockCount()));




                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override

            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error: " + e.getMessage());
            }
        });

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {


            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {

        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
    }

}