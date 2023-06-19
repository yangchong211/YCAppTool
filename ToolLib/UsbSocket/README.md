# 简单版本socket通信
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念介绍



### 02.常见思路和做法



### 03.Api调用说明




### 04.遇到的坑分析




### 05.其他问题说明



### 06.参考博客说明





---------------------------- PROCESS STARTED (19955) for package com.tencent.wx.app ----------------------------
2023-06-13 14:07:06.931 19955-19955 MainActivity: |         com.tencent.wx.app                   V  网络请求开始
2023-06-13 14:07:06.931 19955-19955 MainActivity: |         com.tencent.wx.app                   V  getXakUnionPayKey：2D7E7C96-DAC5-4526-96C3-C60CDEC4B120
2023-06-13 14:07:06.931 19955-19955 MainActivity: |         com.tencent.wx.app                   V  网络请求结束-0
2023-06-13 14:07:06.932 19955-19955 MainActivity: |         com.tencent.wx.app                   V  getBobiChinaPayPrivateKey：MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5WOP+rTn8AlTJ/T9azyquxmkzWS8pbcIobxdtDKX96VT7DXHq7OuD1gGzX11FHI6F773h5hC6F99bWj8QaKS4PMwR8SguImmuSFMsygyIIKfjyR8JEXPZw5nJ/lCHps28QvLLqmNp+/ikIjMhlfaYvrPd/QF+tn7cmOwuS1ZuQwIDAQAB
2023-06-13 14:07:06.932 19955-19955 MainActivity: |         com.tencent.wx.app                   V  getBobiChinaPayWxPrivateKey：MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKjEGxqvSMR8bxyoWjLgUrsB0z9gMPB3i7NUOPMzMWar1dX44eeqJGXhYHHOYa1xSP15+Vh0oTxqR0mHG2IC+OEtWOm9t/vwoJCbABtUFfeefUI4S37rZ4gsGsiT2Ha083DWukNnZp6blW+HG4SWEDaV/hfMHdDu52sYJjsglEEJAgMBAAECgYBKcRYzkxXZ9bldPrVqs6BKdOelkNyFdXXdyEzGmdI0o59THtEmytn7RoqwKITVaJ71kBP4fJmSeJ6xRDND7GOy2Fq+d3Pqy/rqv9v/5ARWvp2qw3lqsD0Whg4IWjWFa45LOQ/T/0hyCbyg9+DY3mUNljkjFcUkdvhYI/9rFevewQJBANBeTA9rWqNCW3v0ZWoW0NE8AQiqxfc1cULoNWFkQp+QvG5WWt3wLNokET/aQm7yKPn/6rCpfMcWnVasrhT/Uz0CQQDPWEi3vHCPvCjpSP5+vBJoVPkOJtli7ddDjlV9ErAf1nobkRkuI5Qz3rrMSV0UrjlKvpSxZZK2B9vHto6Xi9G9AkBVMsls4Jt998sKYwL4nv2hICTYE2PA0gROtD3nZaRsNDUn9H1RFDxysZ6v+iGB1dh1aHPg6lxP12pcH11Kzp+NAkAoA/nEu/FdTAr5OsUi4FkNGZkXShgaJ5yrxQRqpvimrFXUjCQ361XmzM1wL5hvLVCRREaeFeRCjTG1UV4jkTBhAkAMhJU4NzTo8pU4nzQxuPoZs9ihpUzkU2VNxXxhZhWR4eQz+dGUaYM+K79sMcgVfM/3VyTyUOBZQuZfKvj1yOsL
2023-06-13 14:07:06.932 19955-19955 MainActivity: |         com.tencent.wx.app                   V  getBobiChinaPayWxPublicKey：MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoxBsar0jEfG8cqFoy4FK7AdM/YDDwd4uzVDjzMzFmq9XV+OHnqiRl4WBxzmGtcUj9eflYdKE8akdJhxtiAvjhLVjpvbf78KCQmwAbVBX3nn1COEt+62eILBrIk9h2tPNw1rpDZ2aem5VvhxuElhA2lf4XzB3Q7udrGCY7IJRBCQIDAQAB
2023-06-13 14:07:06.932 19955-19955 MainActivity: |         com.tencent.wx.app                   V  getTxItAppKey：A4E7D47EEA7E8D68
2023-06-13 14:07:06.932 19955-19955 MainActivity: |         com.tencent.wx.app                   V  getTxItSecreteKey：7C4AD41E7FB3FA20DAEC86E7FBDCB295
2023-06-13 14:07:06.939 19955-19955 MainActivity: |         com.tencent.wx.app                   V  decryptQrCode：{"code":0,"data":{"card_number":"1122334415","is_offline":0,"identity_type":"1"}}
2023-06-13 14:07:06.939 19955-19955 MainActivity: |         com.tencent.wx.app                   V  getFingerprint：1438D74F7B9EF56795E1AB1C2BCCC771
2023-06-13 14:07:06.939 19955-19955 MainActivity: |         com.tencent.wx.app                   V  getTxCardSign：7c17b4c640ca525c71f00047b86ed59a
2023-06-13 14:07:06.939 19955-19955 MainActivity: |         com.tencent.wx.app                   V  刷掌证书：l0pAgyhoRNdZB5zMmV51HjDLJ27jBXQC31VSjoSLX0P0Ax60itttYM3Q5qJDzkJtvkZ9LZa0PJy0HXkmgGHpqHQ5frGF8zvgqRGfhH9w6M4vmGJAYgECOqMg+lklf2Yl63sIdZv77N4EfgjRx9DMUqEPJ4gJOQLc2dVtPjxXM+8X5ss4l6R927vAwR+i4NEoj5JBlEoy5WJ4uR+DD56CYiHLmjXLA1MDIjJV+NgpX2ov90Ba62iaBIM/QXx1fV0s01tvDP8ii7TDNjjf05PF5g5a0Lqxp4v1A6L7cilT9CXlJLvN+0PGKONao0B5SKFTOjdlMcy5VZ6k8EtZ+iBggNE0TwzbNlBm7bizawLVsj9hoJOlEcE+vIu43x+NXYpa49WgNoMRqtAe2Gq3trndpUl0dccsh8jkHOEo9jhCaQRGWWjPmG3aBtKJerHXBgvHyFJrP5Mn4ecITuYhdwHPVHsGjyKJ3Tp4rnsLN2+09DQjDzW3kxpottzjyauYpiTJW/6/Cl7xIvNQGPoqpNjVJmLGcbDo2hTnxjBe8V1+soPa5Z1uwtqsGf3/GUZNgW6NSYeS08pRAHnGr/puQWuG/IedBKFSn04TH48B/W2eVppkjFhUEXTGZJ4akVPHSZPT/DwOo9FvDIWHfumYlYUCkjLD+BNN/0yDl4r0K1yPg/ZkY7nfibbE+hF4H/yhVKevrRs1UhbLMgA47++cQsjMueHTk8CNyFmiYclcHB0dWg/5uKDsyV25mZMiI0TU3oPs0oUZXsnZujynAIwK/nArYvW01XCrPeTU/CrlFmHnTJ1JrX6oog6hRJb5vKle+lOXCI80cXJD7YkMycubyAqoPqyEvabGfnDVQAbAI2Cc/m2pG1LqNX35ZSHLo91UFZumgE2nBlUhJBgygSJaDl7seg2ulVHpZta1MxgeVIELuRF7YewpoPrITqxef7IoH+lp74w1I69S+jCqAw7k/gaP7NXBNUjTaVb3Gihi3amQm5neyaEBSuM8yL7zASQ8DuPQR86jGiNpEoKZyRqWh60XNAtG2iQUX24mCoIFtZPo7RaiRDRTtmkG+g6H4S386+GbcQT4ZsLEqNduzYqd5x7o+92+tQc10A38urCPanMmUCw2hIiuXAuLAg==
2023-06-13 14:07:06.939 19955-19955 MainActivity: |         com.tencent.wx.app                   V  刷掌证书密码：760d04ab948159c6
2023-06-13 14:07:06.940 19955-19955 MainActivity: |         com.tencent.wx.app                   V  国密2的秘钥对：Sm2Key{publicKey='', privateKey=''}
2023-06-13 14:07:06.945 19955-19971 MainActivity: |         com.tencent.wx.app                   V  decryptQrCode-多线程1：{"code":0,"data":{"card_number":"1122334415","is_offline":0,"identity_type":"1"}}
2023-06-13 14:07:07.079 19955-19955 MainActivity: |         com.tencent.wx.app                   V  m1CardMessage：NfcResult{cardType=1, result=1, cardId='5b9bc141', resultMsg='解卡成功', studentId='REG196372400000024', userName='韦炳亿', sign='1bdf90ad971e95b6474ea53373bdf42b', date='220523', crc32Data=3674316042, totalMoney=2, cardDailyNum=2, version=0, totalCount=33, csTotalMoney=0, stTotalMoney=0}
2023-06-13 14:07:07.079 19955-19955 MainActivity: |         com.tencent.wx.app                   V  cpuCardMessage：NfcResult{cardType=0, result=1, cardId='2bdbce60', resultMsg='解卡成功', studentId='13760282032', userName='小尹', sign='12b9d3410f4b1038e9ff2af403d13713', date='220520', crc32Data=364747492, totalMoney=1, cardDailyNum=1, version=1, totalCount=0, csTotalMoney=2, stTotalMoney=1}
2023-06-13 14:07:07.085 19955-19955 MainActivity: |         com.tencent.wx.app                   V  cardKey：ByteArrayResult{code=1, msg='解密成功', byteArray=200ea89d0e3bb0261f2cf2c76e2ccd7f}
2023-06-13 14:07:07.085 19955-19955 MainActivity: |         com.tencent.wx.app                   V  获取Saas的公钥：-----BEGIN PUBLIC KEY-----
                                                                                                    MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEtXU76EBlVbgzEVBSPVE3mcyCLe7Q
                                                                                                    pLwyJWYanW2aKYJzM4J6WgR4pTowYYny/shcHHVgrgffeduhUdL+uSmDqA==
                                                                                                    -----END PUBLIC KEY-----
