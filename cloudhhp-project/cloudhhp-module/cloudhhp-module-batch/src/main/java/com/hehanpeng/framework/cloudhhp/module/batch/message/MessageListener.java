package com.hehanpeng.framework.cloudhhp.module.batch.message;

import com.alibaba.fastjson.JSON;
import com.hehanpeng.framework.cloudhhp.common.exception.BatchMessageQueueException;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageHandler;
import com.hehanpeng.framework.cloudhhp.module.batch.message.msinterface.MessageQueueReceiver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executors;

@Slf4j
public class MessageListener implements Runnable {

    private MessageHandler handler;

    private MessageQueueReceiver queue;

    private volatile boolean flag;

    public MessageListener(MessageHandler handler, MessageQueueReceiver queue) {
        this.handler = handler;
        this.queue = queue;
    }

    public void close() {
        log.info("close channel:{} listener", handler.getChannelName());
        this.flag = false;
    }

    @Override
    public void run() {
        String channel = handler.getChannelName();
        flag = true;
        log.info("start listen channel:{}", channel);
        try {
            while (flag) {
                String message = queue.getMessage(channel);
                if (StringUtils.isNotEmpty(message)) {
                    Message msg = JSON.parseObject(message, Message.class);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {

                        @Override
                        public void run() {
                            handler.processMessage(msg);
                        }
                    });
                }
            }
        } catch (BatchMessageQueueException e) {
            log.error("message listener error:{}", e);
            // redis抛异常时，关闭当前线程
            close();
            // 3s后重启当前线程
            log.info("3s restart message listener:{}", channel);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Thread thread = new Thread(this);
            thread.start();
        }
    }
}
