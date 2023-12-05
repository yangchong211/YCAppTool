/*
 * Copyright (C) 2014 Google Inc.
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

package com.google.gson.functional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import junit.framework.TestCase;

/**
 * Functional tests for the {@link com.google.gson.annotations.JsonAdapter} annotation on classes.
 */
public final class JsonAdapterAnnotationOnClassesTest extends TestCase {

  public void testJsonAdapterInvoked() {
    Gson gson = new Gson();
    String json = gson.toJson(new A("bar"));
    assertEquals("\"jsonAdapter\"", json);

   // Also invoke the JsonAdapter javadoc sample
    json = gson.toJson(new User("Inderjeet", "Singh"));
    assertEquals("{\"name\":\"Inderjeet Singh\"}", json);
    User user = gson.fromJson("{'name':'Joel Leitch'}", User.class);
    assertEquals("Joel", user.firstName);
    assertEquals("Leitch", user.lastName);

    json = gson.toJson(Foo.BAR);
    assertEquals("\"bar\"", json);
    Foo baz = gson.fromJson("\"baz\"", Foo.class);
    assertEquals(Foo.BAZ, baz);
  }

  public void testJsonAdapterFactoryInvoked() {
    Gson gson = new Gson();
    String json = gson.toJson(new C("bar"));
    assertEquals("\"jsonAdapterFactory\"", json);
    C c = gson.fromJson("\"bar\"", C.class);
    assertEquals("jsonAdapterFactory", c.value);
  }

  public void testRegisteredAdapterOverridesJsonAdapter() {
    TypeAdapter<A> typeAdapter = new TypeAdapter<A>() {
      @Override public void write(JsonWriter out, A value) throws IOException {
        out.value("registeredAdapter");
      }
      @Override public A read(JsonReader in) throws IOException {
        return new A(in.nextString());
      }
    };
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(A.class, typeAdapter)
      .create();
    String json = gson.toJson(new A("abcd"));
    assertEquals("\"registeredAdapter\"", json);
  }

  /**
   * The serializer overrides field adapter, but for deserializer the fieldAdapter is used.
   */
  public void testRegisteredSerializerOverridesJsonAdapter() {
    JsonSerializer<A> serializer = new JsonSerializer<A>() {
      public JsonElement serialize(A src, Type typeOfSrc,
          JsonSerializationContext context) {
        return new JsonPrimitive("registeredSerializer");
      }
    };
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(A.class, serializer)
      .create();
    String json = gson.toJson(new A("abcd"));
    assertEquals("\"registeredSerializer\"", json);
    A target = gson.fromJson("abcd", A.class);
    assertEquals("jsonAdapter", target.value);
  }

  /**
   * The deserializer overrides Json adapter, but for serializer the jsonAdapter is used.
   */
  public void testRegisteredDeserializerOverridesJsonAdapter() {
    JsonDeserializer<A> deserializer = new JsonDeserializer<A>() {
      public A deserialize(JsonElement json, Type typeOfT,
          JsonDeserializationContext context) throws JsonParseException {
        return new A("registeredDeserializer");
      }
    };
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(A.class, deserializer)
      .create();
    String json = gson.toJson(new A("abcd"));
    assertEquals("\"jsonAdapter\"", json);
    A target = gson.fromJson("abcd", A.class);
    assertEquals("registeredDeserializer", target.value);
  }

  public void testIncorrectTypeAdapterFails() {
    try {
      String json = new Gson().toJson(new ClassWithIncorrectJsonAdapter("bar"));
      fail(json);
    } catch (ClassCastException expected) {}
  }

  public void testSuperclassTypeAdapterNotInvoked() {
    String json = new Gson().toJson(new B("bar"));
    assertFalse(json.contains("jsonAdapter"));
  }

  public void testNullSafeObjectFromJson() {
    Gson gson = new Gson();
    NullableClass fromJson = gson.fromJson("null", NullableClass.class);
    assertNull(fromJson);
  }

  @JsonAdapter(A.JsonAdapter.class)
  private static class A {
    final String value;
    A(String value) {
      this.value = value;
    }
    static final class JsonAdapter extends TypeAdapter<A> {
      @Override public void write(JsonWriter out, A value) throws IOException {
        out.value("jsonAdapter");
      }
      @Override public A read(JsonReader in) throws IOException {
        in.nextString();
        return new A("jsonAdapter");
      }
    }
  }

  @JsonAdapter(C.JsonAdapterFactory.class)
  private static class C {
    final String value;
    C(String value) {
      this.value = value;
    }
    static final class JsonAdapterFactory implements TypeAdapterFactory {
      @Override public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
        return new TypeAdapter<T>() {
          @Override public void write(JsonWriter out, T value) throws IOException {
            out.value("jsonAdapterFactory");
          }
          @SuppressWarnings("unchecked")
          @Override public T read(JsonReader in) throws IOException {
            in.nextString();
            return (T) new C("jsonAdapterFactory");
          }
        };
      }
    }
  }

  private static final class B extends A {
    B(String value) {
      super(value);
    }
  }
  // Note that the type is NOT TypeAdapter<ClassWithIncorrectJsonAdapter> so this
  // should cause error
  @JsonAdapter(A.JsonAdapter.class)
  private static final class ClassWithIncorrectJsonAdapter {
    @SuppressWarnings("unused") final String value;
    ClassWithIncorrectJsonAdapter(String value) {
      this.value = value;
    }
  }

  // This class is used in JsonAdapter Javadoc as an example
  @JsonAdapter(UserJsonAdapter.class)
  private static class User {
    final String firstName, lastName;
    User(String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
    }
  }
  private static class UserJsonAdapter extends TypeAdapter<User> {
    @Override public void write(JsonWriter out, User user) throws IOException {
      // implement write: combine firstName and lastName into name
      out.beginObject();
      out.name("name");
      out.value(user.firstName + " " + user.lastName);
      out.endObject();
      // implement the write method
    }
    @Override public User read(JsonReader in) throws IOException {
      // implement read: split name into firstName and lastName
      in.beginObject();
      in.nextName();
      String[] nameParts = in.nextString().split(" ");
      in.endObject();
      return new User(nameParts[0], nameParts[1]);
    }
  }

  @JsonAdapter(value = NullableClassJsonAdapter.class)
  private static class NullableClass {
  }

  private static class NullableClassJsonAdapter extends TypeAdapter<NullableClass> {
    @Override
    public void write(JsonWriter out, NullableClass value) throws IOException {
      out.value("nullable");
    }

    @Override
    public NullableClass read(JsonReader in) throws IOException {
      in.nextString();
      return new NullableClass();
    }
  }

  @JsonAdapter(FooJsonAdapter.class)
  private static enum Foo { BAR, BAZ }
  private static class FooJsonAdapter extends TypeAdapter<Foo> {
    @Override public void write(JsonWriter out, Foo value) throws IOException {
      out.value(value.name().toLowerCase(Locale.US));
    }

    @Override public Foo read(JsonReader in) throws IOException {
      return Foo.valueOf(in.nextString().toUpperCase(Locale.US));
    }
  }

  public void testIncorrectJsonAdapterType() {
    try {
      new Gson().toJson(new D());
      fail();
    } catch (IllegalArgumentException expected) {}
  }
  @JsonAdapter(Integer.class)
  private static final class D {
    @SuppressWarnings("unused") final String value = "a";
  }
}
