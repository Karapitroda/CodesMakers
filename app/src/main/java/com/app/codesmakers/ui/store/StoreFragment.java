package com.app.codesmakers.ui.store;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.codesmakers.ui.main.MainActivity;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.api.pojo.store.StoreResponse;
import com.app.codesmakers.databinding.FragmentStoreBinding;
import com.app.codesmakers.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class StoreFragment extends BaseFragment implements StoreListener {

    private StoreVM viewModel;
    FragmentStoreBinding binding;

    StoreAdapter mAdapter;
    List<StoreModel> mList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(StoreVM.class); // New
        View root = inflater.inflate(R.layout.fragment_store, container, false);
        binding = FragmentStoreBinding.bind(root);
        viewModel.setDataListener(getActivity(), this);
        binding.setViewModel(viewModel);

        initializeUI();
        return root;
    }

    @Override
    public void initializeUI() {
        binding.setLifecycleOwner(this);
        viewModel.getShowProgress().observe(getActivity(), aBoolean -> {
            try {
                ((MainActivity) getActivity()).updateProgress(aBoolean);
            } catch (Exception ignored) {
            }
        });

        mAdapter = new StoreAdapter();
        binding.recyclerViewStores.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewStores.setAdapter(mAdapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public void updateList(List<StoreResponse> response) {
        mList = response.get(0).getStoresList();
        Log.e("Store List ::: ", mList + " Size");
        //mAdapter.mList = mList;
        if(mList.size()>0){
            binding.tvPlaceStore.setVisibility(View.GONE);
        }else{
            binding.tvPlaceStore.setVisibility(View.VISIBLE);
        }
        mAdapter.setList(getActivity(), mList, callbackListener);
    }

    StoreAdapter.CallbackListener callbackListener = storeModel -> openStoreDetails(storeModel);
}