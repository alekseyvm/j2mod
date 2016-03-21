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
import com.j2mod.modbus.procimg.InputRegister;
import com.j2mod.modbus.procimg.SimpleInputRegister;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>ReadInputRegistersRequest</tt>. The implementation
 * directly correlates with the class 0 function <i>read multiple registers (FC
 * 4)</i>. It encapsulates the corresponding response message.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public final class ReadInputRegistersResponse extends ModbusResponse {

    // instance attributes
    private int m_ByteCount;
    private InputRegister[] m_Registers;

    /**
     * Constructs a new <tt>ReadInputRegistersResponse</tt> instance.
     */
    public ReadInputRegistersResponse() {
        super();

        setFunctionCode(Modbus.READ_INPUT_REGISTERS);
    }

    /**
     * Constructs a new <tt>ReadInputRegistersResponse</tt> instance.
     *
     * @param registers the InputRegister[] holding response input registers.
     */
    public ReadInputRegistersResponse(InputRegister[] registers) {
        super();

        setFunctionCode(Modbus.READ_INPUT_REGISTERS);
        setDataLength(registers.length * 2 + 1);

        m_ByteCount = registers.length * 2 + 1;
        m_Registers = registers;
    }

    /**
     * Returns the number of bytes that have been read.
     *
     * @return the number of bytes that have been read as <tt>int</tt>.
     */
    public int getByteCount() {
        return m_ByteCount;
    }

    /**
     * Returns the number of words that have been read. The returned value
     * should be half as much as the byte count of the response.
     *
     * @return the number of words that have been read as <tt>int</tt>.
     */
    public int getWordCount() {
        return m_ByteCount / 2;
    }

    /**
     * Set the number of words to be written.
     */
    public void setWordCount(int count) {
        m_ByteCount = count * 2 + 1;
    }

    /**
     * Returns the <tt>InputRegister</tt> at the given position (relative to the
     * reference used in the request).
     *
     * @param index the relative index of the <tt>InputRegister</tt>.
     *
     * @return the register as <tt>InputRegister</tt>.
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public InputRegister getRegister(int index) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException(index + " < 0");
        }

        if (index >= getWordCount()) {
            throw new IndexOutOfBoundsException(index + " >= " + getWordCount());
        }

        return m_Registers[index];
    }

    /**
     * Returns the value of the register at the given position (relative to the
     * reference used in the request) interpreted as usigned short.
     *
     * @param index the relative index of the register for which the value should
     *              be retrieved.
     *
     * @return the unsigned short value as an <tt>int</tt>.
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public int getRegisterValue(int index) throws IndexOutOfBoundsException {
        return getRegister(index).toUnsignedShort();
    }

    /**
     * Returns a reference to the array of input registers read.
     *
     * @return a <tt>InputRegister[]</tt> instance.
     */
    public synchronized InputRegister[] getRegisters() {
        InputRegister[] dest = new InputRegister[m_Registers.length];
        System.arraycopy(m_Registers, 0, dest, 0, dest.length);
        return dest;
    }

    /**
     * Sets the entire block of registers for this response
     */
    public void setRegisters(InputRegister[] registers) {
        setDataLength(registers.length * 2 + 1);
        m_ByteCount = registers.length * 2 + 1;
        m_Registers = registers;
    }

    public void writeData(DataOutput dout) throws IOException {
        dout.writeByte(m_ByteCount);

        for (int k = 0; k < getWordCount(); k++) {
            dout.write(m_Registers[k].toBytes());
        }
    }

    public void readData(DataInput din) throws IOException {
        m_ByteCount = din.readUnsignedByte();

        InputRegister[] registers = new InputRegister[getWordCount()];
        for (int k = 0; k < getWordCount(); k++) {
            registers[k] = new SimpleInputRegister(din.readByte(), din.readByte());
        }
        m_Registers = registers;

        setDataLength(m_ByteCount + 1);
    }

    public byte[] getMessage() {
        byte result[] = new byte[m_Registers.length * 2 + 1];
        result[0] = (byte)(m_Registers.length * 2);

        for (int i = 0; i < m_Registers.length; i++) {
            byte value[] = m_Registers[i].toBytes();

            result[1 + i * 2] = value[0];
            result[2 + i * 2] = value[1];
        }
        return result;
    }
}