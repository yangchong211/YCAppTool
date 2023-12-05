/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.stream;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("resource")
public final class JsonWriterTest extends TestCase {

  public void testTopLevelValueTypes() throws IOException {
    StringWriter string1 = new StringWriter();
    JsonWriter writer1 = new JsonWriter(string1);
    writer1.value(true);
    writer1.close();
    assertEquals("true", string1.toString());

    StringWriter string2 = new StringWriter();
    JsonWriter writer2 = new JsonWriter(string2);
    writer2.nullValue();
    writer2.close();
    assertEquals("null", string2.toString());

    StringWriter string3 = new StringWriter();
    JsonWriter writer3 = new JsonWriter(string3);
    writer3.value(123);
    writer3.close();
    assertEquals("123", string3.toString());

    StringWriter string4 = new StringWriter();
    JsonWriter writer4 = new JsonWriter(string4);
    writer4.value(123.4);
    writer4.close();
    assertEquals("123.4", string4.toString());

    StringWriter string5 = new StringWriter();
    JsonWriter writert = new JsonWriter(string5);
    writert.value("a");
    writert.close();
    assertEquals("\"a\"", string5.toString());
  }

  public void testInvalidTopLevelTypes() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.name("hello");
    try {
      jsonWriter.value("world");
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testTwoNames() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.name("a");
    try {
      jsonWriter.name("a");
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testNameWithoutValue() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.name("a");
    try {
      jsonWriter.endObject();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testValueWithoutName() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    try {
      jsonWriter.value(true);
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testMultipleTopLevelValues() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray().endArray();
    try {
      jsonWriter.beginArray();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testBadNestingObject() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.beginObject();
    try {
      jsonWriter.endArray();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testBadNestingArray() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.beginArray();
    try {
      jsonWriter.endObject();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testNullName() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    try {
      jsonWriter.name(null);
      fail();
    } catch (NullPointerException expected) {
    }
  }

  public void testNullStringValue() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.name("a");
    jsonWriter.value((String) null);
    jsonWriter.endObject();
    assertEquals("{\"a\":null}", stringWriter.toString());
  }

  public void testJsonValue() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.name("a");
    jsonWriter.jsonValue("{\"b\":true}");
    jsonWriter.name("c");
    jsonWriter.value(1);
    jsonWriter.endObject();
    assertEquals("{\"a\":{\"b\":true},\"c\":1}", stringWriter.toString());
  }

  public void testNonFiniteDoubles() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    try {
      jsonWriter.value(Double.NaN);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      jsonWriter.value(Double.NEGATIVE_INFINITY);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      jsonWriter.value(Double.POSITIVE_INFINITY);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testNonFiniteBoxedDoubles() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    try {
      jsonWriter.value(Double.valueOf(Double.NaN));
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      jsonWriter.value(Double.valueOf(Double.NEGATIVE_INFINITY));
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      jsonWriter.value(Double.valueOf(Double.POSITIVE_INFINITY));
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testNonFiniteDoublesWhenLenient() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setLenient(true);
    jsonWriter.beginArray();
    jsonWriter.value(Double.NaN);
    jsonWriter.value(Double.NEGATIVE_INFINITY);
    jsonWriter.value(Double.POSITIVE_INFINITY);
    jsonWriter.endArray();
    assertEquals("[NaN,-Infinity,Infinity]", stringWriter.toString());
  }

  public void testNonFiniteBoxedDoublesWhenLenient() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setLenient(true);
    jsonWriter.beginArray();
    jsonWriter.value(Double.valueOf(Double.NaN));
    jsonWriter.value(Double.valueOf(Double.NEGATIVE_INFINITY));
    jsonWriter.value(Double.valueOf(Double.POSITIVE_INFINITY));
    jsonWriter.endArray();
    assertEquals("[NaN,-Infinity,Infinity]", stringWriter.toString());
  }

  public void testDoubles() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.value(-0.0);
    jsonWriter.value(1.0);
    jsonWriter.value(Double.MAX_VALUE);
    jsonWriter.value(Double.MIN_VALUE);
    jsonWriter.value(0.0);
    jsonWriter.value(-0.5);
    jsonWriter.value(2.2250738585072014E-308);
    jsonWriter.value(Math.PI);
    jsonWriter.value(Math.E);
    jsonWriter.endArray();
    jsonWriter.close();
    assertEquals("[-0.0,"
        + "1.0,"
        + "1.7976931348623157E308,"
        + "4.9E-324,"
        + "0.0,"
        + "-0.5,"
        + "2.2250738585072014E-308,"
        + "3.141592653589793,"
        + "2.718281828459045]", stringWriter.toString());
  }

  public void testLongs() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.value(0);
    jsonWriter.value(1);
    jsonWriter.value(-1);
    jsonWriter.value(Long.MIN_VALUE);
    jsonWriter.value(Long.MAX_VALUE);
    jsonWriter.endArray();
    jsonWriter.close();
    assertEquals("[0,"
        + "1,"
        + "-1,"
        + "-9223372036854775808,"
        + "9223372036854775807]", stringWriter.toString());
  }

  public void testNumbers() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.value(new BigInteger("0"));
    jsonWriter.value(new BigInteger("9223372036854775808"));
    jsonWriter.value(new BigInteger("-9223372036854775809"));
    jsonWriter.value(new BigDecimal("3.141592653589793238462643383"));
    jsonWriter.endArray();
    jsonWriter.close();
    assertEquals("[0,"
        + "9223372036854775808,"
        + "-9223372036854775809,"
        + "3.141592653589793238462643383]", stringWriter.toString());
  }

  public void testBooleans() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.value(true);
    jsonWriter.value(false);
    jsonWriter.endArray();
    assertEquals("[true,false]", stringWriter.toString());
  }

