  # update
  升级更新包
========================================================================================
  功能：
  1.进入app时SplashActivity弹出提示升级的Dialog
  2.可以点击下载，或是取消下载
  3.下载可以配置是Dialog形式或是Notification形式
  4.可以支持手动检查更新


  使用方法：

  1.在需要更新的页面（如SplashActivity）的OnCreate里面做如下操作：

  
  UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;   //通过Dialog来进行下载
  //UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;通过通知栏来进行下载(默认)

  //url 填写服务器提供的检查更新的接口
  UpdateFunGO.init(this, url, null);
		
		

 2.在onResume里添加如下代码：
 UpdateFunGO.onResume(this);
 
 3.在OnStop里面添加如下代码：
 UpdateFunGO.onStop(this);
 
 4.在软件包里面的Update.java里根据你服务器返回的接口的实际情况，做具体的修改。下面是实例：
 
   private void interpretingData(String result) {
   // 		JSONObject object = new JSONObject(result);
   
   //            Log.i("OK",object.toString());
   
   //            DownloadKey.version = object.optInt("version");
   
   //            DownloadKey.apkUrl = object.getString("installUrl");
   
   //            DownloadKey.versionName = object.getString("versionShort");
   
   }
 
上面4步完成了最简单的调用，会弹出升级框和下载框
================================================================================================

如果在你的SplashActivity里需要获取是否需要升级的状态的回调，再根据状态做一些业务操作，此时你可以这么做：
  
	public void checkUpdate() {
      //下载方式:
        UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;   //通过Dialog来进行下载
      //UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;通过通知栏来进行下载(默认)

        UpdateFunGO.init(this, url,updateListener);
    }


    UpdateListener updateListener = new UpdateListener() {
        @Override
        public void OnUpdateListener(int flag) {
			updateHandler.sendEmptyMessage(flag);
        }
    }

    Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int result = (Integer)msg.obj;
			            switch (result){
                case 1:
                   //需要升级     To do what you want
                    break;
                case 2:
					//网络不好    To do what you want
                    break;
                case 3:
                  //已是最新版本  To do what you want
                    break;
            }
			
        }
    };
 
 ===================================================================================================
 
 本工具类是在  hugeterry的基础上改造而成的，去掉了里面对fir.im的依赖。谢谢大神的分享，向大神致敬。
 https://github.com/hugeterry/UpdateDemo
