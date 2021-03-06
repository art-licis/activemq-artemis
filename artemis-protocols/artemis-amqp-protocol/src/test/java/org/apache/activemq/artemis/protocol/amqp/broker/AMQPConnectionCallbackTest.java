/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.protocol.amqp.broker;

import org.apache.activemq.artemis.core.remoting.impl.invm.InVMConnection;
import org.apache.activemq.artemis.core.server.impl.ActiveMQServerImpl;
import org.apache.activemq.artemis.protocol.amqp.sasl.AnonymousServerSASL;
import org.apache.activemq.artemis.protocol.amqp.sasl.GSSAPIServerSASL;
import org.apache.activemq.artemis.protocol.amqp.sasl.PlainSASL;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AMQPConnectionCallbackTest {

   @Test
   public void getServerSASLOnlyAllowedMechs() throws Exception {
      ProtonProtocolManager protonProtocolManager = new ProtonProtocolManager(new ProtonProtocolManagerFactory(), null, null, null);
      protonProtocolManager.setSaslMechanisms(new String[]{PlainSASL.NAME});
      AMQPConnectionCallback connectionCallback = new AMQPConnectionCallback(protonProtocolManager, new InVMConnection(1, null, null, null), null, new ActiveMQServerImpl());
      assertEquals(1, connectionCallback.getSaslMechanisms().length);
      for (String mech: connectionCallback.getSaslMechanisms()) {
         assertNotNull(connectionCallback.getServerSASL(mech));
      }
      assertNull("can't get mechanism not in the list", connectionCallback.getServerSASL(GSSAPIServerSASL.NAME));
   }

   @Test
   public void getServerSASLAnonDefault() throws Exception {
      ProtonProtocolManager protonProtocolManager = new ProtonProtocolManager(new ProtonProtocolManagerFactory(), null, null, null);
      protonProtocolManager.setSaslMechanisms(new String[]{});
      AMQPConnectionCallback connectionCallback = new AMQPConnectionCallback(protonProtocolManager, null, null, new ActiveMQServerImpl());
      assertNotNull("can get anon with empty list", connectionCallback.getServerSASL(AnonymousServerSASL.NAME));
   }
}