  public void testBoxedBooleans() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.value((Boolean) true);
    jsonWriter.value((Boolean) false);
    jsonWriter.value((Boolean) null);
    jsonWriter.endArray();
    assertEquals("[true,false,null]", stringWriter.toString());
  }

  public void testNulls() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.nullValue();
    jsonWriter.endArray();
    assertEquals("[null]", stringWriter.toString());
  }

  public void testStrings() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.value("a");
    jsonWriter.value("a\"");
    jsonWriter.value("\"");
    jsonWriter.value(":");
    jsonWriter.value(",");
    jsonWriter.value("\b");
    jsonWriter.value("\f");
    jsonWriter.value("\n");
    jsonWriter.value("\r");
    jsonWriter.value("\t");
    jsonWriter.value(" ");
    jsonWriter.value("\\");
    jsonWriter.value("{");
    jsonWriter.value("}");
    jsonWriter.value("[");
    jsonWriter.value("]");
    jsonWriter.value("\0");
    jsonWriter.value("\u0019");
    jsonWriter.endArray();
    assertEquals("[\"a\","
        + "\"a\\\"\","
        + "\"\\\"\","
        + "\":\","
        + "\",\","
        + "\"\\b\","
        + "\"\\f\","
        + "\"\\n\","
        + "\"\\r\","
        + "\"\\t\","
        + "\" \","
        + "\"\\\\\","
        + "\"{\","
        + "\"}\","
        + "\"[\","
        + "\"]\","
        + "\"\\u0000\","
        + "\"\\u0019\"]", stringWriter.toString());
  }

  public void testUnicodeLineBreaksEscaped() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.value("\u2028 \u2029");
    jsonWriter.endArray();
    assertEquals("[\"\\u2028 \\u2029\"]", stringWriter.toString());
  }

  public void testEmptyArray() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.endArray();
    assertEquals("[]", stringWriter.toString());
  }

  public void testEmptyObject() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.endObject();
    assertEquals("{}", stringWriter.toString());
  }

  public void testObjectsInArrays() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.beginObject();
    jsonWriter.name("a").value(5);
    jsonWriter.name("b").value(false);
    jsonWriter.endObject();
    jsonWriter.beginObject();
    jsonWriter.name("c").value(6);
    jsonWriter.name("d").value(true);
    jsonWriter.endObject();
    jsonWriter.endArray();
    assertEquals("[{\"a\":5,\"b\":false},"
        + "{\"c\":6,\"d\":true}]", stringWriter.toString());
  }

  public void testArraysInObjects() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.name("a");
    jsonWriter.beginArray();
    jsonWriter.value(5);
    jsonWriter.value(false);
    jsonWriter.endArray();
    jsonWriter.name("b");
    jsonWriter.beginArray();
    jsonWriter.value(6);
    jsonWriter.value(true);
    jsonWriter.endArray();
    jsonWriter.endObject();
    assertEquals("{\"a\":[5,false],"
        + "\"b\":[6,true]}", stringWriter.toString());
  }

  public void testDeepNestingArrays() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    for (int i = 0; i < 20; i++) {
      jsonWriter.beginArray();
    }
    for (int i = 0; i < 20; i++) {
      jsonWriter.endArray();
    }
    assertEquals("[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]", stringWriter.toString());
  }

  public void testDeepNestingObjects() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    for (int i = 0; i < 20; i++) {
      jsonWriter.name("a");
      jsonWriter.beginObject();
    }
    for (int i = 0; i < 20; i++) {
      jsonWriter.endObject();
    }
    jsonWriter.endObject();
    assertEquals("{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":"
        + "{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{"
        + "}}}}}}}}}}}}}}}}}}}}}", stringWriter.toString());
  }

  public void testRepeatedName() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.name("a").value(true);
    jsonWriter.name("a").value(false);
    jsonWriter.endObject();
    // JsonWriter doesn't attempt to detect duplicate names
    assertEquals("{\"a\":true,\"a\":false}", stringWriter.toString());
  }

  public void testPrettyPrintObject() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setIndent("   ");

    jsonWriter.beginObject();
    jsonWriter.name("a").value(true);
    jsonWriter.name("b").value(false);
    jsonWriter.name("c").value(5.0);
    jsonWriter.name("e").nullValue();
    jsonWriter.name("f").beginArray();
    jsonWriter.value(6.0);
    jsonWriter.value(7.0);
    jsonWriter.endArray();
    jsonWriter.name("g").beginObject();
    jsonWriter.name("h").value(8.0);
    jsonWriter.name("i").value(9.0);
    jsonWriter.endObject();
    jsonWriter.endObject();

    String expected = "{\n"
        + "   \"a\": true,\n"
        + "   \"b\": false,\n"
        + "   \"c\": 5.0,\n"
        + "   \"e\": null,\n"
        + "   \"f\": [\n"
        + "      6.0,\n"
        + "      7.0\n"
        + "   ],\n"
        + "   \"g\": {\n"
        + "      \"h\": 8.0,\n"
        + "      \"i\": 9.0\n"
        + "   }\n"
        + "}";
    assertEquals(expected, stringWriter.toString());
  }

  public void testPrettyPrintArray() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setIndent("   ");

    jsonWriter.beginArray();
    jsonWriter.value(true);
    jsonWriter.value(false);
    jsonWriter.value(5.0);
    jsonWriter.nullValue();
    jsonWriter.beginObject();
    jsonWriter.name("a").value(6.0);
    jsonWriter.name("b").value(7.0);
    jsonWriter.endObject();
    jsonWriter.beginArray();
    jsonWriter.value(8.0);
    jsonWriter.value(9.0);
    jsonWriter.endArray();
    jsonWriter.endArray();

    String expected = "[\n"
        + "   true,\n"
        + "   false,\n"
        + "   5.0,\n"
        + "   null,\n"
        + "   {\n"
        + "      \"a\": 6.0,\n"
        + "      \"b\": 7.0\n"
        + "   },\n"
        + "   [\n"
        + "      8.0,\n"
        + "      9.0\n"
        + "   ]\n"
        + "]";
    assertEquals(expected, stringWriter.toString());
  }

  public void testLenientWriterPermitsMultipleTopLevelValues() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.setLenient(true);
    writer.beginArray();
    writer.endArray();
    writer.beginArray();
    writer.endArray();
    writer.close();
    assertEquals("[][]", stringWriter.toString());
  }

  public void testStrictWriterDoesNotPermitMultipleTopLevelValues() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginArray();
    writer.endArray();
    try {
      writer.beginArray();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testClosedWriterThrowsOnStructure() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginArray();
    writer.endArray();
    writer.close();
    try {
      writer.beginArray();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      writer.endArray();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      writer.beginObject();
      fail();
    } catch (IllegalStateException expected) {
    }
    try {
      writer.endObject();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testClosedWriterThrowsOnName() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginArray();
    writer.endArray();
    writer.close();
    try {
      writer.name("a");
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testClosedWriterThrowsOnValue() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginArray();
    writer.endArray();
    writer.close();
    try {
      writer.value("a");
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testClosedWriterThrowsOnFlush() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginArray();
    writer.endArray();
    writer.close();
    try {
      writer.flush();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testWriterCloseIsIdempotent() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginArray();
    writer.endArray();
    writer.close();
    writer.close();
  }
}
