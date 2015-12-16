package ru.trendtech.services.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * Created by petr on 08.08.2015.
 */
@Component
public class Receiver implements MessageListener {

    @Override
    public void onMessage(Message message) {

        String str = new String(message.getBody());
        System.out.println("mess: "+message.toString()+ " str = "+str);
        //super.onMessage(message);
    }


    /* можно не наследоваться от листенера и просто отсавить метод ниже, в таком случае
       в конфиг файле необходимо прописать:
       <rabbit:listener-container connection-factory="rabbitConnectionFactory">
        <rabbit:listener ref="receiver" method="listen" queue-names="myQueue" />
    </rabbit:listener-container>
      с указанием метода, который отвечает за прослушку
      в случае с наследованием это можно не делать т.к. от MessageListener все входящие события обрабатываются в onMessage
      * */
    public void listen(String mess) {
        System.out.println("vvvv: "+mess);
    }
}
