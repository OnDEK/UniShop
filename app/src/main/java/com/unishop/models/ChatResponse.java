package com.unishop.models;

/**
 * Created by Daniel on 4/20/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatResponse {

    @SerializedName("messages")
    @Expose
    private List<USMessage> messages = null;
    @SerializedName("older_href")
    @Expose
    private String olderHref;

    public List<USMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<USMessage> messages) {
        this.messages = messages;
    }

    public String getOlderHref() {
        return olderHref;
    }

    public void setOlderHref(String olderHref) {
        this.olderHref = olderHref;
    }

}
