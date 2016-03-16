/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.common.data;

/**
 *
 * @author Morten
 */
public class ActionHandler
{

    private static boolean[] keys;
    private static boolean[] pkeys;

    private static final int NUM_ACTIONS = 7;

    static
    {
        keys = new boolean[NUM_ACTIONS];
        pkeys = new boolean[NUM_ACTIONS];
    }

    public static void update()
    {
        for (int i = 0; i < NUM_ACTIONS; i++)
        {
            pkeys[i] = keys[i];
        }
    }

    public static void setActionActive(int k, boolean b)
    {
        keys[k] = b;
    }

    public static boolean isACtionDown(int k)
    {
        return keys[k];
    }

    public static boolean isActionPressed(int k)
    {
        return keys[k] && !pkeys[k];
    }
}
