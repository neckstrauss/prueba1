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

import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.avianca.esb.resttoamq.properties.AmqProducerBase;


@Configuration
public class MessageConfigurationBase {

    @Autowired
    private AmqProducerBase consumerBase;
    
    @Bean
    public ConnectionFactory connectionFactory(){
    	String brokerURL;
    	
    	if(consumerBase.getHostNameFailover() == null || consumerBase.getHostNameFailover().equals(""))
    	{
    		brokerURL = "tcp://" + consumerBase.getHostName() + ":" + consumerBase.getPort(); 
    	}
    	else
    	{
    		brokerURL = "failover:(tcp://" + consumerBase.getHostName() + ":" + consumerBase.getPort() 
			+ ",tcp://" + consumerBase.getHostNameFailover() + ":" + consumerBase.getPortFailover() + ")?maxReconnectAttempts=3"; 
    	}	
    	
    	ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerURL);
        connectionFactory.setUserName(consumerBase.getUser());
        connectionFactory.setPassword(consumerBase.getPasswd());
        
        RedeliveryPolicy policy = connectionFactory.getRedeliveryPolicy();
        policy.setInitialRedeliveryDelay(consumerBase.getInitialRedeliveryDelay());
        policy.setBackOffMultiplier(consumerBase.getBackOffMultiplier());
        policy.setUseExponentialBackOff(consumerBase.isUseExponentialBackOff());
        policy.setMaximumRedeliveries(consumerBase.getMaximumRedeliveries());
        return connectionFactory;
    }
      
	@Bean(name = "activemq-component")
	public ActiveMQComponent amqpComponent()
	{		
		ActiveMQComponent amqComp= new ActiveMQComponent();
		amqComp.setTransacted(consumerBase.isTransacted());
		amqComp.setTransactionManager(txManager());
		amqComp.setCacheLevelName("CACHE_CONSUMER");
		amqComp.setConnectionFactory(connectionFactory());
		return amqComp;
	}
    
    @Bean 
    public PlatformTransactionManager  txManager()
	{
		JmsTransactionManager jmsTransactionManager = new JmsTransactionManager(connectionFactory());
		return jmsTransactionManager;
	}
}