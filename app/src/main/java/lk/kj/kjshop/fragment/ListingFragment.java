package lk.kj.kjshop.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.List;

import lk.kj.kjshop.R;
import lk.kj.kjshop.adapter.ListingAdapter;
import lk.kj.kjshop.adapter.ProductSliderAdapter;
import lk.kj.kjshop.databinding.FragmentListingBinding;
import lk.kj.kjshop.model.Category;
import lk.kj.kjshop.model.Product;


public class ListingFragment extends Fragment {


    private FragmentListingBinding binding;


    private ListingAdapter adapter;

    private String categoryId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewListing.setLayoutManager(new GridLayoutManager(getContext(), 2));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        Product p1 = new Product("prod1", "TVs", "Television 50inch 4K", 200000, "cat1", Arrays.asList("","",""), 30);
//        Product p2 = new Product("prod2", "Fans", "Table Fan with 3-speed", 10000, "cat1", Arrays.asList("", "", ""), 30);
//        Product p3 = new Product("prod3", "Switches", "4 switches in one panel", 1200, "cat2", Arrays.asList("", "", ""), 20);
//
//
//
//
//        List<Product> prods = List.of(p1, p2, p3);
//
//
//        WriteBatch batch = db.batch();
//
//        for (Product p : prods) {
//            DocumentReference ref = db.collection("products").document();
//
//            batch.set(ref, p);
//        }
//
//
//        batch.commit();

        db.collection("products").whereEqualTo("categoryId", categoryId)
                .orderBy("title", Query.Direction.ASCENDING).get().addOnSuccessListener(ds -> {
                    if (!ds.isEmpty()) {
                        List<Product> products = ds.toObjects(Product.class);
                        adapter = new ListingAdapter(products, product -> {


                            Bundle bundle = new Bundle();
                            bundle.putString("productId", product.getProductId());

                            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                            productDetailsFragment.setArguments(bundle);

                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, productDetailsFragment)
                                    .addToBackStack(null)
                                    .commit();
                        });

                        binding.recyclerViewListing.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error:" + e.getMessage());
                    }
                });

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}