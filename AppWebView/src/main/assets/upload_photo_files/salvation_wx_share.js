
wxtitle = "抗癌卫士全程化救助申报表单test";
wxdesc = "抗癌卫士联合互惠理财网启动抗癌卫士第二期全程化救助行动，希望帮助病友们找到正确的抗癌方法，不走错路，少走弯路，抗癌成功！";
wxlink = "http://taoyanran.duapp.com/kaws/salvation/salvation.html";
wximgUrl = "http://taoyanran.duapp.com/kaws/salvation/images/kaxws_qrcode.png";
url = "http://taoyanran.duapp.com/kaws/salvation/salvation.html";

window.addEventListener('load', onloadFun, false);
function onloadFun() {

  $.ajax({
    async: false,
    url: "http://support.kangaiweishi.com/wx_api",
    type: "GET",
    timeout: 5000,
    beforeSend: function () {
    },
    success: function (json) {
      wx.config({
        debug: false,
        appId: json.appId,
        timestamp: json.timestamp,
        nonceStr: json.nonceStr,
        signature: json.signature,
        jsApiList: [
          'checkJsApi',
          'onMenuShareTimeline',
          'onMenuShareAppMessage'
        ]
      });

      wx.ready(function () {
        wx.onMenuShareAppMessage({
          title: wxtitle,
          desc: wxdesc,
          link: wxlink,
          imgUrl: wximgUrl,
          trigger: function (res) {

          },
          success: function (res) {
            share("好友");
            // alert("分享成功"); 分享给好友
          },
          cancel: function (res) {
            // alert("cancel");
          },
          fail: function (res) {

          }
        });

        wx.onMenuShareTimeline({
          title: wxtitle,
          link: wxlink,
          desc: wxdesc,
          imgUrl: wximgUrl,
          trigger: function (res) {

          },
          success: function (res) {
            share("朋友圈");
            // alert("ok"); 分享到朋友圈
          },
          cancel: function (res) {
            // alert("cancel");
          },
          fail: function (res) {
            // alert("fail");
          }
        });
      });
    },
    complete: function (XMLHttpRequest, textStatus) {

    },
    error: function (xhr) {
      //alert(xhr.errMsg);
    }
  });
}

function wx_share(wxtitle, wxdesc, wxlink, wximgUrl) {
  onloadFun();
}

wx_share(wxtitle, wxdesc, wxlink, wximgUrl);
