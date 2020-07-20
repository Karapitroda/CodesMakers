package com.app.codesmakers.ui.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.codesmakers.BuildConfig;
import com.app.codesmakers.CMApplication;
import com.app.codesmakers.api.pojo.ResponseBody;
import com.app.codesmakers.api.pojo.chat.ChatItem;
import com.app.codesmakers.api.pojo.chat.ChatModel;
import com.app.codesmakers.api.pojo.chat.ChatResponse;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.courieroffers.CourierOffersModel;
import com.app.codesmakers.api.pojo.track.CurrentOrderModel;
import com.app.codesmakers.ui.base.BaseVM;
import com.app.codesmakers.ui.update.UpdateActivity;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.app.codesmakers.utils.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import gun0912.tedbottompicker.TedBottomPicker;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;

import static com.app.codesmakers.ui.base.BaseActivity.PERMISSIONS_STORAGE;
import static com.app.codesmakers.ui.base.BaseActivity.REQUEST_EXTERNAL_STORAGE;
import static com.app.codesmakers.utils.AppConstants.STATUS_ORDER_COMPLETED;

/**
 * Created by DeveloperAndroid on 06/05/2019.
 */
public class ChatViewModel extends ViewModel implements ChatAdapter.MessagesListener {
    private List<ChatItem> mMessageList;
    private Set<ChatItem> mSetList;
    private ChatView mListener;
    public ObservableField<String> chatMessage = new ObservableField<>();
    private ChatAdapter chatAdapter;
    Activity activity;

    //Data Requires from intent
    public String orderId = "";
    public String chatWithName = "";
    public String chatWithPicture = "";
    public String chatWithPhone = "";
    public String imageToSend = "";
    public String billImage = "";
    public boolean isBillImage = false;
    public boolean isCustomer = false;
    public boolean isFirst = true;
    public boolean chatScrolled = false;
    public Uri uriValue = null;

    public static final int REQUEST_TAKE_PHOTO = 1;

    Timer mTimer;

    public ObservableField<String> getChatMessage() {
        return chatMessage;
    }

    ChatViewModel(@NonNull final ChatView resultView, Activity activity) {
        mListener = resultView;
        this.activity = activity;
        mSetList = new HashSet<>();
        mMessageList = new ArrayList<>();

        mListener.getRecyclerView().setLayoutManager(new LinearLayoutManager(mListener.getCurrentActivity()));
        chatAdapter = new ChatAdapter(mListener.getCurrentActivity(), mMessageList, this);
        mListener.getRecyclerView().setAdapter(chatAdapter);
        mListener.getRecyclerView().scrollToPosition(chatAdapter.getItemCount() - 1);
    }


    public void onSendImageClicked(@NonNull final View view) {
        mListener.updateSheet();
        mListener.hidekeyboard();
        mListener.openImagePicker();
    }

    public void onImageComplaintClicked(@NonNull final View view) {

    }

    public void onCancelOrderClicked(final View view) {
        mListener.updateSheet();
        mListener.hidekeyboard();
        mListener.showConfirmDialog();
    }

    public void onPostBillClicked(final View view) {
        isBillImage = true;
        mListener.updateSheet();
        mListener.hidekeyboard();
        startCamera();
    }

    public void onSendClicked(@NonNull final View view) {
        if (chatMessage.get() == null || chatMessage.get().trim().isEmpty()) {
            return;
        }
        sendMessage();
    }


    public void addSendMessage(ChatItem chatItem) {

        if (chatAdapter == null) {
            if (mMessageList == null) {
                mMessageList = new ArrayList<>();
            }
            chatAdapter = new ChatAdapter(mListener.getCurrentActivity(), mMessageList, this);
            mListener.getRecyclerView().setAdapter(chatAdapter);
            mListener.getRecyclerView().scrollToPosition(chatAdapter.getItemCount() - 1);
        }
        mMessageList.add(chatItem);
        chatAdapter.notifyItemInserted(mMessageList.size() - 1);
        scrollToBottom();
    }


    public void scrollToBottom() {
        mListener.getRecyclerView().scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @SuppressLint("CheckResult")
    public void getChat() {
        if (mListener.checkConnection()) {
            if (isFirst) {
                mListener.showProgress("");
                isFirst = false;
            }
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            Observable<List<ChatResponse>> observable = CMApplication.getInstance().instantiateRetroInterface().getChat(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getDeviceType(), request1.getOSPlayerID(), request1.getAppName(), request1.getUserID(),
                    Utilities.getLocationString(SessionManager.getInstance().getLastLocation()), orderId);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResponse, this::handleError);
        }
    }

