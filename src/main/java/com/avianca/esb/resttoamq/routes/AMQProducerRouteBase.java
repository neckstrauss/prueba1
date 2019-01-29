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
package com.avianca.esb.resttoamq.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.avianca.esb.resttoamq.properties.AmqProducerBase;
import com.avianca.esb.resttoamq.configurador.ConfigurationRoute;

@Component
public class AMQProducerRouteBase extends ConfigurationRoute {

	@Autowired
	private AmqProducerBase amqpProducerConfig;

	public void configure() throws Exception {
		super.configure();
		// context().setStreamCaching(true);
		from("direct:amqProducerRouteBase").routeId("resttoamq_amqp_producer")
			.log("Sending to amqp")
			.inOnly("activemq:" + amqpProducerConfig.getQueueName())
			//.to( "activemq:" + amqpProducerConfig.getQueueName())
			.log("Sending to amqp 2")
		.end();
	}
}
