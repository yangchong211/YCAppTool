package com.yc.designbusiness.structural.adapter;

public class AdapterDesign {

    /********************************************类适配器，主要是通过继承***********************************************/
    //类适配器，主要是通过继承
    public interface ITarget {
        void f1();
        void f2();
        void fc();
    }

    public class Adapter1 implements ITarget{

        @Override
        public void f1() {

        }

        @Override
        public void f2() {

        }

        @Override
        public void fc() {

        }
    }

    public class Adaptee{
        public void fa(){}
        public void fb(){}
        public void fc(){}
    }

    public class Adapter2 extends Adaptee implements ITarget{

        @Override
        public void f1() {
            fa();
        }

        @Override
        public void f2() {
            //...重新实现f2()...
        }
    }

    /*******************************************************************************************/
    /********************************************对象的适配器。通过组合来实现***********************************************/
    public class Adapter3 implements ITarget{
        private Adaptee adaptee;
        public Adapter3(Adaptee adaptee){
            this.adaptee = adaptee;
        }

        @Override
        public void f1() {
            //委托给Adaptee
            adaptee.fa();
        }

        @Override
        public void f2() {
            //...重新实现f2()...
        }

        @Override
        public void fc() {
            adaptee.fc();
        }
    }
    /*******************************************************************************************/


}
