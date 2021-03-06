/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.id.serial;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.id.LongIdentifierGenerator;
import org.apache.commons.id.SerializationTestContext;
import org.apache.commons.id.test.AssertSerialization;

import java.io.Serializable;

/**
 * @author Commons-Uid team
 * @version $Id: LongGeneratorTest.java 480488 2006-11-29 08:57:26Z bayard $
 */
public class LongGeneratorTest extends TestCase {
    
    /** Test LongIdentifier incrementing */
    public void testLongIncrementing() {
        LongIdentifierGenerator f = new LongGenerator(true, 0);
        assertEquals(new Long(0), f.nextLongIdentifier());
        assertEquals(new Long(1), f.nextLongIdentifier());
        assertEquals(new Long(2), f.nextIdentifier());
        assertEquals(new Long(3), f.nextLongIdentifier());
        assertEquals(new Long(4), f.nextLongIdentifier());
        assertEquals(new Long(5), f.nextIdentifier());
        assertEquals(new Long(6), f.nextIdentifier());
        assertEquals(new Long(7), f.nextLongIdentifier());
    }

    /** Test LongIdentifier initialization */
    public void testLongIncrementingInit() {
        LongIdentifierGenerator f = new LongGenerator(true, 100);
        assertEquals(new Long(100), f.nextLongIdentifier());
        assertEquals(new Long(101), f.nextLongIdentifier());
    }

    /** Test LongIdentifier wrapping */
    public void testLongIncrementingWrap() {
        LongIdentifierGenerator f = new LongGenerator(true, Long.MAX_VALUE - 1);
        assertEquals(new Long(f.maxValue() - 1), f.nextLongIdentifier());
        assertEquals(new Long(f.maxValue()), f.nextLongIdentifier());
        assertEquals(new Long(f.minValue()), f.nextLongIdentifier());
        assertEquals(new Long(Long.MIN_VALUE + 1), f.nextLongIdentifier());
    }

    /** Test LongIdentifier without wrapping */
    public void testLongIncrementingNoWrap() {
        LongIdentifierGenerator f = new LongGenerator(false, Long.MAX_VALUE);
        try {
            f.nextLongIdentifier();
            fail("Thrown " + IllegalStateException.class.getName() + " expected");
        } catch (final IllegalStateException e) {
        }
    }
    
    /** Test Long generator with no wrapping is not resetted */
    public void testLongIncrementingNoWrapIsNotResetted() {
        LongIdentifierGenerator f = new LongGenerator(false, Long.MAX_VALUE);
        try {
            f.nextLongIdentifier();
            fail("Thrown " + IllegalStateException.class.getName() + " expected");
        } catch (final IllegalStateException e) {
        }
        try {
            f.nextLongIdentifier();
            fail("Thrown " + IllegalStateException.class.getName() + " expected");
        } catch (final IllegalStateException e) {
        }
    }
    
    /**
     * {@link TestSuite} for SessionIdGenerator. Ensures serialization.
     * 
     * @return the TestSuite
     */
    public static TestSuite suite() {
        final TestSuite suite = new TestSuite(LongGeneratorTest.class);
        suite.addTest(AssertSerialization.createSerializationTestSuite(new SerializationTestContext() {

            public void verify(Object serialized, long uid) {
                LongGenerator test = (LongGenerator)serialized;
                LongGenerator idGenerator = (LongGenerator)createReference();
                assertEquals(idGenerator.maxValue(), test.maxValue());
                assertEquals(idGenerator.minValue(), test.minValue());
                assertEquals(idGenerator.isWrap(), test.isWrap());
                assertEquals(new Long(3), test.nextLongIdentifier());
            }

            public Serializable createReference() {
                return new LongGenerator(false, 3);
            }

            public Class getType() {
                return LongGenerator.class;
            }
        }));
        return suite;
    }
}
