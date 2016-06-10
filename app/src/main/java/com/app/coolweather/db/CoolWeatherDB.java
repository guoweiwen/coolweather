package com.app.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.coolweather.model.City;
import com.app.coolweather.model.County;
import com.app.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端数据库操作 CRUD
 */
public class CoolWeatherDB {

    /**
     * 数据库名
     * */
    public static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     * */
    public static final int VERSION = 1;

    private SQLiteDatabase db;

    //单例模式
    private static CoolWeatherDB coolWeatherDB;

    public static CoolWeatherDB  getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 将Province实例存储到数据库中
     * */
    public void saveProvince(Province province){
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    /**
     * 从数据库读取全国省份的信息
     * */
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
            if(cursor !=  null){
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 将City储存到数据库里
     * */
    public void saveCity(City city){
        if(city != null){
            ContentValues contentValue = new ContentValues();
            contentValue.put("id",city.getId());
            contentValue.put("city_name",city.getCityName());
            contentValue.put("city_code",city.getCityCode());
            contentValue.put("province_id",city.getProvinceId());
            db.insert("City",null,contentValue);
        }
    }

    /**
     * 读取某省的所有城市信息
     * */
    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null,null);
        if(cursor.moveToNext()){
            do{
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("city_id")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(city);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return list;
    }

    /**
     * 将county实例存储到数据库
     * */
    public void saveCounty(County county){
        if(county != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("city_name",county.getCountyText());
            contentValues.put("city_code",county.getCountyCode());
            contentValues.put("city_id",county.getCityId());
            db.insert("County",null,contentValues);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息
     * */
    public List<County> loadCounties(int cityId) {
        Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null, null);
        List<County> list = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("city_code")));
                county.setCountyText(cursor.getString(cursor.getColumnIndex("city_name")));
                list.add(county);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }


}
