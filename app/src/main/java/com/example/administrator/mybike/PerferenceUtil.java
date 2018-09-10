package com.example.administrator.mybike;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class PerferenceUtil {

    /************************************************************
     * 注释: 定义一个静态私有变量(不初始化，不使用final关键字，
     * 使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
     ************************************************************/
    private static volatile PerferenceUtil instance;
    private Context mContext;

    private PerferenceUtil(Context context) {
        mContext = context;
    }

    public static PerferenceUtil getInstance(Context context) {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (PerferenceUtil.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new PerferenceUtil(context);
                }
            }
        }
        return instance;
    }

    public static void reset(final Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.clear();
        edit.apply();
    }

    public static String getString(String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance()).getString(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance()).getLong(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance()).getFloat(key, defValue);
    }

    public static void put(String key, String value) {
        putString(key, value);
    }

    public static void put(String key, int value) {
        putInt(key, value);
    }

    public static void put(String key, float value) {
        putFloat(key, value);
    }

    public static void put(String key, boolean value) {
        putBoolean(key, value);
    }

    private static void putFloat(String key, float value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance());
    }

    public static int getInt(String key, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance()).getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance()).getBoolean(key, defValue);
    }

    public static void putStringProcess(String key, String value) {
        SharedPreferences sharedPreferences = BikeApp.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringProcess(String key, String defValue) {
        SharedPreferences sharedPreferences = BikeApp.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getString(key, defValue);
    }

    public static boolean hasString(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance());
        return sharedPreferences.contains(key);
    }

    private static void putString(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putLong(String key, long value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BikeApp.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getString1(String keyName) {

        return  getString(keyName,"");
    }
    public static <T> PerferenceUtil putObj(String objKeyName, T object) {
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            // 创建对象输出流，并封装字节流
            oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符窜
            String obj_Base64 = new String(baos.toByteArray());
            putString(objKeyName, obj_Base64);

        } catch (IOException e) {
            // TODO Auto-generated
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Logger.d("ok", "存储成功");
        return instance;
    }

    /**
     * @param objKey
     *        存储该对象的键
     * @param value
     *        要获取的对象的值
     * @param <T>
     * @return
     */
    public static <T> T getObj(String objKey, T value) {

        String objectBase64 = getString1(objKey);
        Log.i("xxx", "getObj: " + objectBase64);
        //读取字节
        byte[] base64 = objectBase64.getBytes();

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        ObjectInputStream bis = null;
        try {
            //再次封装
            bis = new ObjectInputStream(bais);
            try {
                value = (T) bis.readObject();
                return value;
            } catch (ClassNotFoundException e) {

                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }




    public static void remove(String... keys) {
        if (keys != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                    BikeApp.getInstance());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }

    public static void removeAll(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                BikeApp.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
