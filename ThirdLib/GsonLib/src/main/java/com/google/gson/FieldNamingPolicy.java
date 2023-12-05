/*
 * Copyright (C) 2008 Google Inc.
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

package com.google.gson;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * An enumeration that defines a few standard naming conventions for JSON field names.
 * This enumeration should be used in conjunction with {@link com.google.gson.GsonBuilder}
 * to configure a {@link com.google.gson.Gson} instance to properly translate Java field
 * names into the desired JSON field names.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public enum FieldNamingPolicy implements FieldNamingStrategy {

  /**
   * Using this naming policy with Gson will ensure that the field name is
   * unchanged.
   */
  IDENTITY() {
    @Override public String translateName(Field f) {
      return f.getName();
    }
  },

  /**
   * Using this naming policy with Gson will ensure that the first "letter" of the Java
   * field name is capitalized when serialized to its JSON form.
   *
   * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
   * <ul>
   *   <li>someFieldName ---> SomeFieldName</li>
   *   <li>_someFieldName ---> _SomeFieldName</li>
   * </ul>
   */
  UPPER_CAMEL_CASE() {
    @Override public String translateName(Field f) {
      return upperCaseFirstLetter(f.getName());
    }
  },

  /**
   * Using this naming policy with Gson will ensure that the first "letter" of the Java
   * field name is capitalized when serialized to its JSON form and the words will be
   * separated by a space.
   *
   * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
   * <ul>
   *   <li>someFieldName ---> Some Field Name</li>
   *   <li>_someFieldName ---> _Some Field Name</li>
   * </ul>
   *
   * @since 1.4
   */
  UPPER_CAMEL_CASE_WITH_SPACES() {
    @Override public String translateName(Field f) {
      return upperCaseFirstLetter(separateCamelCase(f.getName(), " "));
    }
  },

  /**
   * Using this naming policy with Gson will modify the Java Field name from its camel cased
   * form to a lower case field name where each word is separated by an underscore (_).
   *
   * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
   * <ul>
   *   <li>someFieldName ---> some_field_name</li>
   *   <li>_someFieldName ---> _some_field_name</li>
   *   <li>aStringField ---> a_string_field</li>
   *   <li>aURL ---> a_u_r_l</li>
   * </ul>
   */
  LOWER_CASE_WITH_UNDERSCORES() {
    @Override public String translateName(Field f) {
      return separateCamelCase(f.getName(), "_").toLowerCase(Locale.ENGLISH);
    }
  },

  /**
   * Using this naming policy with Gson will modify the Java Field name from its camel cased
   * form to a lower case field name where each word is separated by a dash (-).
   *
   * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
   * <ul>
   *   <li>someFieldName ---> some-field-name</li>
   *   <li>_someFieldName ---> _some-field-name</li>
   *   <li>aStringField ---> a-string-field</li>
   *   <li>aURL ---> a-u-r-l</li>
   * </ul>
   * Using dashes in JavaScript is not recommended since dash is also used for a minus sign in
   * expressions. This requires that a field named with dashes is always accessed as a quoted
   * property like {@code myobject['my-field']}. Accessing it as an object field
   * {@code myobject.my-field} will result in an unintended javascript expression.
   * @since 1.4
   */
  LOWER_CASE_WITH_DASHES() {
    @Override public String translateName(Field f) {
      return separateCamelCase(f.getName(), "-").toLowerCase(Locale.ENGLISH);
    }
  },

  /**
   * Using this naming policy with Gson will modify the Java Field name from its camel cased
   * form to a lower case field name where each word is separated by a dot (.).
   *
   * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
   * <ul>
   *   <li>someFieldName ---> some.field.name</li>
   *   <li>_someFieldName ---> _some.field.name</li>
   *   <li>aStringField ---> a.string.field</li>
   *   <li>aURL ---> a.u.r.l</li>
   * </ul>
   * Using dots in JavaScript is not recommended since dot is also used for a member sign in
   * expressions. This requires that a field named with dots is always accessed as a quoted
   * property like {@code myobject['my.field']}. Accessing it as an object field
   * {@code myobject.my.field} will result in an unintended javascript expression.
   * @since 2.8
   */
  LOWER_CASE_WITH_DOTS() {
    @Override public String translateName(Field f) {
      return separateCamelCase(f.getName(), ".").toLowerCase(Locale.ENGLISH);
    }
  };

  /**
   * Converts the field name that uses camel-case define word separation into
   * separate words that are separated by the provided {@code separatorString}.
   */
  static String separateCamelCase(String name, String separator) {
    StringBuilder translation = new StringBuilder();
    for (int i = 0, length = name.length(); i < length; i++) {
      char character = name.charAt(i);
      if (Character.isUpperCase(character) && translation.length() != 0) {
        translation.append(separator);
      }
      translation.append(character);
    }
    return translation.toString();
  }

  /**
   * Ensures the JSON field names begins with an upper case letter.
   */
  static String upperCaseFirstLetter(String name) {
    int firstLetterIndex = 0;
    int limit = name.length() - 1;
    for(; !Character.isLetter(name.charAt(firstLetterIndex)) && firstLetterIndex < limit; ++firstLetterIndex);

    char firstLetter = name.charAt(firstLetterIndex);
    if(Character.isUpperCase(firstLetter)) { //The letter is already uppercased, return the original
      return name;
    }

    char uppercased = Character.toUpperCase(firstLetter);
    if(firstLetterIndex == 0) { //First character in the string is the first letter, saves 1 substring
      return uppercased + name.substring(1);
    }

    return name.substring(0, firstLetterIndex) + uppercased + name.substring(firstLetterIndex + 1);
  }
}
