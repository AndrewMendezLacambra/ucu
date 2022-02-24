package com.ar.dev.ucubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.ar.dev.ucubs.Adapter.ProductAdapter;
import com.ar.dev.ucubs.Model.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryProductsActivity extends AppCompatActivity {

    private String category;

    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private List<ProductModel> productModelsList;

    private DatabaseReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        category = getIntent().getStringExtra(HomeActivity.CATEGORY_NAME);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" "+category);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        if(category.equals("1st Year 1st Sem"))
            actionBar.setLogo(R.drawable.icon_cart);
        else if(category.equals("1st Year 2nd Sem"))
            actionBar.setLogo(R.drawable.icon_cart);
        else if(category.equals("2nd Year 1st Sem"))
            actionBar.setLogo(R.drawable.icon_cart);
        else if(category.equals("2nd Year 2nd Sem"))
            actionBar.setLogo(R.drawable.icon_cart);
        else if(category.equals("3rd Year 1st Sem"))
            actionBar.setLogo(R.drawable.icon_cart);
        else if(category.equals("3rd Year 2nd Sem"))
            actionBar.setLogo(R.drawable.icon_cart);
        else if(category.equals("4th Year 1st Sem"))
            actionBar.setLogo(R.drawable.icon_cart);
        else if(category.equals("4th Year 2nd Sem"))
            actionBar.setLogo(R.drawable.icon_cart);

        databaseRef=FirebaseDatabase.getInstance().getReference("Products");

        productModelsList=new ArrayList<>();

        recyclerView=findViewById(R.id.recyclerViewProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        databaseRef.child(category)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        productModelsList.clear();
                        for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                            ProductModel productModel = productSnapshot.getValue(ProductModel.class);
                            productModelsList.add(productModel);
                        }
                        Collections.sort(productModelsList,ProductModel.BY_ASCENDING);
                        productAdapter =new ProductAdapter(CategoryProductsActivity.this,productModelsList);
                        recyclerView.setAdapter(productAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CategoryProductsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.products, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            startActivity(new Intent(this,CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
