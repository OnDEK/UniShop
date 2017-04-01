package com.unishop.Closing;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.util.Log;

import com.unishop.R;

import java.util.ArrayList;
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

/**
 * Created by Daniel on 3/31/17.
 */

public class MessagingActivity extends Activity {

    private static int n = 0;
    private static String[] conversationSim = {
            "Hello", "Are you selling the item still", "Yes", "It looks nice", "Thanks", "Meet me in the woods at night", "Where do you live?", "Wrong number pal",
            "Hi there :)", "What is the items condition?", "Why don't you have a seat over there?", "I Refuse to sell to white cis males, sorry look elseware for your goods", "Sorry to hear that",
            "Is everything okay?", "I don't know...", "Who would ask a question like that", "What is your name, share your personal details with me please.", "That’s a cool looking [phone]. Is it easy to use?",
            "I really like your [shoes]. Did you get them near here?", "That is a really nice [hat]. Can I ask where you got it?", "Is that store near here?", "Was it good value? (Try to avoid asking for specific monetary amounts of items like “How much did it cost?” as that can be considered rude)",
            "Do they have other colours available?", "Thanks for the suggestion.", "I appreciate the information.", "Thank you. That was really helpful."
    };

    private static Message getRandomMessage() {
        n++;
        TextMessage textMessage = new TextMessage();
        textMessage.setText(n + "" +  ": " + conversationSim[(int) (Math.random() * conversationSim.length)]);
        textMessage.setDate(new Date().getTime());
        if (Math.random() > 0.5) {

            textMessage.setSource(MessageSource.EXTERNAL_USER);
        } else {

            textMessage.setSource(MessageSource.LOCAL_USER);
        }
        return textMessage;
    }

    SlyceMessagingFragment slyceMessagingFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        slyceMessagingFragment = (SlyceMessagingFragment) getFragmentManager().findFragmentById(R.id.messaging_fragment);
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo");
        slyceMessagingFragment.setOnSendMessageListener(new UserSendsMessageListener() {
            @Override
            public void onUserSendsTextMessage(String text) {
                Log.d("inf", "******************************** " + text);
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
                for (int i = 0; i < 0; i++)
                    messages.add(getRandomMessage());
                return messages;
            }
        });

        Timer timer = new Timer();
        timer.schedule(new SimulateConvo(), 0, 5000);

        slyceMessagingFragment.setMoreMessagesExist(false);
    }

    class SimulateConvo extends TimerTask {
        public void run() {
            TextMessage textMessage = new TextMessage();
            textMessage.setText(conversationSim[(int) (Math.random() * conversationSim.length)]);
            textMessage.setDate(new Date().getTime());
            if (Math.random() > 0.5) {

                textMessage.setSource(MessageSource.EXTERNAL_USER);
            } else {

                textMessage.setSource(MessageSource.LOCAL_USER);
            }
            slyceMessagingFragment.addNewMessage(textMessage);
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