2023-06-13 14:07:07.085 19955-19955 MainActivity: |         com.tencent.wx.app                   E  sm2AuthCode：
2023-06-13 14:07:07.085 19955-19955 MainActivity: |         com.tencent.wx.app                   V  获取Saas的RSA公钥：-----BEGIN RSA PUBLIC KEY-----
                                                                                                    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr9IJZc+ZvcRcecQspeuT
                                                                                                    P98YiWoaByudAa89S+1bdaqtmfLgl4ZN+7KudYkdsBzvn2ZVJUWcvFRr513MHjIm
                                                                                                    rmNHqX5TB9lTlO28Y+N9L9TyZF6tgtHtJxp4PEiQdPAXVcxsbElL17RxG9El5gIl
                                                                                                    5BYmNwwWcs581XhbhJxdlAqkAmefvu3BL5uSGugiogPmvdz/Nrb5QvPI4r8up+lr
                                                                                                    zwzQltquTSedUsGeqNX5AJZQ9rMnXY1LDbnzgIeAdqccbyswnE18O6Fbb6PcBCcW
                                                                                                    74MGG5XnV6PkpNd/0wKx+o4V+bPXrVWYp+MzcXDGrY4321PUdYr4+AHKMIpkz2Lj
                                                                                                    lwIDAQAB
                                                                                                    -----END RSA PUBLIC KEY-----
