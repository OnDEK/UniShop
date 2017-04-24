package com.unishop.Closing;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.unishop.R;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.ChatResponse;
import com.unishop.models.ItemsResponse;
import com.unishop.models.SendMessage;
import com.unishop.models.USMessage;
import com.unishop.utils.NetworkUtils;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.slyce.messaging.SlyceMessagingFragment;
import it.slyce.messaging.listeners.LoadMoreMessagesListener;
import it.slyce.messaging.listeners.UserSendsMessageListener;
import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 3/31/17.
 */

public class MessagingActivity extends Activity {

    ArrayList<USMessage> messageList = new ArrayList<>();
    String transactionId, title;
    SlyceMessagingFragment slyceMessagingFragment;
    TextView titleTV;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        transactionId = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        titleTV = (TextView) findViewById(R.id.chat_header);
        titleTV.setText(title);

        slyceMessagingFragment = (SlyceMessagingFragment) getFragmentManager().findFragmentById(R.id.messaging_fragment);
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo");
        slyceMessagingFragment.setPictureButtonVisible(false);
        slyceMessagingFragment.setOnSendMessageListener(new UserSendsMessageListener() {
            @Override
            public void onUserSendsTextMessage(String text) {
                timer.cancel();
                SendMessage message = new SendMessage(text);

                String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());
                ApiEndpointInterface apiService = NetworkUtils.getApiService();
                Call<USMessage> call = apiService.sendChat(sessionToken,transactionId, message);
                call.enqueue(new Callback<USMessage>() {
                    @Override
                    public void onResponse(Call<USMessage> call, Response<USMessage> response) {
                        int statusCode = response.code();

                        if(statusCode == 200) {
                            messageList.add(response.body());
                            timer = new Timer();
                            timer.schedule(new refreshConvo(), 0, 5000);
                        }
                    }

                    @Override
                    public void onFailure(Call<USMessage> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onUserSendsMediaMessage(Uri imageUri) {
                Log.d("inf", "******************************** " + imageUri);
            }
        });

        slyceMessagingFragment.setLoadMoreMessagesListener(new LoadMoreMessagesListener() {
            @Override
            public List<Message> loadMoreMessages() {
                ArrayList<Message> messages = new ArrayList<>();
                return messages;
            }
        });
        timer = new Timer();
        timer.schedule(new refreshConvo(), 0, 5000);

        slyceMessagingFragment.setMoreMessagesExist(false);
    }

    static Timer timer;
    @Override
    public void onBackPressed() {
        timer.cancel();
        super.onBackPressed();
    }

    class refreshConvo extends TimerTask {
        public void run() {

            String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());
            ApiEndpointInterface apiService = NetworkUtils.getApiService();
            Call<ChatResponse> call = apiService.getChat(sessionToken,transactionId);
            call.enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                    int statusCode = response.code();

                    if(statusCode == 200) {

                        List<USMessage> tempList = response.body().getMessages();
                        Collections.reverse(tempList);
                        for(USMessage message : tempList) {
                            if(!messageList.contains(message)) {
                                messageList.add(message);
                                TextMessage textMessage = new TextMessage();
                                textMessage.setText(message.getText());
                                String date = message.getSentAt();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
                                Date d = new Date();
                                try {
                                    d = sdf.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                textMessage.setDate(d.getTime());

                                if(!message.getFromYou()){
                                    textMessage.setSource(MessageSource.EXTERNAL_USER);
                                }
                                else {
                                    textMessage.setSource(MessageSource.LOCAL_USER);
                                }
                                slyceMessagingFragment.addNewMessage(textMessage);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<ChatResponse> call, Throwable t) {

                }
            });

        }
    }

}

/**
 * The MIT License (MIT)

 Copyright (c) 2016 SnipSnap

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
