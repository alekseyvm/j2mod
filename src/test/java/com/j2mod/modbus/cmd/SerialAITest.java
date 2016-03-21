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
package com.j2mod.modbus.cmd;

import com.j2mod.modbus.ModbusCoupler;
import com.j2mod.modbus.io.ModbusSerialTransaction;
import com.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.j2mod.modbus.net.SerialConnection;
import com.j2mod.modbus.util.Logger;
import com.j2mod.modbus.util.SerialParameters;

/**
 * Class that implements a simple commandline
 * tool for reading an analog input.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class SerialAITest {

    private static final Logger logger = Logger.getLogger(SerialAITest.class);

    public static void main(String[] args) {

        SerialConnection con = null;
        ModbusSerialTransaction trans;
        ReadInputRegistersRequest req;
        ReadInputRegistersResponse res;

        String portname = null;
        int unitid = 0;
        int ref = 0;
        int count = 0;
        int repeat = 1;

        try {

            //1. Setup the parameters
            if (args.length < 4) {
                printUsage();
                System.exit(1);
            }
            else {
                try {
                    portname = args[0];
                    unitid = Integer.parseInt(args[1]);
                    ref = Integer.parseInt(args[2]);
                    count = Integer.parseInt(args[3]);
                    if (args.length == 5) {
                        repeat = Integer.parseInt(args[4]);
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    printUsage();
                    System.exit(1);
                }
            }

            //2. Set slave identifier for master response parsing
            ModbusCoupler.getReference().setUnitID(unitid);

            //3. Setup serial parameters
            SerialParameters params = new SerialParameters();
            params.setPortName(portname);
            params.setBaudRate(9600);
            params.setDatabits(8);
            params.setParity("None");
            params.setStopbits(1);
            params.setEncoding("ascii");
            params.setEcho(false);
            logger.system("Encoding [%s]", params.getEncoding());

            //4. Open the connection
            con = new SerialConnection(params);
            con.open();

            //5. Prepare a request
            req = new ReadInputRegistersRequest(ref, count);
            req.setUnitID(unitid);
            req.setHeadless();
            logger.system("Request: %s", req.getHexMessage());

            //6. Prepare the transaction
            trans = new ModbusSerialTransaction(con);
            trans.setRequest(req);

            //7. Execute the transaction repeat times
            int k = 0;
            do {
                trans.execute();

                res = (ReadInputRegistersResponse)trans.getResponse();
                logger.system("Response: %s", res.getHexMessage());
                for (int n = 0; n < res.getWordCount(); n++) {
                    logger.system("Word %d=%d", n, res.getRegisterValue(n));
                }
                k++;
            } while (k < repeat);

            //8. Close the connection
            con.close();

        }
        catch (Exception ex) {
            ex.printStackTrace();
            // Close the connection
            if (con != null) {
                con.close();
            }
        }
    }

    private static void printUsage() {
        logger.system("\nUsage:\n    java com.j2mod.modbus.cmd.SerialAITest <portname [String]>  <Unit Address [int8]> <register [int16]> <wordcount [int16]> {<repeat [int]>}"
        );
    }
}
