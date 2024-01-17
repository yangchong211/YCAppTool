package com.yc.designbusiness;

public class AppDesignActivity {

    private static class Test{

        private static volatile Test test;

        private Test(){

        }

        public static Test getInstance1(){
            if (test == null){
                test = new Test();
            }
            return test;
        }

        public static synchronized Test getInstance2(){
            if (test == null){
                test = new Test();
            }
            return test;
        }

        public static synchronized Test getInstance3(){
            //避免不必要的同步
            if (test == null){
                synchronized (Test.class){
                    if (test == null){
                        test = new Test();
                    }
                }
            }
            return test;
        }

        /**
         * 恶汉式
         */
        private static final Test INSTANCE = new Test();

        public static synchronized Test getInstance4(){
            return INSTANCE;
        }

        public static synchronized Test getInstance6(){
            return Loader.INSTANCE;
        }

        public static class Loader{
            private static final Test INSTANCE = new Test();
        }

        /**
         * 懒汉式
         */
        public static Test getInstance5(){
            if (test == null){
                test = new Test();
            }
            return test;
        }



    }

}
