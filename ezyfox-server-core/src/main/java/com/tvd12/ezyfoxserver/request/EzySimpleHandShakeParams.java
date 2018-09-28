package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;

import lombok.Getter;

@Getter
public class EzySimpleHandShakeParams 
        extends EzySimpleRequestParams 
        implements EzyHandshakeParams {
    private static final long serialVersionUID = 8042927639638762414L;
    
    protected String token;
    protected String clientId;
    protected String clientKey;
    protected String clientType;
    protected String clientVersion;
    protected boolean enableEncryption;
    
    @Override
    public void deserialize(EzyArray t) {
        this.clientId = t.get(0, String.class);
        this.clientKey = t.get(1, String.class);
        this.clientType = t.get(2, String.class);
        this.clientVersion = t.get(3, String.class);
        this.enableEncryption = t.get(4, boolean.class);
        this.token = t.get(5, String.class);
    }
    
}