    @SuppressLint("CheckResult")
    public void sendMessage() {
        if (mListener.checkConnection()) {
            mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            if (chatMessage.get() == null || chatMessage.get().isEmpty()) {
                ChatItem chatItem = new ChatItem("11", System.currentTimeMillis() + "", ChatItem.ChatItemType.MESSAGE_OUT, "", "", "uri", true);
                chatItem.setUriImage(uriValue);
                addSendMessage(chatItem);
            } else {
                ChatItem chatItem = new ChatItem("", System.currentTimeMillis() + "", ChatItem.ChatItemType.MESSAGE_OUT, chatMessage.get(), "", "", false);
                addSendMessage(chatItem);
            }

            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().sendMessage(
                    request1.getApiKey(), request1.getAppModelType(),
                    request1.getAppModelVersion(), request1.getDeviceID(),
                    request1.getDeviceType(), request1.getOSPlayerID(),
                    request1.getAppName(), request1.getUserID(),
                    Utilities.getLocationString(SessionManager.getInstance().getLastLocation()), chatMessage.get() == null || chatMessage.get().isEmpty() ? " " : chatMessage.get(), orderId, imageToSend, billImage);
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                        @Override
                        public void onNext(List<ResponseBody> responseBodies) {
                            mListener.hideProgress();
                            resetVariables();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mListener.hideProgress();
                        }
                    });
        }
    }

    private void resetVariables() {
        chatMessage.set("");
        imageToSend = "";
        billImage = "";
        isBillImage = false;
    }


    public void handleError(Throwable pThrowable) {
        mListener.hideProgress();
    }

    private void handleResponse(List<ChatResponse> courierOffersModels) {
        mListener.hideProgress();
        int size = mMessageList.size();
        int responseSize = courierOffersModels.get(0).getData().size();

        if (size != responseSize) {
            if (courierOffersModels.get(0).getData() != null && courierOffersModels.get(0).getData().size() > 0) {
                String userId = SessionManager.getInstallationResquest().getUserID();
                for (int i = size; i < responseSize; i++) {
                    ChatModel chatModel = courierOffersModels.get(0).getData().get(i);
                    String image = chatModel.getImg().trim();
                    boolean isImage = false;
                    try {
                        URL url = new URL(image);
                        isImage = true;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (!chatModel.getFromUserID().equalsIgnoreCase(userId)) {
                        ChatItem chatItem = new ChatItem(chatModel.getId(), chatModel.getDate(), ChatItem.ChatItemType.MESSAGE_IN, chatModel.getContent(), chatModel.getDate(), chatModel.getImg(), isImage);
                        mMessageList.add(chatItem);
                    } else {
                        ChatItem chatItem = new ChatItem(chatModel.getId(), chatModel.getDate(), ChatItem.ChatItemType.MESSAGE_OUT, chatModel.getContent(), chatModel.getDate(), chatModel.getImg(), isImage);
                        mMessageList.add(chatItem);
                    }
                }
                chatAdapter.notifyDataSetChanged();
                if (!chatScrolled) {
                    chatScrolled = true;
                    scrollToBottom();
                }
            }
        }
    }


    public void startCamera() {
        int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

        } else {
            try {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

    }


    public void updateOrder(String status) {
        if (mListener.checkConnection()) {
            mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().updateOrder(request1.getApiKey(), request1.getAppModelType(),
                    request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(), request1.getUserID(),
                    request1.getDeviceType(), Utilities.getLocationString(SessionManager.getInstance().getLastLocation()),
                    status, orderId, request1.getAppName());

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                        @Override
                        public void onNext(List<ResponseBody> responseBodies) {
                            mListener.hideProgress();
                            mListener.showToast("" + responseBodies.get(0).getMessage());
                            mListener.backFinish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mListener.hideProgress();
                        }
                    });
        }
    }


    void startTimer() {
        Log.e("START 1111", "CHAT CALL");

        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("START 22222", "CHAT CALL");

                        getChat();
                    }
                });
            }
        };
        mTimer.schedule(timerTask, 0, 10000);
    }

    //cancel timer
    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
            Log.e("CHAT 22222", "CHAT canceled");

        }
    }

    @Override
    public void onMessageSelected(ChatItem chatModel) {
        FragmentManager manager = mListener.getManagerForFragment();
        Fragment frag = manager.findFragmentByTag(PreviewDialog.TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        PreviewDialog dialog = PreviewDialog.newInstance(activity, chatModel.getImage(), chatModel.getUriImage());
        dialog.show(manager, PreviewDialog.TAG);
    }


    @SuppressLint("CheckResult")
    public void newComplaint(String contentComplaintStr) {
        if (mListener.checkConnection()) {
            mListener.showProgress("");
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;

            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().newComplaint(
                    request1.getApiKey(), request1.getAppModelType(),
                    request1.getAppModelVersion(), request1.getDeviceID(),
                    request1.getOSPlayerID(), request1.getUserID(),
                    request1.getDeviceType(), Utilities.getLocationString(SessionManager.getInstance().getLastLocation()),
                    contentComplaintStr, imageToSend, orderId, request1.getAppName());

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                        @Override
                        public void onNext(List<ResponseBody> responseBodies) {
                            mListener.hideProgress();

                        }

                        @Override
                        public void onError(Throwable e) {
                            mListener.hideProgress();
                        }
                    });
        }
    }
}