2023-06-13 14:07:07.089 19955-19955 MainActivity: |         com.tencent.wx.app                   E  rsaAuthCode：142F6F40FA5378CBCF624B67A209E71015A98E6DFAF4C23F6F1D19EC04F11EF54C518B76D5B33405B1206E87B5D77FFD792BC56C8F115C8005CC4A2161645199064585278E73FE77C209803E72D62F5E7F6EF246CFD19BF8C1A3A17AB5760275B13E6027DB5BE8482E29057BAF9A917D3D28C9A68BD03D06C753560007D316AF5EC4823062EF5584154F64EBF32D12D010CF2D09B501C47F45964859F5A17E71E5BF7B7BC395B1C243D4C9EC49017D183FB75D5B1C2C579301ACA0BE6DBE91E0D505253C6D6FBA85FC6FF88E41C6D2AA7433EA2A14E85D7F61142E87417535CB080866CA1913E474D3CA5446D6B9F37FC4FCFC4098EB0FE0A5FE680887CAF62A,1
2023-06-13 14:07:07.112 19955-19955 MainActivity: |         com.tencent.wx.app                   E  RSA解密出来的硬件信息：qwertyuiopasdfghjklzxcvbnm123456
2023-06-13 14:07:07.112 19955-19955 MainActivity: |         com.tencent.wx.app                   E  aesEncrypt：z_cjcmjerpy
2023-06-13 14:07:07.112 19955-19955 MainActivity: |         com.tencent.wx.app                   E  aesDecrypt：v_yfyifanlu
2023-06-13 14:07:07.443   774-795   ActivityManager         system_process                       I  Displayed com.tencent.wx.app/.MainActivity: +649ms
2023-06-13 14:07:07.936 19955-19972 MainActivity: |         com.tencent.wx.app                   V  decryptQrCode-多线程2：{"code":0,"data":{"card_number":"1122334415","is_offline":0,"identity_type":"1"}}
2023-06-13 14:07:08.088 19955-19986 MainActivity: |         com.tencent.wx.app                   V  m1CardMessage：NfcResult{cardType=1, result=1, cardId='5b9bc141', resultMsg='解卡成功', studentId='REG196372400000024', userName='韦炳亿', sign='1bdf90ad971e95b6474ea53373bdf42b', date='220523', crc32Data=3674316042, totalMoney=2, cardDailyNum=2, version=0, totalCount=33, csTotalMoney=0, stTotalMoney=0}
2023-06-13 14:07:08.088 19955-19986 MainActivity: |         com.tencent.wx.app                   V  cpuCardMessage：NfcResult{cardType=0, result=1, cardId='2bdbce60', resultMsg='解卡成功', studentId='13760282032', userName='小尹', sign='12b9d3410f4b1038e9ff2af403d13713', date='220520', crc32Data=364747492, totalMoney=1, cardDailyNum=1, version=1, totalCount=0, csTotalMoney=2, stTotalMoney=1}


