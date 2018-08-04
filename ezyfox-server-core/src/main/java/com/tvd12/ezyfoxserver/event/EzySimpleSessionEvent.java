package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.Getter;

@Getter
public class EzySimpleSessionEvent implements EzySessionEvent {

    protected EzySession session;
    
    protected EzySimpleSessionEvent(Builder<?> builder) {
        this.session = builder.session;
    }
    
    public static class Builder<B extends Builder<B>> implements EzyBuilder<EzyEvent> {
    
        protected EzySession session;
        
        @SuppressWarnings("unchecked")
        public B session(EzySession session) {
            this.session = session;
            return (B)this;
        }
        
        @Override
        public EzyEvent build() {
            return new EzySimpleSessionEvent(this);
        }
        
    }
    
}
