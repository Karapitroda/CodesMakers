package com.app.codesmakers.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.app.codesmakers.ui.main.MainActivity;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.notification.NotificationResponse;
import com.app.codesmakers.databinding.FragmentNotificationsBinding;
import com.app.codesmakers.ui.base.BaseFragment;
import com.hypertrack.hyperlog.HLCallback;
import com.hypertrack.hyperlog.error.HLErrorResponse;

import java.io.File;
import java.util.List;

import static com.app.codesmakers.CMApplication.hyperLog;

public class NotificationFragment extends BaseFragment implements NotificationListener {

    private NotificationVM viewModel;
    FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(NotificationVM.class); // New
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        binding = FragmentNotificationsBinding.bind(root);
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

      //  File file = hyperLog.getDeviceLogsInFile(getActivity());
        Log.i("TAG", hyperLog.getDeviceLogsCount()+"");

    }


    @Override
    public void updateList(List<NotificationResponse> response) {
        if (response.size() > 0 && response.get(0).getData().size() > 0) {
            binding.tvPlaceNotificaion.setVisibility(View.GONE);
            binding.recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
            Log.e("REsponse", response.get(0).getData().size() + "SIZE");
            binding.recyclerViewNotification.setAdapter(new NotificationAdapter(getActivity(), response.get(0).getData()));
        } else {
            binding.tvPlaceNotificaion.setVisibility(View.VISIBLE);
        }
    }

}