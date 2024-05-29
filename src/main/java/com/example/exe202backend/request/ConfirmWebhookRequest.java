package com.example.exe202backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmWebhookRequest {
    private String url;

    public ConfirmWebhookRequest(String url) {
        this.url = url;
    }
}
