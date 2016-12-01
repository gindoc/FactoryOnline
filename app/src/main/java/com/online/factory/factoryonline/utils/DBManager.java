package com.online.factory.factoryonline.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.online.factory.factoryonline.models.Contacter;
import com.online.factory.factoryonline.models.Factory;
import com.online.factory.factoryonline.models.Tag;
import com.online.factory.factoryonline.models.WantedMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * 作者: GIndoc
 * 日期: 2016/11/21 16:29
 * 作用: SQLite 管理器
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;


    public DBManager(Context context) {
//        this.context = context;
        helper = new DBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
//        db = helper.getWritableDatabase();
    }

    public List<WantedMessage> queryWantedMessagesWithoutIds(int pageNo, List<Integer> id) {
        db = helper.getWritableDatabase();
        Cursor c = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (id != null) {
                for (int i : id) {
                    sb.append(",").append(i);
                }
            }
            String ids = "";
            if (sb.length() > 0) {
                ids = sb.toString().substring(1);
            }
            c = db.rawQuery("SELECT * FROM (WantedMessage INNER JOIN Factory ON factoryId = Factory.id)  " +
                    "INNER JOIN Contacter ON contacterId = Contacter.id AND WantedMessage.id NOT IN (?) ORDER BY id desc LIMIT 10 OFFSET ?",
                    new String[]{ids, String.valueOf(pageNo*10-10)});
            List<WantedMessage> wantedMessages = new ArrayList<>();
            WantedMessage wantedMessage;
            Factory factory;
            Contacter contacter;
            while (c.moveToNext()) {
                wantedMessage = new WantedMessage();
                factory = new Factory();
                contacter = new Contacter();

                factory.setId(c.getString(c.getColumnIndex("factoryId")));
                factory.setTitle(c.getString(c.getColumnIndex("title")));
                factory.setThumbnail_url(c.getString(c.getColumnIndex("thumbnail_url")));
                factory.setPrice(c.getFloat(c.getColumnIndex("price")));
                factory.setRange(c.getFloat(c.getColumnIndex("range")));
                String[] stringTags = c.getString(c.getColumnIndex("tags")).split(",");
                List<Tag> tags = new ArrayList<>();
                for (String stringTag : stringTags) {
                    Tag tag = new Tag();
                    tag.setName(stringTag);
                    tags.add(tag);
                }
                factory.setTags(tags);
                String[] stringUrls = c.getString(c.getColumnIndex("image_urls")).split(",");
                factory.setImage_urls(Arrays.asList(stringUrls));
                factory.setDescription(c.getString(c.getColumnIndex("description")));
                factory.setAddress_overview(c.getString(c.getColumnIndex("address_overview")));
                factory.setPre_pay(c.getString(c.getColumnIndex("pre_pay")));
                factory.setRent_type(c.getString(c.getColumnIndex("rent_type")));
                factory.setGeohash(c.getString(c.getColumnIndex("geohash")));

                contacter.setId(c.getString(c.getColumnIndex("contacterId")));
                contacter.setName(c.getString(c.getColumnIndex("name")));
                contacter.setPhone_num(c.getString(c.getColumnIndex("phone_num")));

                wantedMessage.setId(c.getString(c.getColumnIndex("id")));
                int i = c.getInt(c.getColumnIndex("isCollect"));
                boolean a = i != 0;
                wantedMessage.setCollect(a);
                wantedMessage.setCreated_time(c.getInt(c.getColumnIndex("created_time")));
                wantedMessage.setDelete_id(c.getString(c.getColumnIndex("delete_id")));
                wantedMessage.setOwner_id(c.getString(c.getColumnIndex("owner_id")));
                wantedMessage.setUpdate_id(c.getString(c.getColumnIndex("update_id")));
                wantedMessage.setUpdate_time(c.getInt(c.getColumnIndex("update_time")));
                wantedMessage.setFactory(factory);
                wantedMessage.setContacter(contacter);

                wantedMessages.add(wantedMessage);
            }
            return wantedMessages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
    }

    public List<WantedMessage> queryWantedMessages(int pageNo) {
        db = helper.getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM (WantedMessage INNER JOIN Factory ON factoryId = Factory.id)  " +
                    "INNER JOIN Contacter ON contacterId = Contacter.id ORDER BY id desc LIMIT 10 OFFSET ?", new String[]{String.valueOf(pageNo*10-10)});
            List<WantedMessage> wantedMessages = new ArrayList<>();
            WantedMessage wantedMessage;
            Factory factory;
            Contacter contacter;
            while (c.moveToNext()) {
                wantedMessage = new WantedMessage();
                factory = new Factory();
                contacter = new Contacter();

                factory.setId(c.getString(c.getColumnIndex("factoryId")));
                factory.setTitle(c.getString(c.getColumnIndex("title")));
                factory.setThumbnail_url(c.getString(c.getColumnIndex("thumbnail_url")));
                factory.setPrice(c.getFloat(c.getColumnIndex("price")));
                factory.setRange(c.getFloat(c.getColumnIndex("range")));
                String[] stringTags = c.getString(c.getColumnIndex("tags")).split(",");
                List<Tag> tags = new ArrayList<>();
                for (String stringTag : stringTags) {
                    Tag tag = new Tag();
                    tag.setName(stringTag);
                    tags.add(tag);
                }
                factory.setTags(tags);
                String[] stringUrls = c.getString(c.getColumnIndex("image_urls")).split(",");
                factory.setImage_urls(Arrays.asList(stringUrls));
                factory.setDescription(c.getString(c.getColumnIndex("description")));
                factory.setAddress_overview(c.getString(c.getColumnIndex("address_overview")));
                factory.setPre_pay(c.getString(c.getColumnIndex("pre_pay")));
                factory.setRent_type(c.getString(c.getColumnIndex("rent_type")));
                factory.setGeohash(c.getString(c.getColumnIndex("geohash")));

                contacter.setId(c.getString(c.getColumnIndex("contacterId")));
                contacter.setName(c.getString(c.getColumnIndex("name")));
                contacter.setPhone_num(c.getString(c.getColumnIndex("phone_num")));

                wantedMessage.setId(c.getString(c.getColumnIndex("id")));
                int i = c.getInt(c.getColumnIndex("isCollect"));
                boolean a = i != 0;
                wantedMessage.setCollect(a);
                wantedMessage.setCreated_time(c.getInt(c.getColumnIndex("created_time")));
                wantedMessage.setDelete_id(c.getString(c.getColumnIndex("delete_id")));
                wantedMessage.setOwner_id(c.getString(c.getColumnIndex("owner_id")));
                wantedMessage.setUpdate_id(c.getString(c.getColumnIndex("update_id")));
                wantedMessage.setUpdate_time(c.getInt(c.getColumnIndex("update_time")));
                wantedMessage.setFactory(factory);
                wantedMessage.setContacter(contacter);

                wantedMessages.add(wantedMessage);
            }
            return wantedMessages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
    }

    public void insertWantedMessages(List<WantedMessage> wantedMessages) {
        db = helper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i=0; i<wantedMessages.size();i++) {
                WantedMessage wantedMessage = wantedMessages.get(i);

                Factory factory = wantedMessage.getFactory();
                Contacter contacter = wantedMessage.getContacter();

                ContentValues values = new ContentValues();
                values.put("id", factory.getId());
                values.put("title", factory.getTitle());
                String thumbnail = factory.getThumbnail_url();
                values.put("thumbnail_url", thumbnail);
                values.put("price", factory.getPrice());
                values.put("range", factory.getRange());
                StringBuffer sb = new StringBuffer();
                for (Tag tag : factory.getTags()) {
                    sb.append(",");
                    sb.append(tag.getName());
                }
                values.put("tags", sb.toString().substring(1));
                sb = new StringBuffer();
                for (String stringUrl : factory.getImage_urls()) {
                    sb.append(",").append(stringUrl);
                }
                values.put("image_urls", sb.substring(1));
                values.put("description", factory.getDescription());
                values.put("address_overview", factory.getAddress_overview());
                values.put("pre_pay", factory.getPre_pay());
                values.put("rent_type", factory.getRent_type());
                values.put("geohash", factory.getGeohash());
                db.insert("Factory", null, values);

                ContentValues values2 = new ContentValues();
                values2.put("id", contacter.getId());
                values2.put("name", contacter.getName());
                values2.put("phone_num", contacter.getPhone_num());
                db.insert("Contacter", null, values2);

                ContentValues values3 = new ContentValues();
                values3.put("id", wantedMessage.getId());
                values3.put("isCollect", wantedMessage.isCollect());
                values3.put("created_time", wantedMessage.getCreated_time());
                values3.put("delete_id", wantedMessage.getDelete_id());
                values3.put("owner_id", wantedMessage.getOwner_id());
                values3.put("update_id", wantedMessage.getUpdate_id());
                values3.put("update_time", wantedMessage.getUpdate_time());
                values3.put("factoryId", factory.getId());
                values3.put("contacterId", contacter.getId());
                db.insert("WantedMessage", null, values3);

            }
            db.setTransactionSuccessful();  //设置事务成功完成

        } catch (Exception e) {
            Timber.e(e.getMessage());
            e.printStackTrace();
        }finally {
            db.endTransaction();    //结束事务
            db.close();
        }
    }

    public int queryMaxUpdateTime() {
        db = helper.getWritableDatabase();
        Cursor c = null;
        int maxUpdateTime = 0;
        try {
            c = db.rawQuery("SELECT max(update_time) FROM WantedMessage", null);
            while (c.moveToNext()) {
                maxUpdateTime = c.getInt(0);
            }
        } catch (Exception e) {
            Timber.e(e.getMessage());
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
            return maxUpdateTime;
        }
    }

    public void insertHomeWantedMessages(List<WantedMessage> wantedMessages) {
        db = helper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i=0; i<wantedMessages.size();i++) {
                WantedMessage wantedMessage = wantedMessages.get(i);

                Factory factory = wantedMessage.getFactory();
                Contacter contacter = wantedMessage.getContacter();

                ContentValues values = new ContentValues();
                values.put("id", factory.getId());
                values.put("title", factory.getTitle());
                String thumbnail = factory.getThumbnail_url();
                values.put("thumbnail_url", thumbnail);
                values.put("price", factory.getPrice());
                values.put("range", factory.getRange());
                StringBuffer sb = new StringBuffer();
                for (Tag tag : factory.getTags()) {
                    sb.append(",");
                    sb.append(tag.getName());
                }
                values.put("tags", sb.toString().substring(1));
                sb = new StringBuffer();
                for (String stringUrl : factory.getImage_urls()) {
                    sb.append(",").append(stringUrl);
                }
                values.put("image_urls", sb.substring(1));
                values.put("description", factory.getDescription());
                values.put("address_overview", factory.getAddress_overview());
                values.put("pre_pay", factory.getPre_pay());
                values.put("rent_type", factory.getRent_type());
                values.put("geohash", factory.getGeohash());
                db.insert("Factory", null, values);

                ContentValues values2 = new ContentValues();
                values2.put("id", contacter.getId());
                values2.put("name", contacter.getName());
                values2.put("phone_num", contacter.getPhone_num());
                db.insert("Contacter", null, values2);

                ContentValues values3 = new ContentValues();
                values3.put("id", wantedMessage.getId());
                values3.put("isCollect", wantedMessage.isCollect());
                values3.put("created_time", wantedMessage.getCreated_time());
                values3.put("delete_id", wantedMessage.getDelete_id());
                values3.put("owner_id", wantedMessage.getOwner_id());
                values3.put("update_id", wantedMessage.getUpdate_id());
                values3.put("update_time", wantedMessage.getUpdate_time());
                values3.put("factoryId", factory.getId());
                values3.put("contacterId", contacter.getId());
                db.insert("HomeWantedMessage", null, values3);

            }
            db.setTransactionSuccessful();  //设置事务成功完成

        } catch (Exception e) {
            Timber.e(e.getMessage());
            e.printStackTrace();
        }finally {
            db.endTransaction();    //结束事务
            db.close();
        }
    }

    public List<WantedMessage> queryHomeWantedMessages() {
        db = helper.getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM (HomeWantedMessage INNER JOIN Factory ON factoryId = Factory.id)  " +
                    "INNER JOIN Contacter ON contacterId = Contacter.id ORDER BY id desc LIMIT 10 ", null);
            List<WantedMessage> wantedMessages = new ArrayList<>();
            WantedMessage wantedMessage;
            Factory factory;
            Contacter contacter;
            while (c.moveToNext()) {
                wantedMessage = new WantedMessage();
                factory = new Factory();
                contacter = new Contacter();

                factory.setId(c.getString(c.getColumnIndex("factoryId")));
                factory.setTitle(c.getString(c.getColumnIndex("title")));
                factory.setThumbnail_url(c.getString(c.getColumnIndex("thumbnail_url")));
                factory.setPrice(c.getFloat(c.getColumnIndex("price")));
                factory.setRange(c.getFloat(c.getColumnIndex("range")));
                String[] stringTags = c.getString(c.getColumnIndex("tags")).split(",");
                List<Tag> tags = new ArrayList<>();
                for (String stringTag : stringTags) {
                    Tag tag = new Tag();
                    tag.setName(stringTag);
                    tags.add(tag);
                }
                factory.setTags(tags);
                String[] stringUrls = c.getString(c.getColumnIndex("image_urls")).split(",");
                factory.setImage_urls(Arrays.asList(stringUrls));
                factory.setDescription(c.getString(c.getColumnIndex("description")));
                factory.setAddress_overview(c.getString(c.getColumnIndex("address_overview")));
                factory.setPre_pay(c.getString(c.getColumnIndex("pre_pay")));
                factory.setRent_type(c.getString(c.getColumnIndex("rent_type")));
                factory.setGeohash(c.getString(c.getColumnIndex("geohash")));

                contacter.setId(c.getString(c.getColumnIndex("contacterId")));
                contacter.setName(c.getString(c.getColumnIndex("name")));
                contacter.setPhone_num(c.getString(c.getColumnIndex("phone_num")));

                wantedMessage.setId(c.getString(c.getColumnIndex("id")));
                int i = c.getInt(c.getColumnIndex("isCollect"));
                boolean a = i != 0;
                wantedMessage.setCollect(a);
                wantedMessage.setCreated_time(c.getInt(c.getColumnIndex("created_time")));
                wantedMessage.setDelete_id(c.getString(c.getColumnIndex("delete_id")));
                wantedMessage.setOwner_id(c.getString(c.getColumnIndex("owner_id")));
                wantedMessage.setUpdate_id(c.getString(c.getColumnIndex("update_id")));
                wantedMessage.setUpdate_time(c.getInt(c.getColumnIndex("update_time")));
                wantedMessage.setFactory(factory);
                wantedMessage.setContacter(contacter);

                wantedMessages.add(wantedMessage);
            }
            return wantedMessages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
    }
}
