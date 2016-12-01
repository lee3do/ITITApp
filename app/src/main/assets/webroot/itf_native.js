var ItfNative = {	
	/**
	 * 初始化web模拟页面
	 */
	init:function(){
        var userAgent = navigator.userAgent;
        if(userAgent.indexOf('itit')!=-1){
            return;
        }
       //
		var header=document.createElement('div');
		header.innerHTML='<div id="itf-debug-frame" style="height:40px;width:100%;background-color:#eee;border-bottom:1px solid #ccc;position:fixed;top:0;left:0;padding-top:10px">'+
		'<div id="itf-left-button" style="width:33%;display:inline-block"></div>'+
		'<div id="itf-center-title" style="width:33%;display:inline-block;text-align:center"></div>'+
		'<div id="itf-right-button" style="width:33%;display:inline-block;text-align:right"></div>'
		+'</div>';
		document.body.appendChild(header);
		document.body.style["padding-top"]='40px';
		document.getElementById('itf-center-title').innerHTML=document.title;
        //
        try{
            eval("onITFNativeLoaded()");
        } catch (e) {}
        
	},	
    /**
     *设置全局消息回调函数
     */
    setMessageCallback: function(callback) {
        if(window.itf){
            itf.setMessageCallback(callback);
        }else{
            console.log("setMessageCallback:"+callback);
        }
    },
    //
    /**
     *设置全局消息回调函数
     */
    postMessage: function(msg) {
        if(window.itf){
            itf.postMessage(msg);
        }else{
            console.log("postMessage:"+msg);
        }
    },
    //
	/**
     *返回系统平台
     */
	getPlatformType: function() {
        if(window.itf){
            return itf.getPlatformType();
        }
        return "Web";
	},
    /**
     *注册一个回调函数，当页面显示的时候调用
     */
    onAttach:function(callback){
        if(window.itf){
            itf.onAttach(callback);
        }else{
            console.log("call itf.onAttach "+callback);
        }
    },
    /**
     *显示警告对话框
     */
    showAlertWithTitleMessage:function(title,message){
        if(window.itf){
            itf.showAlertWithTitleMessage(title,message);
        }else{
            alert(title+","+message);
        }
    },
    /**
     *记录日志
     */
    log:function(info){
        if(window.itf){
            itf.log(info);
        }else{
            console.log(info);
        }
    },
    /**
     *震动
     */
    vibrate:function(){
        if(window.itf){
            itf.vibrate();
        }else{
            alert('vibrate');
        }
    },
    /**
     *获取变量
     */
    getVariable:function(key){
        if(window.itf){
           return itf.getVariable(key);
        }else{
           return ItfNative[key];
        }
    },
    /**
     *设置变量
     */
    setVariableWithKeyValue:function(key,value){
        if(window.itf){
           itf.setVariableWithKeyValue(key,value);
        }else{
           ItfNative[key]=value;
        }
    },
    
    /**
     *获取存储的变量
     */
    getPersistence:function(key){
        if(window.itf){
           return itf.getPersistence(key);
        }else{
           return localStorage[key];
        }
    },
    /**
     *设置存储变量
     */
    savePersistenceWithKeyValue:function(key,value){
        if(window.itf){
           itf.savePersistenceWithKeyValue(key,value);
        }else{
        	localStorage[key]=value;
        }
    },
    /**
     * 弹窗显示新页面
     */
    presentView:function(url){
    	if(window.itf){
            itf.presentView(url);
         }else{
            window.location.href="/"+url;
         }
    },
    
    /**
     * 关闭弹窗显示页面
     */
    closePresentView:function(){
    	if(window.itf){
            itf.closePresentView();
         }else{
            window.close();
         }
    },
    
    /**
     * 本页面加载地址
     */
    loadView:function(url){
        if(window.itf){
            itf.loadView(url);
        }else{
            window.location.href="/"+url;
        }
    },
    
    /**
     * 堆栈打开新页面
     */
    pushView:function(url){
    	if(window.itf){
            itf.pushView(url);
         }else{
            window.location.href="/"+url;
         }
    },
    /**
     * 弹出新页面
     */
    popView:function(){
    	if(window.itf){
            itf.popView();
         }else{
        	window.location.back();
         }
    },
    /**
     * 弹出新页面
     */
    popToRootView:function(){
    	if(window.itf){
            itf.popToRootView();
         }else{
        	 console.log("pop to root view");
        	 window.location.back();
         }
    },
    /**
     * 设置标题
     */
    setViewTitle:function(title){
    	if(window.itf){
            itf.setViewTitle(title);
        }else{
        	document.getElementById('itf-center-title').innerHTML=title;
        	window.title=title;
        }
    },
    /**
     * 设置标题图片
     */
    setViewTitleImage:function(imageFile){
        if(window.itf){
            itf.setViewTitleImage(imageFile);
        }else{
            console.log("setViewTitleImage "+imageFile);
        }
    },
    /**
     * 获取GPS坐标
     */
    getGeoLocation:function(callback){
    	if(window.itf){
            itf.getGeoLocation(callback);
        }else{
        	var s=callback+"({latitude:22.5487,longitude:113.93413,course:1,speed:1,altitude:1})";
        	eval(s);
        }
    },
    /**
     * 显示图片
     */
    showImageViewer:function(url){
    	if(window.itf){
            itf.showImageViewer(url);
        }else{
        	window.location.href=url;
        }
    },
    /**
     * 显示图片
     */
    showImagesViewer:function(url){
        if(window.itf){
            itf.showImagesViewer(url);
        }else{
            window.location.href=url[0];
        }
    },
    /**
     * 设置窗口多标题
     */
    setViewWithTitlesCallback:function(titles,callback){
    	if(window.itf){
            itf.setViewWithTitlesCallback(titles,callback);
        }else{
        	window.title=titles[0];
        	console.log('set view titles '+callback);
        }
    },
    /**
     * 扫描二维码
     */
    scanQRCode:function(callback){
    	if(window.itf){
            itf.scanQRCode(callback);
        }else{
        	console.log('scan qrcode '+callback);
           	eval(callback+"('http://io.itit.io')");
        }
    },
    
    /**
     * 显示toast
     */
    toast:function(info){
    	if(window.itf){
            itf.toast(info);
        }else{
        	alert(info);
        }
    },
    
    
    setNavRightButtonsWithTitlesTypes:function(titles,types,callback){
        if(window.itf){
            itf.setNavRightButtonsWithTitlesTypesCallback(titles,types,callback);
        }else{
            var btn=document.getElementById('itf-right-button');
            btn.innerHTML=titles.join(',');
            btn.setAttribute('onclick',callback+"()")
        }
    },
    setNavRightButtonsEnable:function(title,enable){
        if(window.itf){
            itf.setNavRightButtonsEnableWithTitleEnable(title,enable);
        }else{
            var btn=document.getElementById('itf-right-button');
            if(!enable){
                btn.style["color"]='#ddd';
            }else{
                btn.style["color"]='#000';
            }
        }
    },
    /**
     * 设置导航栏按钮
     */
    setNavRightButtonWithTitleCallback:function(title,callback){
    	if(window.itf){
            itf.setNavRightButtonWithTitleCallback(title,callback);
        }else{
        	var btn=document.getElementById('itf-right-button');
        	btn.innerHTML=title;
        	btn.setAttribute('onclick',callback+"()")
        }
    },
    /**
     * 设置导航栏按钮
     */
    setNavRightButtonWithImageCallback:function(image,callback){
        if(window.itf){
            itf.setNavRightButtonWithImageCallback(image,callback);
        }else{
            console.log('setNavRightButtonWithImageCallback '+image+" "+callback);
        }
    },
    
    /**
     * 设置导航栏按钮是否可用
     */
    setNavRightButtonEnabled:function(enable){
    	if(window.itf){
            itf.setNavRightButtonEnabled(enable);
        }else{
        	var btn=document.getElementById('itf-right-button');
        	if(!enable){
        		btn.style["color"]='#ddd';
        	}else{
        		btn.style["color"]='#000';
            }
        }
    },
   
    /**
     * 移出导航栏按钮
     */
    removeNavRightButton:function(){
    	if(window.itf){
            itf.removeNavRightButton();
        }else{
        	var btn=document.getElementById('itf-right-button');
        	btn.innerHTML="";
        	btn.removeAttribute('onclick')
        }
    },
    
    /**
     * 设置导航栏按钮
     */
    setNavLeftButtonWithTitleCallback:function(title,callback){
    	if(window.itf){
            itf.setNavLeftButtonWithTitleCallback(title,callback);
        }else{
        	var btn=document.getElementById('itf-left-button');
        	btn.innerHTML=title;
        	btn.setAttribute('onclick',callback+"()")
        }
    },
    /**
     * 设置导航栏按钮
     */
    setNavLeftButtonWithImageCallback:function(image,callback){
        if(window.itf){
            itf.setNavLeftButtonWithImageCallback(image,callback);
        }else{
            console.log('setNavLeftButtonWithImageCallback '+image+" "+callback);
        }
    },
    
    /**
     * 设置导航栏按钮是否可用
     */
    setNavLeftButtonEnabled:function(enable){
    	if(window.itf){
            itf.setNavLeftButtonEnabled(enable);
        }else{
        	var btn=document.getElementById('itf-left-button');
        	if(!enable){
        		btn.style["color"]='#ddd';
        	}else{
        		btn.style["color"]='#000';
            }
        }
    },
   
    /**
     * 移出导航栏按钮
     */
    removeNavLeftButton:function(){
    	if(window.itf){
            itf.removeNavLeftButton();
        }else{
        	var btn=document.getElementById('itf-left-button');
        	btn.innerHTML="";
        	btn.removeAttribute('onclick')
        }
    },
    /**
     * 设置导航栏按钮
     */
    setNavBarVisible:function(visable){
        if(window.itf){
            itf.setNavBarVisible(visable);
        }else{
            console.log('setNavBarVisible '+visable);
        }
    },
    /**
     * 上传相册图片
     */
    uploadImageWithUrl:function(url,callback){
        if(window.itf){
            itf.uploadImageWithUrlCallback(url,callback);
        }else{
            console.log('uploadImageWithUrl');
        }
    },
    /**
     * 上传相册图片
     */
    uploadAlbumImageWithUrl:function(url,callback){
    	if(window.itf){
            itf.uploadAlbumImageWithUrlCallback(url,callback);
        }else{
        	console.log('uploadAlbumImageWithUrl');
        }
    },
    /**
     * 上传照相机图片
     */
    uploadCameraImageWithUrl:function(url,callback){
    	if(window.itf){
            itf.uploadCameraImageWithUrlCallback(url,callback);
        }else{
        	console.log('uploadCameraImageWithUrl');
        }
    },
    //上传相机视频
    uploadCameraVideoWithUrl: function(url,callback) {
        if(window.itf){
            itf.uploadCameraVideoWithUrlCallback(url,callback);
        }else{
            console.log("uploadCameraVideoWithUrl");
        }
    },
    /**
     * 显示地址薄
     */
    showAddressBookPeoplePicker:function(callback){
    	if(window.itf){
            itf.showAddressBookPeoplePicker(callback);
        }else{
        	eval(callback+"({fullName:'测试',phones:'123,456'})");
        	console.log('showAddressBookPeoplePicker');
        }
    },
    /**
     * 打开系统设置
     */
    systemOpenURL:function(url){
    	if(window.itf){
            itf.systemOpenURL(url);
        }else{
        	console.log('systemOpenUrl:'+url);
        }
    },
    systemCanOpenURL:function(url){
    	if(window.itf){
    		return itf.systemCanOpenURL(url);
        }else{
        	return true;
        }
    },
    /**
     * 打开地图
     */
    showMapViewerWithTitleRemarkLatitudeLongitude:function(title,desc,la,lo){
    	if(window.itf){
            itf.showMapViewerWithTitleRemarkLatitudeLongitude(title,desc,la,lo);
        }else{
        	console.log('showMapViewerWithTitleRemarkLatitudeLongitude:'+title);
        }
    },
    /**
     * 
     */
    systemOpenAppSettings:function(){
    	if(window.itf){
            itf.systemOpenAppSettings();
        }else{
        	console.log('systemOpenAppSettings:');
        }
    },
    /**
     * 列出Document目录
     */
    listDirectory:function(){
    	if(window.itf){
            return itf.listDirectory();
        }else{
        	return ["test"];
        }
    },
    /**
     * 清空path
     */
    clearDocumentDirectory:function(path){
    	if(window.itf){
           itf.clearDocumentDirectory(path);
        }else{
        	console.log("clearDocumentDirectory"+path);
        }
    },
    /**
     * 删除path
     */
    removeDocumentDirectory:function(path){
    	if(window.itf){
           itf.removeDocumentDirectory(path);
        }else{
        	console.log("removeDocumentDirectory"+path);
        }
    },
    /**
    *清空缓存目录
    */
    clearCacheDirectory:function(callback){
       if(window.itf){
           itf.clearCacheDirectory(callback);
        }else{
            console.log("clearCacheDirectory"+callback);
            eval(callback+'()');
        } 
    },
    /**
    *得到缓存目录大小
    */
    getCacheDirectorySize:function(callback){
       if(window.itf){
           itf.getCacheDirectorySize(callback);
        }else{
            console.log("getCacheDirectorySize"+callback);
            eval(callback+'(0)');
        } 
    },
    /**
     * 获取当前www目录版本
     */
    getCurrentWWWVersion:function(){
    	if(window.itf){
            return itf.getCurrentWWWVersion();
        }else{
        	return 0;
        }
    },
    /**
     * 弹出action sheet
     */
    showActionSheetWithTitleMessageActionsCallback:function(title,desc,actions,callback){
    	if(window.itf){
            itf.showActionSheetWithTitleMessageActionsCallback(title,desc,actions,callback);
        }else{
        	console.log("showActionSheetWithTitleMessageActionsCallback"+title);
        }
    },
    /**
     * 设置回弹
     */
    setEnableBounces:function(enable){
    	if(window.itf){
            itf.setEnableBounces(enable);
        }else{
        	console.log("setEnableBounces"+enable);
        }
    },
    /**
     * 设置背景色
     */
    setBackgroundColor:function(color){
    	if(window.itf){
            itf.setBackgroundColor(color);
        }else{
        	console.log("setBackgroundColor"+color);
        }
    },
    
    /**
     * 设置SessionId
     */
    setSessionId:function(id){
    	if(window.itf){
            itf.setSessionId(id);
        }else{
        	localStorage["ITFSESSIONID"]=id;
        }
    },
    

    /**
     * 获取SessionId
     */
    getSessionId:function(){
    	if(window.itf){
            return itf.getSessionId();
        }else{
        	return localStorage["ITFSESSIONID"];
        }
    },
    
    databaseQuery:function(sql){
        if(window.itf){
            return JSON.parse(itf.databaseQuery(sql));
        }else{
            console.log("database query:"+sql);
            return [];
        }
    },
    
    databaseExecute:function(sql){
        if(window.itf){
            return itf.databaseExecute(sql);
        }else{
            console.log("database execute:"+sql);
            return "0";
        }
    },
    //Voice
    showRecordViewWithUrl: function(url,callback) {
        if(window.itf){
            itf.showRecordViewWithUrlCallback(url,callback);
        }else{
            console.log("showRecordViewWithUrl");
        }
    },
    startRecord: function() {
        if(window.itf){
            itf.startRecord();
        }else{
            console.log("startRecord");
        }
    },
    stopRecord: function() {
        if(window.itf){
            return itf.stopRecord();
        }else{
            console.log("stopRecord");
        }
    },
    onVoiceRecordEnd: function(callback) {
        if(window.itf){
            itf.onVoiceRecordEnd(callback);
        }else{
            console.log("onVoiceRecordEnd");
        }
    },
    playVoice: function(localId) {
        if(window.itf){
            itf.playVoice(localId);
        }else{
            console.log("playVoice");
        }
    },
    pauseVoice: function(localId) {
        if(window.itf){
            itf.pauseVoice(localId);
        }else{
            console.log("pauseVoice");
        }
    },
    stopVoice: function(localId) {
        if(window.itf){
            itf.stopVoice(localId);
        }else{
            console.log("stopVoice");
        }
    },
    onVoicePlayEnd: function(callback) {
        if(window.itf){
            itf.onVoicePlayEnd(callback);
        }else{
            console.log("onVoicePlayEnd");
        }
    },
    uploadVoiceWithUrl:function(url,callback){
        if(window.itf){
            itf.uploadVoiceWithUrlCallback(url,callback);
        }else{
            console.log('uploadVoice');
        }
    },
    /**
     *微信登录
     */
    weixinLogin: function(callback) {
        if(window.social){
            social.weixinLogin(callback);
        }else{
            console.log("weixin login "+callback);
        }
    },
    /**
     *微信登出
     */
    weixinLogout: function(callback) {
        if(window.social){
            social.weixinLogout(callback);
        }else{
            console.log("weixin logout "+callback);
        }
    },
    /**微信支付*/
    weixinPay: function(unifiedOrderUrl,callback) {
        if(window.social){
            social.weixinPay(unifiedOrderUrl,callback);
        }else{
            console.log("weixin pay unifiedOrderUrl:"+unifiedOrderUrl+",callback:"+callback);
        }
    }
    ,
    /**支付宝支付*/
    aliPay: function(url,sessionId,callback) {
        if(window.social){
            social.aliPay(url,sessionId,callback);
        }else{
            console.log("ali pay url:"+url+",callback:"+callback);
        }
    }
    ,
    /**分享*/
    socialShare: function(title,text,url,imageUrl,callback) {
        if(window.social){
            social.socialShare(title,text,url,imageUrl,callback);
        }else{
            console.log("socialShare title:"+title+",callback:"+callback);
        }
    },
    /**
    *显示系统图标上的角标数字
    */
    setApplicationIconBadgeNumber:function(number){
        if(window.itf){
            itf.setApplicationIconBadgeNumber(number);
        }else{
            console.log("setApplicationIconBadgeNumber "+number);
        }
    },
    /**
    *显示工具栏图标角标
    */
    setTabBarBadge:function(content){
        if(window.itf){
            itf.setTabBarBadge(content);
        }else{
            console.log("setTabBarBadge "+content);
        }
    }
}
//
window.ItfNative=ItfNative;

if (document.addEventListener) {
    document.addEventListener('DOMContentLoaded', function () {
    document.removeEventListener('DOMContentLoaded', arguments.callee, false);
    ItfNative.init();
    }, false)
}

