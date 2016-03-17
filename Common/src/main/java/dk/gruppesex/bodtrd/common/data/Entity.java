/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.common.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author lucas
 */
public class Entity
{
    private Map<Class<?>, Object> data = new ConcurrentHashMap<>();
    private int _ID;
    private static AtomicInteger _count = new AtomicInteger(0);
    private EntityType _type;

    public Entity()
    {
        _ID = _count.incrementAndGet();
    }

    public void setType(EntityType type)
    {
        this._type = type;
    }

    public EntityType getType()
    {
        return _type;
    }

    public void add(Object data)
    {
        this.data.put(data.getClass(), data);
    }

    public void remove(Class<?> type)
    {
        this.data.remove(type);
    }

    public <T> T get(Class<T> type)
    {
        return (T)data.get(type);
    }

    public int getID()
    {
        return _ID;
    }
}
