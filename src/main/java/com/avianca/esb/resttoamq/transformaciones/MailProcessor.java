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
package com.avianca.esb.resttoamq.transformaciones;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import javax.activation.DataHandler;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: jsherman Date: 4/9/12 Time: 11:39 AM To
 * change this template use File | Settings | File Templates.
 */
public class MailProcessor implements Processor {

	private static final Logger LOG = Logger.getLogger(MailProcessor.class.getName());

	public void process(Exchange exchange) throws Exception {

		LOG.debug("MailProcessor...");
		String body = exchange.getIn().getBody(String.class);

		Map<String, DataHandler> attachments = exchange.getIn().getAttachments();
		if (attachments.size() > 0) {
			for (String name : attachments.keySet()) {
				exchange.getOut().setHeader("attachmentName", name);
			}
		}

		// read the attachments from the in exchange putting them back on the out
		exchange.getOut().setAttachments(attachments);

		// resetting the body on out exchange
		exchange.getOut().setBody(body);
		LOG.debug("MailProcessor complete");
	}
}
