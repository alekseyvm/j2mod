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
package com.ghgande.j2mod.modbus.procimg;

import com.ghgande.j2mod.modbus.util.Observable;

/**
 * Class implementing an observable digital output.
 *
 * @author Dieter Wimberger
 * @author Steve O'Hara (4NG)
 * @version 2.0 (March 2016)
 */
public class ObservableDigitalOut extends Observable implements DigitalOut {

    /**
     * A boolean holding the state of this digital out.
     */
    protected boolean set;

    /**
     * Determine if the digital output is set.
     *
     * @return the boolean value of the digital output.
     */
    public boolean isSet() {
        return set;
    }

    /**
     * Set or clear the digital output.  Will notify any registered
     * observers.
     */
    public void set(boolean b) {
        set = b;
        notifyObservers("value");
    }
}
