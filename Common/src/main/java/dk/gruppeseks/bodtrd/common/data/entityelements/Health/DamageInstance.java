/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data.entityelements.Health;

import dk.gruppeseks.bodtrd.common.data.entityelements.Damage;

/**
 *
 * @author Thanusaan
 */
public class DamageInstance
{
    private Damage _damage;
    private int _ownerId;

    public DamageInstance(Damage damage, int ownerId)
    {
        this._damage = damage;
        this._ownerId = ownerId;
    }

    public int getDamage()
    {
        return _damage.getDamage();
    }

    public int getOwnerId()
    {
        return _ownerId;
    }

}
