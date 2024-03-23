package com.yc.common.encypt;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.yc.appencryptlib.AesEncryptUtils;
import com.yc.appencryptlib.Rc4EncryptUtils;
import com.yc.appencryptlib.RsaEncryptUtils;
import com.yc.appencryptlib.Base64Utils;
import com.yc.appencryptlib.DesEncryptUtils;
import com.yc.toolutils.file.AppFileUtils;
import com.yc.toolutils.stream.BufferIoUtils;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.appencryptlib.Md5EncryptUtils;
import com.yc.toolutils.system.AppLogUtils;

import java.io.File;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


public class EncyptActivity extends BaseActivity implements View.OnClickListener {

    private RoundTextView tvView1;
    private RoundTextView tvView2;
    private RoundTextView tvView3;
    private RoundTextView tvView4;
    private RoundTextView tvView5;
    private RoundTextView tvView6;
    private RoundTextView tvView7;
    private RoundTextView tvView8;
    private RoundTextView tvView9;
    private RoundTextView tvView10;
    private RoundTextView tvView11;
    private RoundTextView tvView12;
    private ImageView ivImageView;

    @Override
    public int getContentView() {
        return R.layout.activity_base_text_view;
    }

    @Override
    public void initView() {
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        tvView4 = findViewById(R.id.tv_view_4);
        tvView5 = findViewById(R.id.tv_view_5);
        tvView6 = findViewById(R.id.tv_view_6);
        tvView7 = findViewById(R.id.tv_view_7);
        tvView8 = findViewById(R.id.tv_view_8);
        tvView9 = findViewById(R.id.tv_view_9);
        tvView10 = findViewById(R.id.tv_view_10);
        tvView11 = findViewById(R.id.tv_view_11);
        tvView12 = findViewById(R.id.tv_view_12);
        ivImageView = findViewById(R.id.iv_image_view);
    }

    @Override
    public void initListener() {
        tvView1.setOnClickListener(this);
        tvView2.setOnClickListener(this);
        tvView3.setOnClickListener(this);
        tvView4.setOnClickListener(this);
        tvView5.setOnClickListener(this);
        tvView6.setOnClickListener(this);
        tvView7.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData() {
        writeFile();
        tvView1.setText("1.MD5相关加密案例");
        tvView2.setText("2.base64相关加解密案例");
        tvView3.setText("3.DES加解密案例");
        tvView4.setText("4.AES加解密案例");
        tvView5.setText("5.RSA加解密案例");
        tvView6.setText("6.RC4加解密案例");
    }

    private void writeFile() {
        String txt = AppFileUtils.getExternalFilePath(this, "txt");
        String content = getString().toString();
        String fileName = txt + File.separator + "yc1.txt";
        AppLogUtils.d("FileActivity : 写文件 路径" , fileName);
        BufferIoUtils.writeString2File2(content,fileName);
    }

    private StringBuilder getString(){
        return new StringBuilder("2你好这个是写入的文件。明确一个前提：各个业务组件之间不会是相互隔离而是必然存在一些交互的；\n" +
                "\n" +
                "业务复用：在Module A需要引用Module B提供的某个功能，比如需要版本更新业务逻辑，而我们一般都是使用强引用的Class显式的调用；\n" +
                "业务复用：在Module A需要调用Module B提供的某个方法，例如别的Module调用用户模块退出登录的方法；\n" +
                "业务获取参数：登陆环境下，在Module A，C，D，E多个业务组件需要拿到Module B登陆注册组件中用户信息id，name，info等参数访问接口数据；\n" +
                "作者：杨充\n" +
                "链接：https://juejin.cn/post/6938008708295163934\n" +
                "来源：稀土掘金\n" +
                "逗比2 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。");
    }

    @Override
    public void onClick(View v) {
        if (v == tvView1) {
            md5();
        } else if (v == tvView2) {
            base64();
        } else if (v == tvView3) {
            des();
        } else if (v == tvView4) {
            aes();
        } else if (v == tvView5) {
            rsa();
        } else if (v == tvView6){
            rc4();
        } else if (v == tvView7){

        }
    }

    private void md5() {
        String string = "yangchong";
        //对字符串md5加密
        String md1 = Md5EncryptUtils.getMd5(string);
        AppLogUtils.d("md5计算字符串1: " + md1);
        //对字符串md5加密
        String md2 = Md5EncryptUtils.encryptMD5ToString(string);
        AppLogUtils.d("md5计算字符串2: " + md2);
        //对字符串md5加密，加盐处理
        String md3 = Md5EncryptUtils.encryptMD5ToString(string,"doubi");
        AppLogUtils.d("md5计算字符串，加盐处理3: " + md3);
        byte[] bytes = string.getBytes();
        //对字节数据md5加密
        String md4 = Md5EncryptUtils.encryptMD5ToString(bytes);
        AppLogUtils.d("md5计算字节数组4: " + md4);
        //对字节数据md5加密，加盐处理
        String md5 = Md5EncryptUtils.encryptMD5ToString(bytes,"doubi".getBytes());
        AppLogUtils.d("md5计算字节数组，加盐处理5: " + md5);

        String txt = AppFileUtils.getExternalFilePath(this, "txt");
        //对文件进行md5加密
        String md6 = Md5EncryptUtils.encryptMD5File1(txt);
        AppLogUtils.d("md5计算文件路径6: " + md6);
        //对文件进行md5加密
        String md7 = Md5EncryptUtils.encryptMD5File2(new File(txt));
        AppLogUtils.d("md5计算文件File7: " + md7);

        String fileName = txt + File.separator + "yc1.txt";
        String md8 = Md5EncryptUtils.encryptMD5File1(fileName);
        AppLogUtils.d("md5计算文件txt路径8: " + md8);
        String md9 = Md5EncryptUtils.encryptMD5File2(new File(fileName));
        AppLogUtils.d("md5计算文件txt路径9: " + md9);
    }

    private void base64() {
        // 加密传入的数据是byte类型的。并不是使用decode方法将原始数据转二进制。String类型的数据 使用 str.getBytes()就可以
        String str = "yangchong";
        // 在这里使用的是encode方式。返回的是byte类型加密数据，可使用new String转为String类型
        byte[] encode = Base64.encode(str.getBytes(), Base64.DEFAULT);
        String strBase64 = new String(encode);
        AppLogUtils.i("base64", "encode >>>" + strBase64);
        // 这里 encodeToString 则直接将返回String类型的加密数据
        String enToStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        AppLogUtils.i("base64", "encodeToString >>> " + enToStr);
        // 对base64加密后的数据进行解密
        AppLogUtils.i("base64", "decode >>>" + new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT)));

