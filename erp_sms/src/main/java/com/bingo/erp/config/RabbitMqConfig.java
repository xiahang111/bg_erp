package com.bingo.erp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMq配置文件
 */
@Configuration
public class RabbitMqConfig {

    public static final String BINHO_WEB = "bingo.web";
    public final static String BINGO_WEB_PROCESS_ANALYZE = "bingo.web.process.analyze";
    public static final String BINHO_PERSON = "bingo.person";
    public static final String BINHO_SMS = "bingo.sms";
    public static final String EXCHANGE_DIRECT = "exchange.direct";

    public static final String ROUTING_KEY_WEB = "bingo.web";
    public final static String ROUTING_KEY_WEB_WEB_PROCESS_ANALYZE = "bingo.web.process.analyze";
    public static final String ROUTING_KEY_PERSON = "bingo.person";
    public static final String ROUTING_KEY_SMS = "bingo.sms";



    /**
     * 申明交换机
     */
    @Bean(EXCHANGE_DIRECT)
    public Exchange EXCHANGE_DIRECT() {
        // 申明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.directExchange(EXCHANGE_DIRECT).durable(true).build();
    }

    /**
     * 申明Web队列
     * @return
     */
    @Bean(BINHO_WEB)
    public Queue BINHO_WEB() {
        return new Queue(BINHO_WEB);
    }


    /**
     * 申明Web队列
     * @return
     */
    @Bean(BINGO_WEB_PROCESS_ANALYZE)
    public Queue BINGO_WEB_PROCESS_ANALYZE() {
        return new Queue(BINGO_WEB_PROCESS_ANALYZE);
    }

    /**
     * 申明Web队列
     * @return
     */
    @Bean(BINHO_PERSON)
    public Queue BINHO_PERSON() {
        return new Queue(BINHO_PERSON);
    }

    /**
     * 申明SMS队列
     * @return
     */
    @Bean(BINHO_SMS)
    public Queue BINHO_SMS() {
        return new Queue(BINHO_SMS);
    }

    /**
     * mogu.web 队列绑定交换机，指定routingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_WEB(@Qualifier(BINHO_WEB) Queue queue, @Qualifier(EXCHANGE_DIRECT) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_WEB).noargs();
    }

    /**
     * mogu.web 队列绑定交换机，指定routingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_PROCESS_ANALYZE(@Qualifier(BINGO_WEB_PROCESS_ANALYZE) Queue queue, @Qualifier(EXCHANGE_DIRECT) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_WEB_WEB_PROCESS_ANALYZE).noargs();
    }

    /**
     * mogu.person 队列绑定交换机，指定routingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_PERSON(@Qualifier(BINHO_PERSON) Queue queue, @Qualifier(EXCHANGE_DIRECT) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_PERSON).noargs();
    }

    /**
     * mogu.sms 队列绑定交换机，指定routingKey
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(BINHO_SMS) Queue queue, @Qualifier(EXCHANGE_DIRECT) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_SMS).noargs();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}


























