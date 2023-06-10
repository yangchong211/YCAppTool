package com.yc.smencryptlib.sm2;


import com.yc.smencryptlib.Util;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class Cipher {
    private int ct;
    private ECPoint p2;
    private SM3Digest sm3keybase;
    private SM3Digest sm3c3;
    private byte key[];
    private byte keyOff;

    public Cipher()
    {
        this.ct = 1;
        this.key = new byte[32];
        this.keyOff = 0;
    }

    private void Reset()
    {
        this.sm3keybase = new SM3Digest();
        this.sm3c3 = new SM3Digest();

        byte p[] = Util.byteConvert32Bytes(p2.getX().toBigInteger());
        this.sm3keybase.update(p, 0, p.length);
        this.sm3c3.update(p, 0, p.length);

        p = Util.byteConvert32Bytes(p2.getY().toBigInteger());
        this.sm3keybase.update(p, 0, p.length);
        this.ct = 1;
        NextKey();
    }

    private void NextKey()
    {
        SM3Digest sm3keycur = new SM3Digest(this.sm3keybase);
        sm3keycur.update((byte) (ct >> 24 & 0xff));
        sm3keycur.update((byte) (ct >> 16 & 0xff));
        sm3keycur.update((byte) (ct >> 8 & 0xff));
        sm3keycur.update((byte) (ct & 0xff));
        sm3keycur.doFinal(key, 0);
        this.keyOff = 0;
        this.ct++;
    }

    public ECPoint Init_enc(SM2 sm2, ECPoint userKey)
    {
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger k = ecpriv.getD();
        ECPoint c1 = ecpub.getQ();
        this.p2 = userKey.multiply(k);
        Reset();
        return c1;
    }

    public void Encrypt(byte data[])
    {
        this.sm3c3.update(data, 0, data.length);
        for (int i = 0; i < data.length; i++)
        {
            if (keyOff == key.length)
            {
                NextKey();
            }
            data[i] ^= key[keyOff++];
        }
    }

    public void Init_dec(BigInteger userD, ECPoint c1)
    {
        this.p2 = c1.multiply(userD);
        Reset();
    }

    public void Decrypt(byte data[])
    {
        for (int i = 0; i < data.length; i++)
        {
            if (keyOff == key.length)
            {
                NextKey();
            }
            data[i] ^= key[keyOff++];
        }

        this.sm3c3.update(data, 0, data.length);
    }

    public void Dofinal(byte c3[])
    {
        byte p[] = Util.byteConvert32Bytes(p2.getY().toBigInteger());
        this.sm3c3.update(p, 0, p.length);
        this.sm3c3.doFinal(c3, 0);
        Reset();
    }
}
