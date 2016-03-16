/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.common.data;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Morten
 */
public class ActionHandler
{

    private static ConcurrentHashMap<Action, Boolean> keys = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Action, Boolean> pkeys = new ConcurrentHashMap<>();

    public static void update()
    {
        for (java.util.Map.Entry<Action, Boolean> entry : keys.entrySet())
        {
            pkeys.put(entry.getKey(), entry.getValue());
        }
    }

    public static void setAction(Action action, boolean b)
    {
        keys.put(action, b);
    }

    public static boolean isActive(Action action)
    {
        return keys.get(action);
    }

    public static boolean isToggled(Action action)
    {
        return keys.get(action) && !pkeys.get(action);
    }
}
