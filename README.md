# MyBike
myBike ofo(v1 . 0)
===========================================

###########environment gardle
baidu MAP -jni


###########build target

1. according to gradle to build 

here is my gardle information: 
com.android.tools.build:gradle:3.1.2

2.   compileSdkVersion 27
      defaultConfig {
        applicationId "com.example.administrator.mybike"
        minSdkVersion 23
        targetSdkVersion 27
        versionCode 1
        versionName "1.0"
        }
        you need to update this information in build.gradle
        
 3. notice the sdk.dir in local.properties
 
 4. important !!!!------notice that you should notify the manifests your key 
    
--------------------
 ###########directories instruction information
 
 
 

  <br>|--content</br>
   <br> |   |--UserCode</br>
  <br>  |   |--UserInfo</br>
  <br>  |--network</br>
  <br>  |   |--OkhttpHelper</br>
  <br>  |--widget</br>
  <br>      |--CommonDialogs</br>
   <br>     |--HeadZoomScrollView</br>
   <br> |--BikeApp</br>
  <br>  |--CircleImageView</br>
  <br>  |--CommonUtil</br>
  <br>  |--Constants</br>
  <br>  |--ConstantUtil</br>
   <br> |--HomePageFragment</br>
   <br> |--LoginActivity</br>
   <br> |--MainActivity</br>
   <br> |--MapLocationLister</br>
   <br> |--MapLocationInfo</br>
  <br>  |--MessageLocationEvent</br>
  <br>  |--MyLocationListener</br>
  <br>  |--PerferenceUtil</br>
  <br>  |--RegisterActivity</br>
   <br> |--RxBaseActivity</br>
   <br> |--RxLazyFragment</br>
   <br> |--ToastUtil</br>
   <br> |--UserFragment</br>
   <br> |--UserInformActivity</br>
  
  ------------------------
   ########### Using frame
   
   
   BaiduMap jni jar     support.design    ophttp3     butterknife     gosn     eventbus
   -----------------------
    
  ###########       BUGS
  <h1>WARINING!!!!!!</h1>
  1. here some problem in this bike ,when your register in the app I put this data in the preferenceUtils ,it can cause many bugs 
   when you clear this data 
   and you search in the User Information , 
   
   2. i write all the location and the map  information  in the  HomePageFragment , it looks mass and
    you can clear this information and encapsulation
    
    3. （important） when you get the car i write everythings in the preference but it doesn't work when you exit the app
    and find it can't  register the car and can't borrow and can't reback because of it prefernce , Connecting to the http://
    and get the data then put this information in the adapter  that   is  the current ways to write 
   


-------------------------------------------------------------

    <img src="/app/src/main/assets/register.jpg" width="200" hegiht="200" align=center />
    <img src="/app/src/main/assets/login.jpg" width="200" hegiht="200" align=center />
    <img src="/app/src/main/assets/UserInformation1.jpg" width="200" hegiht="200" align=center />
    <img src="/app/src/main/assets/bar.jpg" width="200" hegiht="200" align=center />
    <img src="/app/src/main/assets/infowindow.jpg" width="200" hegiht="200" align=center />
    <img src="/app/src/main/assets/map.jpg" width="200" hegiht="200" align=center />
    <img src="/app/src/main/assets/myBike.jpg" width="200" hegiht="200" align=center />
    <img src="/app/src/main/assets/return.png" width="200" hegiht="200" align=center />
    <img src="/app/src/main/assets/backcars.png" width="200" hegiht="200" align=center />
     <img src="/app/src/main/assets/returnsuccess.png" width="200" hegiht="200" align=center />
      <img src="/app/src/main/assets/time.png" width="200" hegiht="200" align=center />
       <img src="/app/src/main/assets/userInform.png" width="200" hegiht="200" align=center />

-------------------------------------------














-------------

GNU make is fully documented in the GNU Make manual, which is contained
in this distribution as the file make.texinfo.  You can also find
on-line and preformatted (PostScript and DVI) versions at the FSF's web
site.  There is information there about ordering hardcopy documentation.
 here is my information ,when you some question you can ask me 
   QQ：747608835@qq.com
   
   
   
