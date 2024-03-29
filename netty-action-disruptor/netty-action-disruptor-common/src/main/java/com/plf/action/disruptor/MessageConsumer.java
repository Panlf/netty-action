package com.plf.action.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.plf.action.entity.TranslatorDataWapper;

public abstract class MessageConsumer implements WorkHandler<TranslatorDataWapper>{

	protected String consumerId;
	
	public MessageConsumer(String consumerId){
		this.consumerId = consumerId;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

}
