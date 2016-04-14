/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data.entityelements;

/**
 *
 * @author Thanusaan
 */
public class Damage 
{
    private int _damage;

    public Damage(int damage) 
    {
        this._damage = damage;
    }

    public int getDamage() 
    {
        return _damage;
    }

    public void setDamage(int damage) 
    {
        this._damage = damage;
    }
}
