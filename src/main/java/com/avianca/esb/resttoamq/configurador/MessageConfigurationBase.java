/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.avianca.esb.resttoamq.configurador;
import javax.jms.ConnectionFactory;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import com.avianca.esb.resttoamq.properties.AmqpProducerBase;


@Configuration
public class MessageConfigurationBase {

    @Autowired
    private AmqpProducerBase consumerBase;
    
    @Bean
    public ConnectionFactory connectionFactory(){
    	String brokerURL = "amqp://" + consumerBase.getHostName() + ":" + consumerBase.getPort(); 
		
//    	String brokerURL = "failover:(tcp://" + consumerBase.getHostName() + ":" + consumerBase.getPort() 
//    			+ ",tcp://" + consumerBase.getHostNameFailover() + ":" + consumerBase.getPortFailover() + ")?maxReconnectAttempts=3"; 
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerURL);
        connectionFactory.setUserName(consumerBase.getUser());
        connectionFactory.setPassword(consumerBase.getPasswd());

        return connectionFactory;
    }

    /*
     * Used for Sending Messages.
     */
    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setPubSubDomain(true); // false for a Queue, true for a Topic
        return template;
    }

    @Bean
    MessageConverter converter(){
        return new SimpleMessageConverter();
    }
}