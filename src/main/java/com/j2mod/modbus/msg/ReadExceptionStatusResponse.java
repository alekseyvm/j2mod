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
package com.j2mod.modbus.msg;

import com.j2mod.modbus.Modbus;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>ReadCommEventCounterResponse</tt>.
 *
 * @author Julie Haugh (jfh@ghgande.com)
 *
 * @version @version@ (@date@)
 */
public final class ReadExceptionStatusResponse extends ModbusResponse {

    /*
     * Message fields.
     */
    private int m_Status;

    /**
     * Constructs a new <tt>ReadExceptionStatusResponse</tt> instance.
     */
    public ReadExceptionStatusResponse() {
        super();

        setFunctionCode(Modbus.READ_EXCEPTION_STATUS);
        setDataLength(1);
    }

    /**
     * getStatus -- get the device's status.
     *
     * @return int
     */
    public int getStatus() {
        return m_Status;
    }

    /**
     * setStatus -- set the device's status.
     *
     * @param status Status to set
     */
    public void setStatus(int status) {
        m_Status = status;
    }

    /**
     * writeData -- output the completed Modbus message to dout
     */
    public void writeData(DataOutput dout) throws IOException {
        dout.write(getMessage());
    }

    /**
     * readData -- input the Modbus message from din. If there was a header,
     * such as for Modbus/TCP, it will have been read already.
     */
    public void readData(DataInput din) throws IOException {
        m_Status = din.readByte() & 0xFF;
    }

    /**
     * getMessage -- format the message into a byte array.
     */
    public byte[] getMessage() {
        byte result[] = new byte[1];

        result[0] = (byte)(m_Status & 0xFF);

        return result;
    }
}
