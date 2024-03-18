package com.yc.appgrpc;

import android.util.Log;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

public class ProtoJsonTest {


    public static class JsonProtoTest {

        private static final String TAG = "Test 效率测试：";

        public void gson(){
            AddressBook addressBook = getObject();
            long l = System.currentTimeMillis();
            Gson gson = new Gson();
            String data = gson.toJson(addressBook);
            byte[] bytes = data.getBytes();
            StringBuilder sb = new StringBuilder();
            sb.append(TAG).append("\n");
            sb.append("Gson 序列化耗时：").append(System.currentTimeMillis() - l).append("\n");
            sb.append("Gson 序列化数据大小：").append(bytes.length).append("\n");
            l = System.currentTimeMillis();
            AddressBook addressBook1 = gson.fromJson(new String(bytes), AddressBook.class);
            Log.e(TAG, "Gson 反序列化耗时：" + (System.currentTimeMillis() - l));
            Log.e(TAG, "Gson 数据：" + data);
        }

        private AddressBook getObject(){
            AddressBook addressBook = new AddressBook();

            AddressBook.PhoneNumber p_110 = new AddressBook.PhoneNumber();
            p_110.setNumber("110");
            p_110.setType(AddressBook.PhoneNumber.PhoneType.HOME);
            AddressBook.Person zs = new AddressBook.Person();
            zs.setId(1);
            zs.setName("张三");
            zs.addPhones(p_110);
            addressBook.addPersons(zs);

            AddressBook.PhoneNumber p_120 = new AddressBook.PhoneNumber();
            p_120.setNumber("120");
            p_120.setType(AddressBook.PhoneNumber.PhoneType.MOBILE);
            AddressBook.Person ls = new AddressBook.Person();
            ls.setId(2);
            ls.setName("李四");
            ls.addPhones(p_120);
            addressBook.addPersons(ls);

            AddressBook.PhoneNumber p_130 = new AddressBook.PhoneNumber();
            p_120.setNumber("130");
            p_120.setType(AddressBook.PhoneNumber.PhoneType.WORK);
            AddressBook.Person ys = new AddressBook.Person();
            ys.setId(3);
            ys.setName("王五");
            ys.addPhones(p_130);
            addressBook.addPersons(ys);

            return addressBook;
        }


        public void protoTest() {
            AddressBookProto.Person.PhoneNumber.Builder builder
                    = AddressBookProto.Person.PhoneNumber.newBuilder()
                    .setNumber("110")
                    .setType(AddressBookProto.Person.PhoneType.HOME);
            AddressBookProto.Person.Builder zs = AddressBookProto.Person.newBuilder()
                    .setName("张三")
                    .setId(1)
                    .addPhones(builder);

            AddressBookProto.Person.PhoneNumber.Builder builder1
                    = AddressBookProto.Person.PhoneNumber.newBuilder()
                    .setNumber("120")
                    .setType(AddressBookProto.Person.PhoneType.MOBILE);
            AddressBookProto.Person.Builder ls = AddressBookProto.Person.newBuilder()
                    .setName("李四")
                    .setId(2)
                    .addPhones(builder1);

            AddressBookProto.Person.PhoneNumber.Builder builder2 = AddressBookProto.Person.PhoneNumber.newBuilder()
                    .setNumber("130").setType(AddressBookProto.Person.PhoneType.WORK);
            AddressBookProto.Person.Builder ys = AddressBookProto.Person.newBuilder().
                    setName("王五")
                    .setId(3)
                    .addPhones(builder2);

            AddressBookProto.AddressBook addressBook = AddressBookProto.AddressBook.newBuilder()
                    .addPeople(zs)
                    .addPeople(ls)
                    .addPeople(ys)
                    .build();

            long l = System.currentTimeMillis();
            //序列化
            byte[] bytes = addressBook.toByteArray();
            Log.e(TAG, "protobuf 序列化耗时：" + (System.currentTimeMillis() - l));
            Log.e(TAG, "protobuf 序列化数据大小：" + bytes.length);
            try {
                l = System.currentTimeMillis();
                //反序列化
                AddressBookProto.AddressBook book = AddressBookProto.AddressBook.parseFrom(bytes);
                Log.e(TAG, "protobuf 反序列化耗时：" + (System.currentTimeMillis() - l));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                Log.e(TAG, "protobuf 反序列化异常");
            }
            String string = addressBook.toString();
            Log.e(TAG, "protobuf 数据：" + string);
        }


        public void testSearchRequest() {
            AddressBookProto.SearchRequest.Builder builder = AddressBookProto.SearchRequest.newBuilder();
            builder.setQuery("1");
            builder.setPageNumber(2);
            builder.setResultPerPage(3);
            AddressBookProto.SearchRequest searchRequest = builder.build();
            Log.e(TAG, "protobuf 数据：" + searchRequest.toString());
            //序列化
            byte[] array = searchRequest.toByteArray();
            //反序列化
            try {
                AddressBookProto.SearchRequest request = AddressBookProto.SearchRequest.parseFrom(array);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        }
    }




    public static class AddressBook {
        List<AddressBook.Person> persons;

        public AddressBook() {
            this.persons = new ArrayList<>();
        }

        public void addPersons(AddressBook.Person person) {
            persons.add(person);
        }

        public List<AddressBook.Person> getPersons( ) {
            return persons;
        }

        public static class Person {
            private String name;
            private int id;
            private String email;

            private List<AddressBook.PhoneNumber> phones;

            public Person() {
                phones = new ArrayList<>();
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }


            public void addPhones(AddressBook.PhoneNumber number) {
                phones.add(number);
            }

            public List<AddressBook.PhoneNumber> getPhones() {
                return phones;
            }
        }

        public static class PhoneNumber {
            public enum PhoneType {
                MOBILE,
                HOME,
                WORK;
            }

            private String number;
            private AddressBook.PhoneNumber.PhoneType type;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public AddressBook.PhoneNumber.PhoneType getType() {
                return type;
            }

            public void setType(AddressBook.PhoneNumber.PhoneType type) {
                this.type = type;
            }
        }
    }


}
