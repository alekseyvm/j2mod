/*
 * Copyright 2002-2016 jamod & j2mod development teams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.j2mod.modbus.net;

import com.fazecast.jSerialComm.SerialPort;
import com.j2mod.modbus.util.Logger;
import com.j2mod.modbus.util.SerialParameters;

/**
 * Create a <tt>ModbusListener</tt> from an URI-like specifier.
 *
 * @author Julie
 */
public class ModbusListenerFactory {

    private static final Logger logger = Logger.getLogger(ModbusListenerFactory.class);

    public static ModbusListener createModbusListener(String address) {
        String parts[] = address.split(" *: *");
        if (parts.length < 2) {
            throw new IllegalArgumentException("missing connection information");
        }

        if (parts[0].toLowerCase().equals("device")) {
            /*
             * Create a ModbusSerialListener with the default Modbus
			 * values of 19200 baud, no parity, using the specified
			 * device.  If there is an additional part after the
			 * device name, it will be used as the Modbus unit number.
			 */
            SerialParameters parms = new SerialParameters();
            parms.setPortName(parts[1]);
            parms.setBaudRate(19200);
            parms.setDatabits(8);
            parms.setEcho(false);
            parms.setParity(SerialPort.NO_PARITY);
            parms.setFlowControlIn(SerialPort.FLOW_CONTROL_DISABLED);

            ModbusSerialListener listener = new ModbusSerialListener(parms);
            if (parts.length > 2) {
                int unit = Integer.parseInt(parts[2]);
                if (unit < 0 || unit > 248) {
                    throw new IllegalArgumentException("illegal unit number");
                }

                listener.setUnit(unit);
            }
            listener.setListening(true);

            Thread result = new Thread(listener);
            result.start();

            return listener;
        }
        else if (parts[0].toLowerCase().equals("tcp")) {
            /*
			 * Create a ModbusTCPListener with the default interface
			 * value.  The second optional value is the TCP port number
			 * and the third optional value is the Modbus unit number.
			 */
            ModbusTCPListener listener = new ModbusTCPListener(5);
            if (parts.length > 2) {
                int port = Integer.parseInt(parts[2]);
                listener.setPort(port);
                if (parts.length > 3) {
                    int unit = Integer.parseInt(parts[3]);
                    listener.setUnit(unit);
                }
            }
            listener.setListening(true);

            Thread result = new Thread(listener);
            result.start();

            return listener;
        }
        else if (parts[0].toLowerCase().equals("udp")) {
			/*
			 * Create a ModbusUDPListener with the default interface
			 * value.  The second optional value is the TCP port number
			 * and the third optional value is the Modbus unit number.
			 */
            ModbusUDPListener listener = new ModbusUDPListener();
            if (parts.length > 2) {
                int port = Integer.parseInt(parts[2]);
                listener.setPort(port);
                if (parts.length > 3) {
                    int unit = Integer.parseInt(parts[3]);
                    listener.setUnit(unit);
                }
            }
            listener.setListening(true);

            Thread result = new Thread(listener);
            result.start();

            return listener;
        }
        else {
            throw new IllegalArgumentException("unknown type " + parts[0]);
        }
    }
}
