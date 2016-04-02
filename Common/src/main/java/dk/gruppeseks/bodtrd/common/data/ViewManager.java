/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data;

import dk.gruppeseks.bodtrd.common.data.entityelements.View;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author lucas
 */
public class ViewManager
{
    private static Map<String, View> _views = new ConcurrentHashMap<>();

    public static Collection<View> views()
    {
        return _views.values();
    }

    public static void createView(String path, boolean repeatImage)
    {
        View v = null;
        try
        {
            v = new View(new File(path).getCanonicalPath().replace("\\", "/"), repeatImage);
            _views.put(path, v);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static View getView(String path)
    {
        return _views.get(path);
    }

    public static void removeView(String path)
    {
        _views.remove(path);
    }
}
