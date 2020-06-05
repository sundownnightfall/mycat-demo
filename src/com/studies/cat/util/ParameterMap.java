package com.studies.cat.util;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author： yangh
 * @date： Created on 2020/6/5 10:10
 * @version： v1.0
 * @modified By:
 */
//该类用于封装请求参数；前端传入的请求参数不可进行修改；为了杜绝通继承方式来修改该对象的信息，我们需要将其设置为final类
public final class ParameterMap<K,V> extends HashMap<K, V> {

    //默认该类对象是可编辑的；当我们设置完参数后将其他设置为锁定状态
    private boolean isLocked = false;

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public V put(K key, V value) {
        if(isLocked){
            throw new IllegalArgumentException("被锁");
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if(isLocked){
            throw new IllegalArgumentException("被锁");
        }
        super.putAll(m);
    }

    @Override
    public V remove(Object key) {
        if(isLocked){
            throw new IllegalArgumentException("被锁");
        }
        return super.remove(key);
    }

    @Override
    public void clear() {
        if(isLocked){
            throw new IllegalArgumentException("被锁");
        }
        super.clear();
    }

    @Override
    public boolean remove(Object key, Object value) {
        if(isLocked){
            throw new IllegalArgumentException("被锁");
        }
        return super.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if(isLocked){
            throw new IllegalArgumentException("被锁");
        }
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        if(isLocked){
            throw new IllegalArgumentException("被锁");
        }
        return super.replace(key, value);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        if(isLocked){
            throw new IllegalArgumentException("被锁");
        }
        super.replaceAll(function);
    }
}