        //字符Base64加密
        String strBase64_2 = Base64Utils.encodeToString(str);
        AppLogUtils.i("base64", "encode 2 >>>" + strBase64_2);
        //字符Base64解密
        String strBase64_3 = Base64Utils.decodeToString(strBase64_2);
        AppLogUtils.i("base64", "decode 3 >>>" + strBase64_3);
    }


    private void des() {
        String string = "yangchongyangchongyangchongyangchong";
        String password = "yc123456";
        //DES加密字符串
        String encrypt1 = DesEncryptUtils.encrypt(string,password);
        AppLogUtils.d("des计算加密字符串1: " + encrypt1);
        //DES解密字符串
        String decrypt1 = DesEncryptUtils.decrypt(encrypt1 , password);
        AppLogUtils.d("des计算解密字符串1: " + decrypt1);


        String txt = AppFileUtils.getExternalFilePath(this, "txt");
        String fileName = txt + File.separator + "yc1.txt";
        String destFile = txt + File.separator + "yc2.txt";
        //DES加密文件
        String encryptFile1 = DesEncryptUtils.encryptFile(password, fileName, destFile);
        AppLogUtils.d("des计算加密文件1: " + encryptFile1);
        String destFile3 = txt + File.separator + "yc3.txt";
        //DES解密文件
        String decryptFile1 = DesEncryptUtils.decryptFile(password, destFile, destFile3);
        AppLogUtils.d("des计算解密文件1: " + decryptFile1);


        //DES 加密后转为 Base64 编码
        String encrypt2 = DesEncryptUtils.encrypt(string.getBytes(), password.getBytes());
        AppLogUtils.d("des计算加密字符串2: " + encrypt2);
        //DES解密字符串 Base64 解码
        String decrypt2 = DesEncryptUtils.decrypt(encrypt2.getBytes(), password.getBytes());
        AppLogUtils.d("des计算解密字符串2: " + decrypt2);
    }

    private void aes() {
        String string = "yangchongyangchongyangchongyangchong";
        String password = "yc123456yc123456";
        //AES加密字符串
        String encrypt1 = AesEncryptUtils.encrypt(string,password);
        AppLogUtils.d("aes计算加密字符串1: " + encrypt1);
        //AES解密字符串
        String decrypt1 = AesEncryptUtils.decrypt(encrypt1 , password);
        AppLogUtils.d("aes计算解密字符串1: " + decrypt1);


        String encrypt2 = AesEncryptUtils.encryptToBase64(string,password);
        AppLogUtils.d("aes计算加密字符串2: " + encrypt2);
        String decrypt2 = AesEncryptUtils.decryptFromBase64(encrypt2 , password);
        AppLogUtils.d("aes计算解密字符串2: " + decrypt2);


//        String encrypt3 = AesEncryptUtils.encryptWithKeyBase64(string,password);
//        AppLogUtils.d("aes计算加密字符串3: " + encrypt3);
//        String decrypt3 = AesEncryptUtils.decryptWithKeyBase64(encrypt3 , password);
//        AppLogUtils.d("aes计算解密字符串3: " + decrypt3);


//        String encrypt4 = AesEncryptUtils.encryptAES(string.getBytes(),password.getBytes());
//        AppLogUtils.d("aes计算加密字符串4: " + encrypt4);
//        String decrypt4 = AesEncryptUtils.decryptAES(encrypt4.getBytes() , password.getBytes());
//        AppLogUtils.d("aes计算解密字符串4: " + decrypt4);
    }

    private class YC{
        private int age;
        private String name;
        private String info;

        public void setAge(int age) {
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    private void rsa() {
        YC yc = new YC();
        yc.setAge(25);
        yc.setName("杨充");
        StringBuilder stringBuilder = new StringBuilder();
        for(int a=0 ; a<500 ; a++){
            stringBuilder.append("都比小杨"+a);
        }
        yc.setInfo(stringBuilder.toString());
        String string = yc.toString();
        //String string = "yangyangyangyangyangyangyangyangyangyangyangyangyangyangyangyangyangyangyangyangyangyang";
        //公钥加密，私钥解密
        //秘钥默认长度
        final int DEFAULT_KEY_SIZE = 1024;
        KeyPair keyPair = RsaEncryptUtils.generateRSAKeyPair(DEFAULT_KEY_SIZE);
        RSAPublicKey publicKey;
        RSAPrivateKey privateKey;
        if (keyPair != null) {
            // 公钥
            publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            privateKey = (RSAPrivateKey) keyPair.getPrivate();

            //用公钥对字符串进行加密
            String encrypt1;
            try {
                byte[] bytes = RsaEncryptUtils.encryptByPublicKey(string.getBytes(), publicKey.getEncoded());
                encrypt1 = new String(bytes);
                AppLogUtils.d("RSA计算加密字符串1: " + encrypt1);
            } catch (Exception e) {
                e.printStackTrace();
                encrypt1 = string;
                AppLogUtils.d("RSA计算加密字符串1: " + e.getMessage());
            }

            //使用私钥进行解密
            try {
                byte[] bytes = RsaEncryptUtils.decryptByPrivateKey(encrypt1.getBytes(), privateKey.getEncoded());
                String s = new String(bytes);
                AppLogUtils.d("RSA计算解密字符串1: " + s);
                //解密后得到的数据：yangchong
            } catch (Exception e) {
                e.printStackTrace();
                AppLogUtils.d("RSA计算解密字符串1: " + e.getMessage());
            }
        }
    }

    private void rc4() {
        String string = "yangchongyangchongyangchongyangchong";
        String secretKey = "yc123456";
        YC yc = new YC();
        yc.setAge(25);
        yc.setName("杨充");
        StringBuilder stringBuilder = new StringBuilder();
        for(int a=0 ; a<500 ; a++){
            stringBuilder.append("都比小杨"+a);
        }
        yc.setInfo(stringBuilder.toString());

        //RC4加密
        String encrypt1 = Rc4EncryptUtils.encryptString(string, secretKey);
        AppLogUtils.d("rc4计算加密字符串1: " + encrypt1);
        //RC4解密
        String decrypt1 = Rc4EncryptUtils.decryptString(encrypt1, secretKey);
        AppLogUtils.d("rc4计算解密字符串1: " + decrypt1);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg_cloud_night);
        byte[] bytes1 = bitmap.getNinePatchChunk();
        //RC4加密base64编码数据
        String encrypt2 = Rc4EncryptUtils.encryptToBase64(bytes1, secretKey);
        AppLogUtils.d("rc4计算加密字符串2: " + encrypt2);
        //RC4解密base64解码数据
        byte[] bytes = Rc4EncryptUtils.decryptFromBase64(encrypt2, secretKey);
        if (bytes!=null){
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ivImageView.setImageBitmap(image);
        }
    }

}
